package com.example.domain

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

type PhoneIdDescription = DescribedAs[Greater[0], "Phone's ID should be strictly positive"]

type PhoneId = Int :| PhoneIdDescription

object PhoneId extends RefinedType[Int, PhoneIdDescription]
