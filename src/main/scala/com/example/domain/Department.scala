package com.example.domain

import zio.schema.*

final case class Department(name: String) derives Schema
