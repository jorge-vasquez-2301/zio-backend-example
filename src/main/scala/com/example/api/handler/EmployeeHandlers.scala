package com.example.api.handler

import com.example.domain.{ Employee, EmployeeId }
import com.example.error.AppError.{ DepartmentNotFound, EmployeeNotFound }
import com.example.service.EmployeeService
import io.github.iltotore.iron.*
import zio.*

trait EmployeeHandlers:
  def createEmployeeHandler(employee: Employee): ZIO[EmployeeService, DepartmentNotFound, Int :| EmployeeId] =
    ZIO.serviceWithZIO[EmployeeService](_.create(employee))

  val getEmployeesHandler: URIO[EmployeeService, Vector[Employee]] =
    ZIO.serviceWithZIO[EmployeeService](_.retrieveAll)

  def getEmployeeHandler(id: Int :| EmployeeId): ZIO[EmployeeService, EmployeeNotFound, Employee] =
    ZIO.serviceWithZIO[EmployeeService](_.retrieveById(id))

  def updateEmployeeHandler(
    employeeId: Int :| EmployeeId,
    employee: Employee
  ): ZIO[EmployeeService, EmployeeNotFound, Unit] =
    ZIO.serviceWithZIO[EmployeeService](_.update(employeeId, employee))

  def deleteEmployeeHandler(id: Int :| EmployeeId): URIO[EmployeeService, Unit] =
    ZIO.serviceWithZIO[EmployeeService](_.delete(id))
