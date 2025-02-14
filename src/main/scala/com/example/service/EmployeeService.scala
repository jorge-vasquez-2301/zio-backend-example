package com.example.service

import com.example.domain.{ Employee, EmployeeId }
import com.example.error.AppError.*
import com.example.repository.{ DepartmentRepository, EmployeeRepository }
import zio.*

trait EmployeeService:
  def create(employee: Employee): IO[DepartmentNotFound, EmployeeId]
  def retrieveAll: UIO[Vector[Employee]]
  def retrieveById(employeeId: EmployeeId): IO[EmployeeNotFound, Employee]
  def update(employeeId: EmployeeId, employee: Employee): IO[EmployeeNotFound, Unit]
  def delete(employeeId: EmployeeId): UIO[Unit]

final case class EmployeeServiceLive(employeeRepository: EmployeeRepository, departmentRepository: DepartmentRepository)
    extends EmployeeService:
  override def create(employee: Employee): IO[DepartmentNotFound, EmployeeId] =
    departmentRepository.retrieve(employee.departmentId).someOrFail(DepartmentNotFound)
      *> employeeRepository.create(employee)

  override def retrieveAll: UIO[Vector[Employee]] = employeeRepository.retrieveAll

  override def retrieveById(employeeId: EmployeeId): IO[EmployeeNotFound, Employee] =
    employeeRepository.retrieve(employeeId).someOrFail(EmployeeNotFound)

  override def update(employeeId: EmployeeId, employee: Employee): IO[EmployeeNotFound, Unit] =
    employeeRepository.retrieve(employeeId).someOrFail(EmployeeNotFound)
      *> employeeRepository.update(employeeId, employee)

  override def delete(employeeId: EmployeeId): UIO[Unit] = employeeRepository.delete(employeeId)

object EmployeeServiceLive:
  val layer: URLayer[EmployeeRepository & DepartmentRepository, EmployeeService] =
    ZLayer.fromFunction(EmployeeServiceLive(_, _))
