package com.example.tables

import com.augustnagro.magnum.magzio.*
import com.example.domain
import com.example.domain.{ Age, DepartmentId, EmployeeId, EmployeeName }
import com.example.util.given
import io.github.iltotore.iron.*

@Table(PostgresDbType, SqlNameMapper.CamelToSnakeCase)
final case class Employee(
  @Id id: Int :| EmployeeId,
  name: String :| EmployeeName,
  age: Int :| Age,
  departmentId: Int :| DepartmentId
) derives DbCodec:
  val toDomain = domain.Employee(name, age, departmentId)

object Employee:
  val table = TableInfo[domain.Employee, Employee, Int :| EmployeeId]

  def fromDomain(employeeId: Int :| EmployeeId, employee: domain.Employee): Employee =
    Employee(employeeId, employee.name, employee.age, employee.departmentId)
