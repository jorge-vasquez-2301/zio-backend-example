package com.example.domain

import zio.schema.*

final case class Phone(number: String) derives Schema
