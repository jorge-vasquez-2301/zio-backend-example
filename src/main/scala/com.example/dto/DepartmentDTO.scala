package com.example.dto

import com.example.domain.Department
import zio.schema.*

final case class DepartmentDTO(name: String) derives Schema:
  val toDomain = Department(name)

object DepartmentDTO:
  def fromDomain(department: Department): DepartmentDTO = DepartmentDTO(department.name)
