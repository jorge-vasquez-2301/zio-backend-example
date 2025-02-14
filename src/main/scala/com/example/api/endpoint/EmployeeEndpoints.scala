package com.example.api.endpoint

import com.example.domain.*
import com.example.error.AppError.{ DepartmentNotFound, EmployeeNotFound }
import com.example.util.given
import io.github.iltotore.iron.*
import zio.http.*
import zio.http.codec.*
import zio.http.endpoint.Endpoint

trait EmployeeEndpoints extends Codecs:
  val createEmployee =
    Endpoint(Method.POST / "employee")
      .in[Employee](Doc.p("Employee to be created"))
      .out[Int :| EmployeeId](Doc.p("ID of the created employee"))
      .outError[DepartmentNotFound](Status.NotFound, Doc.p("The employee's department was not found"))
      ?? Doc.p("Create a new employee")

  val getEmployees =
    Endpoint(Method.GET / "employee")
      .out[Vector[Employee]](Doc.p("List of employees")) ?? Doc.p("Obtain a list of employees")

  val getEmployeeById =
    Endpoint(Method.GET / "employee" / idCodec[EmployeeId])
      .out[Employee](Doc.p("Employee"))
      .outError[EmployeeNotFound](Status.NotFound, Doc.p("The employee was not found"))
      ?? Doc.p("Obtain the employee with the given `id`")

  val updateEmployee =
    Endpoint(Method.PUT / "employee" / idCodec[EmployeeId])
      .in[Employee](Doc.p("Employee to be updated"))
      .out[Unit]
      .outError[EmployeeNotFound](Status.NotFound, Doc.p("The employee was not found"))
      ?? Doc.p("Update the employee with the given `id`")

  val deleteEmployee =
    Endpoint(Method.DELETE / "employee" / idCodec[EmployeeId]).out[Unit] ?? Doc.p(
      "Delete the employee with the given `id`"
    )
