package com.example.domain

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

type DepartmentIdDescription = DescribedAs[Greater[0], "Department's ID should be strictly positive"]

type DepartmentId = Int :| DepartmentIdDescription

object DepartmentId extends RefinedType[Int, DepartmentIdDescription]
