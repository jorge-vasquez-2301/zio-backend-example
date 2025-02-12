package com.example.api.handler

import com.example.domain.Department
import com.example.error.AppError.{ DepartmentAlreadyExists, DepartmentNotFound }
import com.example.service.DepartmentService
import zio.*

trait DepartmentHandlers:
  def createDepartmentHandler(department: Department): ZIO[DepartmentService, DepartmentAlreadyExists, Int] =
    ZIO.serviceWithZIO[DepartmentService](_.create(department))

  val getDepartmentsHandler: URIO[DepartmentService, Vector[Department]] =
    ZIO.serviceWithZIO[DepartmentService](_.retrieveAll)

  def getDepartmentHandler(id: Int): ZIO[DepartmentService, DepartmentNotFound, Department] =
    ZIO.serviceWithZIO[DepartmentService](_.retrieveById(id))

  def updateDepartmentHandler(
    departmentId: Int,
    department: Department
  ): ZIO[DepartmentService, DepartmentNotFound, Unit] =
    ZIO.serviceWithZIO[DepartmentService](_.update(departmentId, department))

  def deleteDepartmentHandler(id: Int): URIO[DepartmentService, Unit] =
    ZIO.serviceWithZIO[DepartmentService](_.delete(id))
