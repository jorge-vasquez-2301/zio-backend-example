package com.example.domain

import com.example.util.given
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import zio.schema.*

type DepartmentName =
  DescribedAs[
    Alphanumeric & Not[Empty] & MaxLength[50],
    "Department's name should be alphanumeric, non-empty and have a maximum length of 50"
  ]

type DepartmentId = DescribedAs[Greater[0], "Department's ID should be strictly positive"]

final case class Department(name: String :| DepartmentName) derives Schema
