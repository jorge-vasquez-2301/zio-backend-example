package com.example.repository

import com.augustnagro.magnum.magzio.*
import com.example.domain.Phone
import com.example.tables
import zio.*

trait EmployeePhoneRepository:
  def addPhoneToEmployee(phoneId: Int, employeeId: Int): UIO[Unit]
  def retrieveEmployeePhones(employeeId: Int): UIO[Vector[Phone]]
  def removePhoneFromEmployee(phoneId: Int, employeeId: Int): UIO[Unit]

final case class EmployeePhoneRepositoryLive(xa: Transactor)
    extends Repo[tables.EmployeePhone, tables.EmployeePhone, Null]
    with EmployeePhoneRepository:

  override def addPhoneToEmployee(phoneId: Int, employeeId: Int): UIO[Unit] =
    xa.transact {
      insert(tables.EmployeePhone(employeeId, phoneId))
    }.orDie

  override def retrieveEmployeePhones(employeeId: Int): UIO[Vector[Phone]] =
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

  override def removePhoneFromEmployee(phoneId: Int, employeeId: Int): UIO[Unit] =
    xa.transact {
      delete(tables.EmployeePhone(employeeId, phoneId))
    }.orDie

object EmployeePhoneRepositoryLive:
  val layer: URLayer[Transactor, EmployeePhoneRepository] = ZLayer.fromFunction(EmployeePhoneRepositoryLive(_))
