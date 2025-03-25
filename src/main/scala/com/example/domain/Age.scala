package com.example.domain

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

type AgeDescription = DescribedAs[Greater[0], "Employee's age should be strictly positive"]

type Age = Int :| AgeDescription

object Age extends RefinedType[Int, AgeDescription]
