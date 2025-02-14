package com.example.tables

import com.augustnagro.magnum.magzio.*
import com.example.domain.{ EmployeeId, PhoneId }
import com.example.util.given
import io.github.iltotore.iron.*

@Table(PostgresDbType, SqlNameMapper.CamelToSnakeCase)
final case class EmployeePhone(
  employeeId: Int :| EmployeeId,
  phoneId: Int :| PhoneId
) derives DbCodec

object EmployeePhone:
  val table = TableInfo[EmployeePhone, EmployeePhone, Null]
