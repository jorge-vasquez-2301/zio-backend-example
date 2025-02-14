package com.example.domain

import com.example.util.given
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.all.*
import zio.schema.*

type PhoneNumberDescription =
  DescribedAs[
    ForAll[Digit] & MinLength[6] & MaxLength[15],
    "Phone number should have a length between 6 and 15"
  ]

type PhoneNumber = String :| PhoneNumberDescription

type PhoneIdDescription = DescribedAs[Greater[0], "Phone's ID should be strictly positive"]

type PhoneId = Int :| PhoneIdDescription

final case class Phone(number: PhoneNumber) derives Schema
