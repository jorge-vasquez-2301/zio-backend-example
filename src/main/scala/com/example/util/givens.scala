package com.example.util

import com.augustnagro.magnum.DbCodec
import io.github.iltotore.iron.*
import zio.schema.*

inline given ironSchema[T, Description](using Schema[T], Constraint[T, Description]): Schema[T :| Description] =
  Schema[T].transformOrFail(_.refineEither[Description], Right(_))

inline given ironDbCodec[T, Description](using DbCodec[T], Constraint[T, Description]): DbCodec[T :| Description] =
  DbCodec[T].biMap(_.refineUnsafe[Description], identity)
