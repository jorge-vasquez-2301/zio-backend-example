package com.example.api.endpoint

import com.example.dto.*
import com.example.error.AppError.{ PhoneAlreadyExists, PhoneNotFound }
import zio.http.*
import zio.http.codec.*
import zio.http.endpoint.Endpoint

trait PhoneEndpoints:
  val createPhone =
    Endpoint(Method.POST / "phone")
      .in[PhoneDTO](Doc.p("Phone to be created"))
      .out[Int](Doc.p("ID of the created phone"))
      .outError[PhoneAlreadyExists](Status.NotFound, Doc.p("The phone number already exists"))
      ?? Doc.p("Create a new phone")

  val getPhoneById =
    Endpoint(Method.GET / "phone" / int("id"))
      .out[PhoneDTO](Doc.p("Phone"))
      .outError[PhoneNotFound](Status.NotFound, Doc.p("The phone was not found"))
      ?? Doc.p("Obtain the phone with the given `id`")

  val updatePhone =
    Endpoint(Method.PUT / "phone" / int("id"))
      .in[PhoneDTO](Doc.p("Phone to be updated"))
      .out[Unit]
      .outError[PhoneNotFound](Status.NotFound, Doc.p("The phone was not found"))
      ?? Doc.p("Update the phone with the given `id`")

  val deletePhone =
    Endpoint(Method.DELETE / "phone" / int("id")).out[Unit] ?? Doc.p("Delete the phone with the given `id`")
