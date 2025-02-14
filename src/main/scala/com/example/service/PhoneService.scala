package com.example.service

import com.example.domain.{ Phone, PhoneId }
import com.example.error.AppError.*
import com.example.repository.PhoneRepository
import io.github.iltotore.iron.*
import zio.*

trait PhoneService:
  def create(phone: Phone): IO[PhoneAlreadyExists, Int :| PhoneId]
  def retrieveById(phoneId: Int :| PhoneId): IO[PhoneNotFound, Phone]
  def update(phoneId: Int :| PhoneId, phone: Phone): IO[PhoneNotFound, Unit]
  def delete(phoneId: Int :| PhoneId): UIO[Unit]

final case class PhoneServiceLive(phoneRepository: PhoneRepository) extends PhoneService:
  override def create(phone: Phone): IO[PhoneAlreadyExists, Int :| PhoneId] =
    for
      maybePhone <- phoneRepository.retrieveByNumber(phone.number)
      phoneId    <- maybePhone match
                      case Some(_) => ZIO.fail(PhoneAlreadyExists)
                      case None    => phoneRepository.create(phone)
    yield phoneId

  override def retrieveById(phoneId: Int :| PhoneId): IO[PhoneNotFound, Phone] =
    phoneRepository.retrieve(phoneId).someOrFail(PhoneNotFound)

  override def update(phoneId: Int :| PhoneId, phone: Phone): IO[PhoneNotFound, Unit] =
    phoneRepository.retrieve(phoneId).someOrFail(PhoneNotFound)
      *> phoneRepository.update(phoneId, phone)

  override def delete(phoneId: Int :| PhoneId): UIO[Unit] = phoneRepository.delete(phoneId)

object PhoneServiceLive:
  val layer: URLayer[PhoneRepository, PhoneService] =
    ZLayer.fromFunction(PhoneServiceLive(_))
