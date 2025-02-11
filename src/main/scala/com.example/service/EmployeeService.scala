package com.example.service

import com.example.domain.Employee
import com.example.error.AppError.*
import com.example.repository.{ DepartmentRepository, EmployeeRepository }
import zio.*

trait EmployeeService:
  def create(employee: Employee): IO[DepartmentNotFound, Int]
  def retrieveAll: UIO[Vector[Employee]]
  def retrieveById(employeeId: Int): IO[EmployeeNotFound, Employee]
  def update(employeeId: Int, employee: Employee): IO[EmployeeNotFound, Unit]
  def delete(employeeId: Int): UIO[Unit]

final case class EmployeeServiceLive(employeeRepository: EmployeeRepository, departmentRepository: DepartmentRepository)
    extends EmployeeService:
  override def create(employee: Employee): IO[DepartmentNotFound, Int] =
    departmentRepository.retrieve(employee.departmentId).someOrFail(DepartmentNotFound)
      *> employeeRepository.create(employee)

  override def retrieveAll: UIO[Vector[Employee]] = employeeRepository.retrieveAll

  override def retrieveById(employeeId: Int): IO[EmployeeNotFound, Employee] =
    employeeRepository.retrieve(employeeId).someOrFail(EmployeeNotFound)

  override def update(employeeId: Int, employee: Employee): IO[EmployeeNotFound, Unit] =
    employeeRepository.retrieve(employeeId).someOrFail(EmployeeNotFound)
      *> employeeRepository.update(employeeId, employee)

  override def delete(employeeId: Int): UIO[Unit] = employeeRepository.delete(employeeId)

object EmployeeServiceLive:
  val layer: URLayer[EmployeeRepository & DepartmentRepository, EmployeeService] =
    ZLayer.fromFunction(EmployeeServiceLive(_, _))
