package com.example.domain

import com.example.util.given
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import zio.schema.*

type EmployeeName =
  DescribedAs[
    Alphanumeric & Not[Empty] & MaxLength[100],
    "Employee's name should be alphanumeric, non-empty and have a maximum length of 100"
  ]

type Age = DescribedAs[Greater[0], "Employee's age should be strictly positive"]

type EmployeeId = DescribedAs[Greater[0], "Employee's ID should be strictly positive"]

final case class Employee(name: String :| EmployeeName, age: Int :| Age, departmentId: Int :| DepartmentId)
    derives Schema
