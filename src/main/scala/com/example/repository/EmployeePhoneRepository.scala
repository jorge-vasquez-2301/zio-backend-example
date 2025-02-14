package com.example.repository

import com.augustnagro.magnum.magzio.*
import com.example.domain.{ EmployeeId, Phone, PhoneId }
import com.example.tables
import com.example.util.given
import io.github.iltotore.iron.*
import zio.*

trait EmployeePhoneRepository:
  def addPhoneToEmployee(phoneId: PhoneId, employeeId: EmployeeId): UIO[Unit]
  def retrieveEmployeePhones(employeeId: EmployeeId): UIO[Vector[Phone]]
  def removePhoneFromEmployee(phoneId: PhoneId, employeeId: EmployeeId): UIO[Unit]

final case class EmployeePhoneRepositoryLive(xa: Transactor)
    extends Repo[tables.EmployeePhone, tables.EmployeePhone, Null]
    with EmployeePhoneRepository:

  override def addPhoneToEmployee(phoneId: PhoneId, employeeId: EmployeeId): UIO[Unit] =
    xa.transact {
      insert(tables.EmployeePhone(employeeId, phoneId))
    }.orDie

  override def retrieveEmployeePhones(employeeId: EmployeeId): UIO[Vector[Phone]] =
    xa.transact {
      val statement =
        sql"""
          SELECT ${tables.Phone.table.all}
          FROM ${tables.Phone.table}
          INNER JOIN ${tables.EmployeePhone.table} ON ${tables.EmployeePhone.table.phoneId} = ${tables.Phone.table.id}
          WHERE ${tables.EmployeePhone.table.employeeId} = $employeeId
        """

      statement.query[tables.Phone].run().map(_.toDomain)
    }.orDie

  override def removePhoneFromEmployee(phoneId: PhoneId, employeeId: EmployeeId): UIO[Unit] =
    xa.transact {
      delete(tables.EmployeePhone(employeeId, phoneId))
    }.orDie

object EmployeePhoneRepositoryLive:
  val layer: URLayer[Transactor, EmployeePhoneRepository] = ZLayer.fromFunction(EmployeePhoneRepositoryLive(_))
