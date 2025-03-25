package com.example.domain

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*

type PhoneNumberDescription =
  DescribedAs[
    ForAll[Digit] & MinLength[6] & MaxLength[15],
    "Phone number should have a length between 6 and 15"
  ]

type PhoneNumber = String :| PhoneNumberDescription

object PhoneNumber extends RefinedType[String, PhoneNumberDescription]
