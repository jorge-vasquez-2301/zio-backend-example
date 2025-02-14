package com.example.tables

import com.augustnagro.magnum.magzio.*
import com.example.domain
import com.example.domain.{ PhoneId, PhoneNumber }
import com.example.util.given
import io.github.iltotore.iron.*

@Table(PostgresDbType, SqlNameMapper.CamelToSnakeCase)
final case class Phone(
  @Id id: PhoneId,
  number: PhoneNumber
) derives DbCodec:
  val toDomain = domain.Phone(number)

object Phone:
  val table = TableInfo[domain.Phone, Phone, PhoneId]

  def fromDomain(phoneId: PhoneId, phone: domain.Phone): Phone = Phone(phoneId, phone.number)
