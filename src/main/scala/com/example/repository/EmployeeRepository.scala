package com.example.repository

import com.augustnagro.magnum.magzio.*
import com.example.domain.{ Employee, EmployeeId }
import com.example.tables
import com.example.util.given
import io.github.iltotore.iron.*
import zio.*

trait EmployeeRepository:
  def create(employee: Employee): UIO[EmployeeId]
  def retrieve(employeeId: EmployeeId): UIO[Option[Employee]]
  def retrieveAll: UIO[Vector[Employee]]
  def update(employeeId: EmployeeId, employee: Employee): UIO[Unit]
  def delete(employeeId: EmployeeId): UIO[Unit]

final case class EmployeeRepositoryLive(xa: Transactor)
    extends Repo[Employee, tables.Employee, EmployeeId]
    with EmployeeRepository:

  override def create(employee: Employee): UIO[EmployeeId] =
    xa.transact {
      insertReturning(employee).id
    }.orDie

  override def retrieve(employeeId: EmployeeId): UIO[Option[Employee]] =
    xa.transact {
      findById(employeeId).map(_.toDomain)
    }.orDie

  override val retrieveAll: UIO[Vector[Employee]] =
    xa.transact {
      findAll.map(_.toDomain)
    }.orDie

  override def update(employeeId: EmployeeId, employee: Employee): UIO[Unit] =
    xa.transact {
      update(tables.Employee.fromDomain(employeeId, employee))
    }.orDie

  override def delete(employeeId: EmployeeId): UIO[Unit] =
    xa.transact {
      deleteById(employeeId)
    }.orDie

object EmployeeRepositoryLive:
  val layer: URLayer[Transactor, EmployeeRepository] = ZLayer.fromFunction(EmployeeRepositoryLive(_))
