package com.example.api.handler

import com.example.dto.PhoneDTO
import com.example.error.AppError.{ PhoneAlreadyExists, PhoneNotFound }
import com.example.service.PhoneService
import zio.*

trait PhoneHandlers:
  def createPhoneHandler(phone: PhoneDTO): ZIO[PhoneService, PhoneAlreadyExists, Int] =
    ZIO.serviceWithZIO[PhoneService](_.create(phone.toDomain))

  def getPhoneHandler(id: Int): ZIO[PhoneService, PhoneNotFound, PhoneDTO] =
    ZIO.serviceWithZIO[PhoneService](_.retrieveById(id).map(PhoneDTO.fromDomain))

  def updatePhoneHandler(
    phoneId: Int,
    phone: PhoneDTO
  ): ZIO[PhoneService, PhoneNotFound, Unit] =
    ZIO.serviceWithZIO[PhoneService](_.update(phoneId, phone.toDomain))

  def deletePhoneHandler(id: Int): URIO[PhoneService, Unit] =
    ZIO.serviceWithZIO[PhoneService](_.delete(id))
