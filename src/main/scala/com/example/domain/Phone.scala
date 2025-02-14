package com.example.domain

import com.example.util.given
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import zio.schema.*

type PhoneNumber =
  DescribedAs[
    ForAll[Digit] & MinLength[6] & MaxLength[15],
    "Phone number should have a length between 6 and 15"
  ]

type PhoneId = DescribedAs[Greater[0], "Phone's ID should be strictly positive"]

final case class Phone(number: String :| PhoneNumber) derives Schema
