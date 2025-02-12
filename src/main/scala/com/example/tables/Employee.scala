package com.example.tables

import com.augustnagro.magnum.magzio.*
import com.example.domain

@Table(PostgresDbType, SqlNameMapper.CamelToSnakeCase)
final case class Employee(
  @Id id: Int,
  name: String,
  age: Int,
  departmentId: Int
) derives DbCodec:
  val toDomain = domain.Employee(name, age, departmentId)

object Employee:
  val table = TableInfo[domain.Employee, Employee, Int]

  def fromDomain(employeeId: Int, employee: domain.Employee): Employee =
    Employee(employeeId, employee.name, employee.age, employee.departmentId)
