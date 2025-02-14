package com.example.api.handler

import com.example.domain.{ Employee, EmployeeId }
import com.example.error.AppError.{ DepartmentNotFound, EmployeeNotFound }
import com.example.service.EmployeeService
import zio.*

trait EmployeeHandlers:
  def createEmployeeHandler(employee: Employee): ZIO[EmployeeService, DepartmentNotFound, EmployeeId] =
    ZIO.serviceWithZIO[EmployeeService](_.create(employee))

  val getEmployeesHandler: URIO[EmployeeService, Vector[Employee]] =
    ZIO.serviceWithZIO[EmployeeService](_.retrieveAll)

  def getEmployeeHandler(id: EmployeeId): ZIO[EmployeeService, EmployeeNotFound, Employee] =
    ZIO.serviceWithZIO[EmployeeService](_.retrieveById(id))

  def updateEmployeeHandler(
    employeeId: EmployeeId,
    employee: Employee
  ): ZIO[EmployeeService, EmployeeNotFound, Unit] =
    ZIO.serviceWithZIO[EmployeeService](_.update(employeeId, employee))

  def deleteEmployeeHandler(id: EmployeeId): URIO[EmployeeService, Unit] =
    ZIO.serviceWithZIO[EmployeeService](_.delete(id))
