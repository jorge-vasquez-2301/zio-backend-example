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
  def fromDomain(departmentId: Int, department: domain.Department): Department =
    Department(departmentId, department.name)

  final case class Creator(name: String) derives DbCodec
  object Creator:
    def fromDomain(department: domain.Department): Creator = Creator(department.name)
