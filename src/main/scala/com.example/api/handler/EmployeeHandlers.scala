package com.example.api.handler

import com.example.dto.EmployeeDTO
import com.example.error.AppError.{ DepartmentNotFound, EmployeeNotFound }
import com.example.service.EmployeeService
import zio.*

trait EmployeeHandlers:
  def createEmployeeHandler(employee: EmployeeDTO): ZIO[EmployeeService, DepartmentNotFound, Int] =
    ZIO.serviceWithZIO[EmployeeService](_.create(employee.toDomain))

  val getEmployeesHandler: URIO[EmployeeService, Vector[EmployeeDTO]] =
    ZIO.serviceWithZIO[EmployeeService](_.retrieveAll.map(_.map(EmployeeDTO.fromDomain)))

  def getEmployeeHandler(id: Int): ZIO[EmployeeService, EmployeeNotFound, EmployeeDTO] =
    ZIO.serviceWithZIO[EmployeeService](_.retrieveById(id).map(EmployeeDTO.fromDomain))

  def updateEmployeeHandler(
    employeeId: Int,
    employee: EmployeeDTO
  ): ZIO[EmployeeService, EmployeeNotFound, Unit] =
    ZIO.serviceWithZIO[EmployeeService](_.update(employeeId, employee.toDomain))

  def deleteEmployeeHandler(id: Int): URIO[EmployeeService, Unit] =
    ZIO.serviceWithZIO[EmployeeService](_.delete(id))
