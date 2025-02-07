package com.example

import com.augustnagro.magnum.magzio.*
import zio.{ ZIO, ZIOAppDefault, ZLayer }
import com.example.repository.*
import com.example.domain.*

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
            CREATE TABLE department(
              id   SERIAL      NOT NULL,
              name VARCHAR(50) NOT NULL,
              PRIMARY KEY(id)
            )
          """

        val employeeTable =
          sql"""
            CREATE TABLE employee(
              id            SERIAL       NOT NULL,
              name          VARCHAR(100) NOT NULL,
              age           INT          NOT NULL,
              department_id INT          NOT NULL,
              PRIMARY KEY (id),
              FOREIGN KEY (department_id) REFERENCES department(id)
            )
          """

        val phoneTable =
          sql"""
            CREATE TABLE phone(
              id     SERIAL      NOT NULL,
              number VARCHAR(15) NOT NULL,
              PRIMARY KEY(id)
            )
          """

        val employeePhoneTable =
          sql"""
            CREATE TABLE employee_phone(
              employee_id INT NOT NULL,
              phone_id    INT NOT NULL,
              PRIMARY KEY (employee_id, phone_id),
              FOREIGN KEY (employee_id) REFERENCES employee(id),
              FOREIGN KEY (phone_id) REFERENCES phone(id)
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
