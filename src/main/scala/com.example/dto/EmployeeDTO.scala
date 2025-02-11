package com.example.dto

import com.example.domain.Employee
import zio.schema.*

final case class EmployeeDTO(name: String, age: Int, departmentId: Int) derives Schema:
  val toDomain = Employee(name, age, departmentId)

object EmployeeDTO:
  def fromDomain(employee: Employee): EmployeeDTO = EmployeeDTO(employee.name, employee.age, employee.departmentId)
