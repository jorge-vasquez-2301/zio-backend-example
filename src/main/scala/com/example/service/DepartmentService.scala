package com.example.service

import com.example.domain.Department
import com.example.error.AppError.*
import com.example.repository.DepartmentRepository
import zio.*

trait DepartmentService:
  def create(department: Department): IO[DepartmentAlreadyExists, Int]
  def retrieveAll: UIO[Vector[Department]]
  def retrieveById(departmentId: Int): IO[DepartmentNotFound, Department]
  def update(departmentId: Int, department: Department): IO[DepartmentNotFound, Unit]
  def delete(departmentId: Int): UIO[Unit]

final case class DepartmentServiceLive(departmentRepository: DepartmentRepository) extends DepartmentService:
  override def create(department: Department): IO[DepartmentAlreadyExists, Int] =
    for
      maybeDepartment <- departmentRepository.retrieveByName(department.name)
      departmentId    <- maybeDepartment match
                           case Some(_) => ZIO.fail(DepartmentAlreadyExists)
                           case None    => departmentRepository.create(department)
    yield departmentId

  override def retrieveAll: UIO[Vector[Department]] = departmentRepository.retrieveAll

  override def retrieveById(departmentId: Int): IO[DepartmentNotFound, Department] =
    departmentRepository.retrieve(departmentId).someOrFail(DepartmentNotFound)

  override def update(departmentId: Int, department: Department): IO[DepartmentNotFound, Unit] =
    departmentRepository.retrieve(departmentId).someOrFail(DepartmentNotFound)
      *> departmentRepository.update(departmentId, department)

  override def delete(departmentId: Int): UIO[Unit] = departmentRepository.delete(departmentId)

object DepartmentServiceLive:
  val layer: URLayer[DepartmentRepository, DepartmentService] = ZLayer.fromFunction(DepartmentServiceLive(_))
