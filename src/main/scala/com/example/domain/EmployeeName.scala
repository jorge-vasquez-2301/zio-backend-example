package com.example.domain

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

type EmployeeNameDescription =
  DescribedAs[
    Alphanumeric & Not[Empty] & MaxLength[100],
    "Employee's name should be alphanumeric, non-empty and have a maximum length of 100"
  ]

type EmployeeName = String :| EmployeeNameDescription

object EmployeeName extends RefinedTypeOps[String, EmployeeNameDescription, EmployeeName]
