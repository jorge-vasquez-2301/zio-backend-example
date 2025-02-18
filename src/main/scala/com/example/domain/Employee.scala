package com.example.domain

import com.example.util.given
import zio.schema.*

final case class Employee(name: EmployeeName, age: Age, departmentId: DepartmentId) derives Schema
