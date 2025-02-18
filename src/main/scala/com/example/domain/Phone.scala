package com.example.domain

import com.example.util.given
import zio.schema.*

final case class Phone(number: PhoneNumber) derives Schema
