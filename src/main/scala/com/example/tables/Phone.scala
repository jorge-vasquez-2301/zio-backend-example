package com.example.tables

import com.augustnagro.magnum.magzio.*
import com.example.domain

@Table(PostgresDbType, SqlNameMapper.CamelToSnakeCase)
final case class Phone(
  @Id id: Int,
  number: String
) derives DbCodec:
  val toDomain = domain.Phone(number)

object Phone:
  val table = TableInfo[domain.Phone, Phone, Int]

  def fromDomain(phoneId: Int, phone: domain.Phone): Phone = Phone(phoneId, phone.number)
