package com.example.domain

import com.example.util.given
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import zio.schema.*

type EmployeeNameDescription =
  DescribedAs[
    Alphanumeric & Not[Empty] & MaxLength[100],
    "Employee's name should be alphanumeric, non-empty and have a maximum length of 100"
  ]

type EmployeeName = String :| EmployeeNameDescription

type AgeDescription = DescribedAs[Greater[0], "Employee's age should be strictly positive"]

type Age = Int :| AgeDescription

type EmployeeIdDescription = DescribedAs[Greater[0], "Employee's ID should be strictly positive"]

type EmployeeId = Int :| EmployeeIdDescription

final case class Employee(name: EmployeeName, age: Age, departmentId: DepartmentId) derives Schema
