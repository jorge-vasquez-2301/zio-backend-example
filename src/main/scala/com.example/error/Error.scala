package com.example.error

import zio.schema.*

sealed trait AppError derives Schema
object AppError:
  case object DepartmentNotFound extends AppError derives Schema
  type DepartmentNotFound = DepartmentNotFound.type

  case object DepartmentAlreadyExists extends AppError derives Schema
  type DepartmentAlreadyExists = DepartmentAlreadyExists.type

  case object EmployeeNotFound extends AppError derives Schema
  type EmployeeNotFound = EmployeeNotFound.type

  case object PhoneNotFound extends AppError derives Schema
  type PhoneNotFound = PhoneNotFound.type

  case object PhoneAlreadyExists extends AppError derives Schema
  type PhoneAlreadyExists = PhoneAlreadyExists.type
