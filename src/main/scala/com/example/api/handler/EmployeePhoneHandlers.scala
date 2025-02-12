package com.example.api.handler

import com.example.domain.Phone
import com.example.error.AppError
import com.example.error.AppError.EmployeeNotFound
import com.example.service.EmployeePhoneService
import zio.*

trait EmployeePhoneHandlers:
  def addPhoneToEmployeeHandler(phoneId: Int, employeeId: Int): ZIO[EmployeePhoneService, AppError, Unit] =
    ZIO.serviceWithZIO[EmployeePhoneService](_.addPhoneToEmployee(phoneId, employeeId))

  def retrieveEmployeePhonesHandler(employeeId: Int): ZIO[EmployeePhoneService, EmployeeNotFound, Vector[Phone]] =
    ZIO.serviceWithZIO[EmployeePhoneService](_.retrieveEmployeePhones(employeeId))

  def removePhoneFromEmployeeHandler(phoneId: Int, employeeId: Int): ZIO[EmployeePhoneService, AppError, Unit] =
    ZIO.serviceWithZIO[EmployeePhoneService](_.removePhoneFromEmployee(phoneId, employeeId))
