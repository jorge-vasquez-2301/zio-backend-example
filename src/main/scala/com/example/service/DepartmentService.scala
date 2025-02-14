package com.example.service

import com.example.domain.{ Department, DepartmentId }
import com.example.error.AppError.*
import com.example.repository.DepartmentRepository
import io.github.iltotore.iron.*
import zio.*

trait DepartmentService:
  def create(department: Department): IO[DepartmentAlreadyExists, Int :| DepartmentId]
  def retrieveAll: UIO[Vector[Department]]
  def retrieveById(departmentId: Int :| DepartmentId): IO[DepartmentNotFound, Department]
  def update(departmentId: Int :| DepartmentId, department: Department): IO[DepartmentNotFound, Unit]
  def delete(departmentId: Int :| DepartmentId): UIO[Unit]

final case class DepartmentServiceLive(departmentRepository: DepartmentRepository) extends DepartmentService:
  override def create(department: Department): IO[DepartmentAlreadyExists, Int :| DepartmentId] =
    for
      maybeDepartment <- departmentRepository.retrieveByName(department.name)
      departmentId    <- maybeDepartment match
                           case Some(_) => ZIO.fail(DepartmentAlreadyExists)
                           case None    => departmentRepository.create(department)
    yield departmentId

  override def retrieveAll: UIO[Vector[Department]] = departmentRepository.retrieveAll

  override def retrieveById(departmentId: Int :| DepartmentId): IO[DepartmentNotFound, Department] =
    departmentRepository.retrieve(departmentId).someOrFail(DepartmentNotFound)

  override def update(departmentId: Int :| DepartmentId, department: Department): IO[DepartmentNotFound, Unit] =
    departmentRepository.retrieve(departmentId).someOrFail(DepartmentNotFound)
      *> departmentRepository.update(departmentId, department)

  override def delete(departmentId: Int :| DepartmentId): UIO[Unit] = departmentRepository.delete(departmentId)

object DepartmentServiceLive:
  val layer: URLayer[DepartmentRepository, DepartmentService] = ZLayer.fromFunction(DepartmentServiceLive(_))
