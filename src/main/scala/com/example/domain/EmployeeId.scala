package com.example.domain

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

type EmployeeIdDescription = DescribedAs[Greater[0], "Employee's ID should be strictly positive"]

type EmployeeId = Int :| EmployeeIdDescription

object EmployeeId extends RefinedType[Int, EmployeeIdDescription]
