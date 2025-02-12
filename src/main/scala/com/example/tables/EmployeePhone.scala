package com.example.tables

import com.augustnagro.magnum.magzio.*

@Table(PostgresDbType, SqlNameMapper.CamelToSnakeCase)
final case class EmployeePhone(
  employeeId: Int,
  phoneId: Int
) derives DbCodec

object EmployeePhone:
  val table = TableInfo[EmployeePhone, EmployeePhone, Null]
