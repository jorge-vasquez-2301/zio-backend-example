package com.example.api.endpoint

import com.example.domain.*
import com.example.error.AppError
import com.example.error.AppError.EmployeeNotFound
import zio.http.*
import zio.http.codec.*
import zio.http.endpoint.Endpoint

trait EmployeePhoneEndpoints extends Codecs:
  val addPhoneToEmployee =
    Endpoint(Method.POST / "employee" / idCodec[EmployeeId] / "phone" / idCodec[PhoneId])
      .out[Unit]
      .outError[AppError](Status.NotFound, Doc.p("The employee/phone was not found"))
      ?? Doc.p("Add a phone to an employee")

  val retrieveEmployeePhones =
    Endpoint(Method.GET / "employee" / idCodec[EmployeeId] / "phone")
      .out[Vector[Phone]](Doc.p("List of employee's phones"))
      .outError[EmployeeNotFound](Status.NotFound, Doc.p("The employee was not found"))
      ?? Doc.p("Obtain a list of a employee's phones")

  val removePhoneFromEmployee =
    Endpoint(Method.DELETE / "employee" / idCodec[EmployeeId] / "phone" / idCodec[PhoneId])
      .out[Unit]
      .outError[AppError](Status.NotFound, Doc.p("The employee/phone was not found"))
      ?? Doc.p("Remove a phone from an employee")
