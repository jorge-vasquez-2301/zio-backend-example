package com.example.tables

import com.augustnagro.magnum.magzio.*
import com.example.domain

@Table(PostgresDbType, SqlNameMapper.CamelToSnakeCase)
final case class Department(
  @Id id: Int,
  name: String
) derives DbCodec:
  val toDomain = domain.Department(name)

object Department:
  val table = TableInfo[domain.Department, Department, Int]

  def fromDomain(departmentId: Int, department: domain.Department): Department =
    Department(departmentId, department.name)
