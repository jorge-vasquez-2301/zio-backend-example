package com.example.service

import com.example.domain.Phone
import com.example.error.AppError
import com.example.error.AppError.*
import com.example.repository.{ EmployeePhoneRepository, EmployeeRepository, PhoneRepository }
import zio.*

trait EmployeePhoneService:
  def addPhoneToEmployee(phoneId: Int, employeeId: Int): IO[AppError, Unit]
  def retrieveEmployeePhones(employeeId: Int): IO[EmployeeNotFound, Vector[Phone]]
  def removePhoneFromEmployee(phoneId: Int, employeeId: Int): IO[AppError, Unit]

final case class EmployeePhoneServiceLive(
  employeePhoneRepository: EmployeePhoneRepository,
  employeeRepository: EmployeeRepository,
  phoneRepository: PhoneRepository
) extends EmployeePhoneService:
  override def addPhoneToEmployee(phoneId: Int, employeeId: Int): IO[AppError, Unit] =
    phoneRepository.retrieve(phoneId).someOrFail(PhoneNotFound)
      *> employeeRepository.retrieve(employeeId).someOrFail(EmployeeNotFound)
      *> employeePhoneRepository.addPhoneToEmployee(phoneId, employeeId)

  override def retrieveEmployeePhones(employeeId: Int): IO[EmployeeNotFound, Vector[Phone]] =
    employeeRepository.retrieve(employeeId).someOrFail(EmployeeNotFound)
      *> employeePhoneRepository.retrieveEmployeePhones(employeeId)

  override def removePhoneFromEmployee(phoneId: Int, employeeId: Int): IO[AppError, Unit] =
    phoneRepository.retrieve(phoneId).someOrFail(PhoneNotFound)
      *> employeeRepository.retrieve(employeeId).someOrFail(EmployeeNotFound)
      *> employeePhoneRepository.removePhoneFromEmployee(phoneId, employeeId)

object EmployeePhoneServiceLive:
  val layer: URLayer[EmployeePhoneRepository & EmployeeRepository & PhoneRepository, EmployeePhoneService] =
    ZLayer.fromFunction(EmployeePhoneServiceLive(_, _, _))
