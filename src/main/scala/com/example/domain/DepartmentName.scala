package com.example.domain

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

type DepartmentNameDescription =
  DescribedAs[
    Alphanumeric & Not[Empty] & MaxLength[50],
    "Department's name should be alphanumeric, non-empty and have a maximum length of 50"
  ]

type DepartmentName = String :| DepartmentNameDescription

object DepartmentName extends RefinedTypeOps[String, DepartmentNameDescription, DepartmentName]
