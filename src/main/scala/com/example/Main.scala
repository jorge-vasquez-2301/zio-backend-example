package com.example

import com.augustnagro.magnum.magzio.*
import com.example.api.Router
import com.example.repository.*
import com.example.service.*
import com.zaxxer.hikari.{ HikariConfig, HikariDataSource }
import org.testcontainers.containers.PostgreSQLContainer
import zio.*
import zio.http.*
import zio.logging.jul.bridge.JULBridge
import zio.logging.{ConsoleLoggerConfig, LogFilter, LogFormat, LoggerNameExtractor, consoleLogger}

object Main extends ZIOAppDefault with Router:
  val logFormat =
    LogFormat.label("name", LoggerNameExtractor.loggerNameAnnotationOrTrace.toLogFormat())
      + LogFormat.space
      + LogFormat.default
      + LogFormat.space
      + LogFormat.allAnnotations

  val logFilterConfig =
    LogFilter.LogLevelByNameConfig(
      LogLevel.Info,
      "com.augustnagro.magnum" -> LogLevel.Debug
    )

  override val bootstrap =
    Runtime.removeDefaultLoggers
      ++ consoleLogger(ConsoleLoggerConfig(logFormat, logFilterConfig))
      ++ JULBridge.init(logFilterConfig.toFilter)

  val startPostgresContainer =
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

  def createDataSource(jdbcUrl: String, username: String, password: String) =
    ZIO.fromAutoCloseable {
      ZIO.attemptBlockingIO {
        val config = HikariConfig()
        config.setJdbcUrl(jdbcUrl)
        config.setUsername(username)
        config.setPassword(password)
        HikariDataSource(config)
      }
    }

  def createTables(xa: Transactor) =
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

  val dataSourceLayer =
    ZLayer.scoped {
      for
        postgresContainer <- startPostgresContainer
        dataSource        <- createDataSource(
                               postgresContainer.getJdbcUrl,
                               postgresContainer.getUsername,
                               postgresContainer.getPassword
                             )
      yield dataSource
    }

  val dbLayer =
    for
      dataSource <- dataSourceLayer
      xa         <- Transactor.layer(dataSource.get)
      _          <- ZLayer(createTables(xa.get))
    yield xa

  val run =
    Server
      .serve(routes ++ swaggerRoutes)
      .provide(
        Server.default,
        DepartmentServiceLive.layer,
        DepartmentRepositoryLive.layer,
        EmployeeServiceLive.layer,
        EmployeeRepositoryLive.layer,
        PhoneServiceLive.layer,
        PhoneRepositoryLive.layer,
        EmployeePhoneServiceLive.layer,
        EmployeePhoneRepositoryLive.layer,
        dbLayer
      )
