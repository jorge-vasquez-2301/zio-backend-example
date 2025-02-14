package com.example.domain

import com.example.util.given
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import zio.schema.*

type DepartmentNameDescription =
  DescribedAs[
    Alphanumeric & Not[Empty] & MaxLength[50],
    "Department's name should be alphanumeric, non-empty and have a maximum length of 50"
  ]

type DepartmentName = String :| DepartmentNameDescription

type DepartmentIdDescription = DescribedAs[Greater[0], "Department's ID should be strictly positive"]

type DepartmentId = Int :| DepartmentIdDescription

final case class Department(name: DepartmentName) derives Schema
