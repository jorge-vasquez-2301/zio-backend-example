package com.example.api.handler

import com.example.domain.{ Phone, PhoneId }
import com.example.error.AppError.{ PhoneAlreadyExists, PhoneNotFound }
import com.example.service.PhoneService
import io.github.iltotore.iron.*
import zio.*

trait PhoneHandlers:
  def createPhoneHandler(phone: Phone): ZIO[PhoneService, PhoneAlreadyExists, Int :| PhoneId] =
    ZIO.serviceWithZIO[PhoneService](_.create(phone))

  def getPhoneHandler(id: Int :| PhoneId): ZIO[PhoneService, PhoneNotFound, Phone] =
    ZIO.serviceWithZIO[PhoneService](_.retrieveById(id))

  def updatePhoneHandler(
    phoneId: Int :| PhoneId,
    phone: Phone
  ): ZIO[PhoneService, PhoneNotFound, Unit] =
    ZIO.serviceWithZIO[PhoneService](_.update(phoneId, phone))

  def deletePhoneHandler(id: Int :| PhoneId): URIO[PhoneService, Unit] =
    ZIO.serviceWithZIO[PhoneService](_.delete(id))
