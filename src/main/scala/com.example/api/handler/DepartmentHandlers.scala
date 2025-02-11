package com.example.api.handler

import com.example.dto.DepartmentDTO
import com.example.error.AppError.{ DepartmentAlreadyExists, DepartmentNotFound }
import com.example.service.DepartmentService
import zio.*

trait DepartmentHandlers:
  def createDepartmentHandler(department: DepartmentDTO): ZIO[DepartmentService, DepartmentAlreadyExists, Int] =
    ZIO.serviceWithZIO[DepartmentService](_.create(department.toDomain))

  val getDepartmentsHandler: URIO[DepartmentService, Vector[DepartmentDTO]] =
    ZIO.serviceWithZIO[DepartmentService](_.retrieveAll.map(_.map(DepartmentDTO.fromDomain)))

  def getDepartmentHandler(id: Int): ZIO[DepartmentService, DepartmentNotFound, DepartmentDTO] =
    ZIO.serviceWithZIO[DepartmentService](_.retrieveById(id).map(DepartmentDTO.fromDomain))

  def updateDepartmentHandler(
    departmentId: Int,
    department: DepartmentDTO
  ): ZIO[DepartmentService, DepartmentNotFound, Unit] =
    ZIO.serviceWithZIO[DepartmentService](_.update(departmentId, department.toDomain))

  def deleteDepartmentHandler(id: Int): URIO[DepartmentService, Unit] =
    ZIO.serviceWithZIO[DepartmentService](_.delete(id))
