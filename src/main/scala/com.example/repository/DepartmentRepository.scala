package com.example.repository

import com.example.domain.Department
import com.example.tables

import com.augustnagro.magnum.magzio.*
import zio.*

trait DepartmentRepository:
  def create(department: Department): UIO[Int]
  def retrieve(departmentId: Int): UIO[Option[Department]]
  def update(departmentId: Int, department: Department): UIO[Unit]
  def delete(departmentId: Int): UIO[Unit]

final case class DepartmentRepositoryLive(xa: Transactor)
    extends Repo[tables.Department.Creator, tables.Department, Int]
    with DepartmentRepository:

  override def create(department: Department): UIO[Int] =
    xa.transact {
      insertReturning(tables.Department.Creator.fromDomain(department)).id
    }.orDie

  override def retrieve(departmentId: Int): UIO[Option[Department]] =
    xa.transact {
      findById(departmentId).map(_.toDomain)
    }.orDie

  override def update(departmentId: Int, department: Department): UIO[Unit] =
    xa.transact {
      update(tables.Department.fromDomain(departmentId, department))
    }.orDie

  override def delete(departmentId: Int): UIO[Unit] =
    xa.transact {
      deleteById(departmentId)
    }.orDie

object DepartmentRepositoryLive:
  val layer: URLayer[Transactor, DepartmentRepository] = ZLayer.fromFunction(DepartmentRepositoryLive(_))
