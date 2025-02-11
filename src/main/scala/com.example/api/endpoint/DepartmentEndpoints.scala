package com.example.api.endpoint

import com.example.dto.*
import com.example.error.AppError.{ DepartmentAlreadyExists, DepartmentNotFound }
import zio.http.*
import zio.http.codec.*
import zio.http.endpoint.Endpoint

trait DepartmentEndpoints:
  val createDepartment =
    Endpoint(Method.POST / "department")
      .in[DepartmentDTO](Doc.p("Department to be created"))
      .out[Int](Doc.p("ID of the created department"))
      .outError[DepartmentAlreadyExists](Status.Conflict, Doc.p("The department already exists"))
      ?? Doc.p("Create a new department")

  val getDepartments =
    Endpoint(Method.GET / "department")
      .out[Vector[DepartmentDTO]](Doc.p("List of departments")) ?? Doc.p("Obtain a list of all departments")

  val getDepartmentById =
    Endpoint(Method.GET / "department" / int("id"))
      .out[DepartmentDTO](Doc.p("Department"))
      .outError[DepartmentNotFound](Status.NotFound, Doc.p("The department was not found"))
      ?? Doc.p("Obtain the department with the given `id`")

  val updateDepartment =
    Endpoint(Method.PUT / "department" / int("id"))
      .in[DepartmentDTO](Doc.p("Department to be updated"))
      .out[Unit]
      .outError[DepartmentNotFound](Status.NotFound, Doc.p("The department was not found"))
      ?? Doc.p("Update the department with the given `id`")

  val deleteDepartment =
    Endpoint(Method.DELETE / "department" / int("id")).out[Unit] ?? Doc.p("Delete the department with the given `id`")
