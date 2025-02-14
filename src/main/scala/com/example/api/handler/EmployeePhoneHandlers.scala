package com.example.api.handler

import com.example.domain.{ EmployeeId, Phone, PhoneId }
import com.example.error.AppError
import com.example.error.AppError.EmployeeNotFound
import com.example.service.EmployeePhoneService
import zio.*

trait EmployeePhoneHandlers:
  def addPhoneToEmployeeHandler(
    employeeId: EmployeeId,
    phoneId: PhoneId
  ): ZIO[EmployeePhoneService, AppError, Unit] =
    ZIO.serviceWithZIO[EmployeePhoneService](_.addPhoneToEmployee(phoneId, employeeId))

  def retrieveEmployeePhonesHandler(
    employeeId: EmployeeId
  ): ZIO[EmployeePhoneService, EmployeeNotFound, Vector[Phone]] =
    ZIO.serviceWithZIO[EmployeePhoneService](_.retrieveEmployeePhones(employeeId))

  def removePhoneFromEmployeeHandler(
    employeeId: EmployeeId,
    phoneId: PhoneId
  ): ZIO[EmployeePhoneService, AppError, Unit] =
    ZIO.serviceWithZIO[EmployeePhoneService](_.removePhoneFromEmployee(phoneId, employeeId))
