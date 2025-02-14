package com.example.repository

import com.augustnagro.magnum.magzio.*
import com.example.domain.{ Phone, PhoneId }
import com.example.tables
import com.example.util.given
import io.github.iltotore.iron.*
import zio.*

trait PhoneRepository:
  def create(phone: Phone): UIO[Int :| PhoneId]
  def retrieve(phoneId: Int :| PhoneId): UIO[Option[Phone]]
  def retrieveByNumber(phoneNumber: String): UIO[Option[Phone]]
  def update(phoneId: Int :| PhoneId, phone: Phone): UIO[Unit]
  def delete(phoneId: Int :| PhoneId): UIO[Unit]

final case class PhoneRepositoryLive(xa: Transactor)
    extends Repo[Phone, tables.Phone, Int :| PhoneId]
    with PhoneRepository:

  override def create(phone: Phone): UIO[Int :| PhoneId] =
    xa.transact {
      insertReturning(phone).id
    }.orDie

  override def retrieve(phoneId: Int :| PhoneId): UIO[Option[Phone]] =
    xa.transact {
      findById(phoneId).map(_.toDomain)
    }.orDie

  override def retrieveByNumber(phoneNumber: String): UIO[Option[Phone]] =
    xa.transact {
      val spec = Spec[tables.Phone].where(sql"${tables.Phone.table.number} = $phoneNumber")

      findAll(spec).headOption.map(_.toDomain)
    }.orDie

  override def update(phoneId: Int :| PhoneId, phone: Phone): UIO[Unit] =
    xa.transact {
      update(tables.Phone.fromDomain(phoneId, phone))
    }.orDie

  override def delete(phoneId: Int :| PhoneId): UIO[Unit] =
    xa.transact {
      deleteById(phoneId)
    }.orDie

object PhoneRepositoryLive:
  val layer: URLayer[Transactor, PhoneRepository] = ZLayer.fromFunction(PhoneRepositoryLive(_))
