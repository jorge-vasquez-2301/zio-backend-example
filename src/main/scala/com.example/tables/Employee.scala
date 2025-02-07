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
  def fromDomain(employeeId: Int, employee: domain.Employee): Employee =
    Employee(employeeId, employee.name, employee.age, employee.departmentId)

  final case class Creator(name: String, age: Int, departmentId: Int) derives DbCodec
  object Creator:
    def fromDomain(employee: domain.Employee): Creator = Creator(employee.name, employee.age, employee.departmentId)
