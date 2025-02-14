package com.example.tables

import com.augustnagro.magnum.magzio.*
import com.example.domain
import com.example.domain.{ DepartmentId, DepartmentName }
import com.example.util.given
import io.github.iltotore.iron.*

@Table(PostgresDbType, SqlNameMapper.CamelToSnakeCase)
final case class Department(
  @Id id: Int :| DepartmentId,
  name: String :| DepartmentName
) derives DbCodec:
  val toDomain = domain.Department(name)

object Department:
  val table = TableInfo[domain.Department, Department, Int :| DepartmentId]

  def fromDomain(departmentId: Int :| DepartmentId, department: domain.Department): Department =
    Department(departmentId, department.name)
