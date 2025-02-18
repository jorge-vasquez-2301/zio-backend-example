package com.example.domain

import com.example.util.given
import zio.schema.*

final case class Department(name: DepartmentName) derives Schema
