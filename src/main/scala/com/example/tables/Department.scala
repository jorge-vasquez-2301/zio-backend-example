package com.example.tables

import com.augustnagro.magnum.magzio.*
import com.example.domain
import com.example.domain.{ DepartmentId, DepartmentName }
import com.example.util.given
import io.github.iltotore.iron.*

@Table(PostgresDbType, SqlNameMapper.CamelToSnakeCase)
final case class Department(
  @Id id: DepartmentId,
  name: DepartmentName
) derives DbCodec:
  val toDomain = domain.Department(name)

object Department:
  val table = TableInfo[domain.Department, Department, DepartmentId]

  def fromDomain(departmentId: DepartmentId, department: domain.Department): Department =
    Department(departmentId, department.name)
