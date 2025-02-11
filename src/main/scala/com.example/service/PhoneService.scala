package com.example.service

import com.example.domain.Phone
import com.example.error.AppError.*
import com.example.repository.PhoneRepository
import zio.*

trait PhoneService:
  def create(phone: Phone): IO[PhoneAlreadyExists, Int]
  def retrieveById(phoneId: Int): IO[PhoneNotFound, Phone]
  def update(phoneId: Int, phone: Phone): IO[PhoneNotFound, Unit]
  def delete(phoneId: Int): UIO[Unit]

final case class PhoneServiceLive(phoneRepository: PhoneRepository) extends PhoneService:
  override def create(phone: Phone): IO[PhoneAlreadyExists, Int] =
    for
      maybePhone <- phoneRepository.retrieveByNumber(phone.number)
      phoneId    <- maybePhone match
                      case Some(_) => ZIO.fail(PhoneAlreadyExists)
                      case None    => phoneRepository.create(phone)
    yield phoneId

  override def retrieveById(phoneId: Int): IO[PhoneNotFound, Phone] =
    phoneRepository.retrieve(phoneId).someOrFail(PhoneNotFound)

  override def update(phoneId: Int, phone: Phone): IO[PhoneNotFound, Unit] =
    phoneRepository.retrieve(phoneId).someOrFail(PhoneNotFound)
      *> phoneRepository.update(phoneId, phone)

  override def delete(phoneId: Int): UIO[Unit] = phoneRepository.delete(phoneId)

object PhoneServiceLive:
  val layer: URLayer[PhoneRepository, PhoneService] =
    ZLayer.fromFunction(PhoneServiceLive(_))
