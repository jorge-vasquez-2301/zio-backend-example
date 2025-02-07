package com.example.repository

import com.example.domain.Employee
import com.example.tables

import com.augustnagro.magnum.magzio.*
import zio.*

trait EmployeeRepository:
  def create(employee: Employee): UIO[Int]
  def retrieve(employeeId: Int): UIO[Option[Employee]]
  def update(employeeId: Int, employee: Employee): UIO[Unit]
  def delete(employeeId: Int): UIO[Unit]

final case class EmployeeRepositoryLive(xa: Transactor)
    extends Repo[tables.Employee.Creator, tables.Employee, Int]
    with EmployeeRepository:

  override def create(employee: Employee): UIO[Int] =
    xa.transact {
      insertReturning(tables.Employee.Creator.fromDomain(employee)).id
    }.orDie

  override def retrieve(employeeId: Int): UIO[Option[Employee]] =
    xa.transact {
      findById(employeeId).map(_.toDomain)
    }.orDie

  override def update(employeeId: Int, employee: Employee): UIO[Unit] =
    xa.transact {
      update(tables.Employee.fromDomain(employeeId, employee))
    }.orDie

  override def delete(employeeId: Int): UIO[Unit] =
    xa.transact {
      deleteById(employeeId)
    }.orDie

object EmployeeRepositoryLive:
  val layer: URLayer[Transactor, EmployeeRepository] = ZLayer.fromFunction(EmployeeRepositoryLive(_))
