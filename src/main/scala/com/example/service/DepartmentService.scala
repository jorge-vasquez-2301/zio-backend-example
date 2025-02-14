package com.example.service

import com.example.domain.{ Department, DepartmentId }
import com.example.error.AppError.*
import com.example.repository.DepartmentRepository
import zio.*

trait DepartmentService:
  def create(department: Department): IO[DepartmentAlreadyExists, DepartmentId]
  def retrieveAll: UIO[Vector[Department]]
  def retrieveById(departmentId: DepartmentId): IO[DepartmentNotFound, Department]
  def update(departmentId: DepartmentId, department: Department): IO[DepartmentNotFound, Unit]
  def delete(departmentId: DepartmentId): UIO[Unit]

final case class DepartmentServiceLive(departmentRepository: DepartmentRepository) extends DepartmentService:
  override def create(department: Department): IO[DepartmentAlreadyExists, DepartmentId] =
    for
      maybeDepartment <- departmentRepository.retrieveByName(department.name)
      departmentId    <- maybeDepartment match
                           case Some(_) => ZIO.fail(DepartmentAlreadyExists)
                           case None    => departmentRepository.create(department)
    yield departmentId

  override def retrieveAll: UIO[Vector[Department]] = departmentRepository.retrieveAll

  override def retrieveById(departmentId: DepartmentId): IO[DepartmentNotFound, Department] =
    departmentRepository.retrieve(departmentId).someOrFail(DepartmentNotFound)

  override def update(departmentId: DepartmentId, department: Department): IO[DepartmentNotFound, Unit] =
    departmentRepository.retrieve(departmentId).someOrFail(DepartmentNotFound)
      *> departmentRepository.update(departmentId, department)

  override def delete(departmentId: DepartmentId): UIO[Unit] = departmentRepository.delete(departmentId)

object DepartmentServiceLive:
  val layer: URLayer[DepartmentRepository, DepartmentService] = ZLayer.fromFunction(DepartmentServiceLive(_))
