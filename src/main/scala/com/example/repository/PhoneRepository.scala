package com.example.repository

import com.augustnagro.magnum.magzio.*
import com.example.domain.Phone
import com.example.tables
import zio.*

trait PhoneRepository:
  def create(phone: Phone): UIO[Int]
  def retrieve(phoneId: Int): UIO[Option[Phone]]
  def retrieveByNumber(phoneNumber: String): UIO[Option[Phone]]
  def update(phoneId: Int, phone: Phone): UIO[Unit]
  def delete(phoneId: Int): UIO[Unit]

final case class PhoneRepositoryLive(xa: Transactor) extends Repo[Phone, tables.Phone, Int] with PhoneRepository:

  override def create(phone: Phone): UIO[Int] =
    xa.transact {
      insertReturning(phone).id
    }.orDie

  override def retrieve(phoneId: Int): UIO[Option[Phone]] =
    xa.transact {
      findById(phoneId).map(_.toDomain)
    }.orDie

  override def retrieveByNumber(phoneNumber: String): UIO[Option[Phone]] =
    xa.transact {
      val spec = Spec[tables.Phone].where(sql"${tables.Phone.table.number} = $phoneNumber")

      findAll(spec).headOption.map(_.toDomain)
    }.orDie

  override def update(phoneId: Int, phone: Phone): UIO[Unit] =
    xa.transact {
      update(tables.Phone.fromDomain(phoneId, phone))
    }.orDie

  override def delete(phoneId: Int): UIO[Unit] =
    xa.transact {
      deleteById(phoneId)
    }.orDie

object PhoneRepositoryLive:
  val layer: URLayer[Transactor, PhoneRepository] = ZLayer.fromFunction(PhoneRepositoryLive(_))
