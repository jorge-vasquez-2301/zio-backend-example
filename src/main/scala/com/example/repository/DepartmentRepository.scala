package com.example.repository

import com.augustnagro.magnum.magzio.*
import com.example.domain.{ Department, DepartmentId, DepartmentName }
import com.example.tables
import com.example.util.given
import io.github.iltotore.iron.*
import zio.*

trait DepartmentRepository:
  def create(department: Department): UIO[DepartmentId]
  def retrieve(departmentId: DepartmentId): UIO[Option[Department]]
  def retrieveByName(departmentName: DepartmentName): UIO[Option[Department]]
  def retrieveAll: UIO[Vector[Department]]
  def update(departmentId: DepartmentId, department: Department): UIO[Unit]
  def delete(departmentId: DepartmentId): UIO[Unit]

final case class DepartmentRepositoryLive(xa: Transactor)
    extends Repo[Department, tables.Department, DepartmentId]
    with DepartmentRepository:

  override def create(department: Department): UIO[DepartmentId] =
    xa.transact {
      insertReturning(department).id
    }.orDie

  override def retrieve(departmentId: DepartmentId): UIO[Option[Department]] =
    xa.transact {
      findById(departmentId).map(_.toDomain)
    }.orDie

  override def retrieveByName(departmentName: DepartmentName): UIO[Option[Department]] =
    xa.transact {
      val spec = Spec[tables.Department].where(sql"${tables.Department.table.name} = $departmentName")

      findAll(spec).headOption.map(_.toDomain)
    }.orDie

  override val retrieveAll: UIO[Vector[Department]] =
    xa.transact {
      findAll.map(_.toDomain)
    }.orDie

  override def update(departmentId: DepartmentId, department: Department): UIO[Unit] =
    xa.transact {
      update(tables.Department.fromDomain(departmentId, department))
    }.orDie

  override def delete(departmentId: DepartmentId): UIO[Unit] =
    xa.transact {
      deleteById(departmentId)
    }.orDie

object DepartmentRepositoryLive:
  val layer: URLayer[Transactor, DepartmentRepository] = ZLayer.fromFunction(DepartmentRepositoryLive(_))
