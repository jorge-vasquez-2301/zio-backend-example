package com.example.api.handler

import com.example.domain.Phone
import com.example.error.AppError.{ PhoneAlreadyExists, PhoneNotFound }
import com.example.service.PhoneService
import zio.*

trait PhoneHandlers:
  def createPhoneHandler(phone: Phone): ZIO[PhoneService, PhoneAlreadyExists, Int] =
    ZIO.serviceWithZIO[PhoneService](_.create(phone))

  def getPhoneHandler(id: Int): ZIO[PhoneService, PhoneNotFound, Phone] =
    ZIO.serviceWithZIO[PhoneService](_.retrieveById(id))

  def updatePhoneHandler(
    phoneId: Int,
    phone: Phone
  ): ZIO[PhoneService, PhoneNotFound, Unit] =
    ZIO.serviceWithZIO[PhoneService](_.update(phoneId, phone))

  def deletePhoneHandler(id: Int): URIO[PhoneService, Unit] =
    ZIO.serviceWithZIO[PhoneService](_.delete(id))
