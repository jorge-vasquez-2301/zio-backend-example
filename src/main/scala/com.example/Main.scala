package com.example

import com.augustnagro.magnum.magzio.*
import zio.{ ZIO, ZIOAppDefault, ZLayer }
import com.example.repository.*
import com.example.domain.*
import com.example.tables

import scala.concurrent.duration.*
import org.testcontainers.containers.PostgreSQLContainer
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object Main extends ZIOAppDefault:
  val postgresContainerLayer =
    ZLayer.scoped {
      ZIO.fromAutoCloseable {
        ZIO.attemptBlockingIO {
          val container = PostgreSQLContainer("postgres:13.18-alpine3.20")
          container.withDatabaseName("example")
          container.withUsername("sa")
          container.withPassword("sa")
          container.start()
          container
        }
      }
    }

  def dataSourceLayer(jdbcUrl: String, username: String, password: String) =
    ZLayer.scoped {
      ZIO.fromAutoCloseable {
        ZIO.attemptBlockingIO {
          val config = HikariConfig()
          config.setJdbcUrl(jdbcUrl)
          config.setUsername(username)
          config.setPassword(password)
          HikariDataSource(config)
        }
      }
    }

  def createTablesLayer(xa: Transactor) =
    ZLayer {
      xa.transact {
        val departmentTable =
          sql"""
            CREATE TABLE ${tables.Department.table}(
              ${tables.Department.table.id}   SERIAL      NOT NULL,
              ${tables.Department.table.name} VARCHAR(50) NOT NULL,
              PRIMARY KEY(${tables.Department.table.id})
            )
          """

        val employeeTable =
          sql"""
            CREATE TABLE ${tables.Employee.table}(
              ${tables.Employee.table.id}            SERIAL       NOT NULL,
              ${tables.Employee.table.name}          VARCHAR(100) NOT NULL,
              ${tables.Employee.table.age}           INT          NOT NULL,
              ${tables.Employee.table.departmentId}  INT          NOT NULL,
              PRIMARY KEY (${tables.Employee.table.id}),
              FOREIGN KEY (${tables.Employee.table.departmentId}) REFERENCES ${tables.Department.table}(${tables.Department.table.id})
            )
          """

        val phoneTable =
          sql"""
            CREATE TABLE ${tables.Phone.table}(
              ${tables.Phone.table.id}     SERIAL      NOT NULL,
              ${tables.Phone.table.number} VARCHAR(15) NOT NULL,
              PRIMARY KEY(${tables.Phone.table.id})
            )
          """

        val employeePhoneTable =
          sql"""
            CREATE TABLE ${tables.EmployeePhone.table}(
              ${tables.EmployeePhone.table.employeeId} INT NOT NULL,
              ${tables.EmployeePhone.table.phoneId}    INT NOT NULL,
              PRIMARY KEY (${tables.EmployeePhone.table.employeeId}, ${tables.EmployeePhone.table.phoneId}),
              FOREIGN KEY (${tables.EmployeePhone.table.employeeId}) REFERENCES ${tables.Employee.table}(${tables.Employee.table.id}),
              FOREIGN KEY (${tables.EmployeePhone.table.phoneId}) REFERENCES ${tables.Phone.table}(${tables.Phone.table.id})
            )
          """

        departmentTable.update.run()
        employeeTable.update.run()
        phoneTable.update.run()
        employeePhoneTable.update.run()
      }
    }

  val dbLayer =
    for
      postgresContainer <- postgresContainerLayer
      dataSource        <- dataSourceLayer(
                             postgresContainer.get.getJdbcUrl(),
                             postgresContainer.get.getUsername(),
                             postgresContainer.get.getPassword()
                           )
      xa                <- Transactor.layer(dataSource.get)
      _                 <- createTablesLayer(xa.get)
    yield xa

  val run =
    (for
      departmentId <- ZIO.serviceWithZIO[DepartmentRepository](_.create(Department("test")))
      _            <- ZIO.serviceWithZIO[DepartmentRepository](_.retrieve(departmentId)).debug("department")
      _            <- ZIO.serviceWithZIO[DepartmentRepository](_.update(departmentId, Department("new test")))
      _            <- ZIO.serviceWithZIO[DepartmentRepository](_.retrieve(departmentId)).debug("updated department")
      employeeId   <- ZIO.serviceWithZIO[EmployeeRepository](_.create(Employee("test", 30, departmentId)))
      _            <- ZIO.serviceWithZIO[EmployeeRepository](_.retrieve(employeeId)).debug("employee")
      _            <- ZIO.serviceWithZIO[EmployeeRepository](_.update(employeeId, Employee("new test", 40, departmentId)))
      _            <- ZIO.serviceWithZIO[EmployeeRepository](_.retrieve(employeeId)).debug("updated employee")
      phoneId      <- ZIO.serviceWithZIO[PhoneRepository](_.create(Phone("5252525")))
      _            <- ZIO.serviceWithZIO[PhoneRepository](_.retrieve(phoneId)).debug("phone")
      _            <- ZIO.serviceWithZIO[PhoneRepository](_.update(phoneId, Phone("7272727")))
      _            <- ZIO.serviceWithZIO[PhoneRepository](_.retrieve(phoneId)).debug("updated phone")
      _            <- ZIO.serviceWithZIO[EmployeePhoneRepository](_.addPhoneToEmployee(phoneId, employeeId))
      _            <- ZIO.serviceWithZIO[EmployeePhoneRepository](_.retrieveEmployeePhones(employeeId)).debug("employeePhones")
      _            <- ZIO.serviceWithZIO[EmployeePhoneRepository](_.removePhoneFromEmployee(phoneId, employeeId))
      _            <- ZIO.serviceWithZIO[EmployeePhoneRepository](_.retrieveEmployeePhones(employeeId)).debug("employeePhones")
      _            <- ZIO.serviceWithZIO[PhoneRepository](_.delete(phoneId))
      _            <- ZIO.serviceWithZIO[EmployeeRepository](_.delete(employeeId))
      _            <- ZIO.serviceWithZIO[DepartmentRepository](_.delete(departmentId))
    yield ()).provide(
      DepartmentRepositoryLive.layer,
      EmployeeRepositoryLive.layer,
      PhoneRepositoryLive.layer,
      EmployeePhoneRepositoryLive.layer,
      dbLayer
    )
