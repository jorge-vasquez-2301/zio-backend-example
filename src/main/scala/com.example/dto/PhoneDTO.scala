package com.example.dto

import com.example.domain.Phone
import zio.schema.*

final case class PhoneDTO(number: String) derives Schema:
  val toDomain = Phone(number)

object PhoneDTO:
  def fromDomain(phone: Phone): PhoneDTO = PhoneDTO(phone.number)
