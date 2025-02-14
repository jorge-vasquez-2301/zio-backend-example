package com.example.util

import com.augustnagro.magnum.DbCodec
import io.github.iltotore.iron.*
import zio.schema.*

inline given ironSchema[A, B](using Schema[A], Constraint[A, B]): Schema[A :| B] =
  Schema[A].transformOrFail(_.refineEither[B], Right(_))

inline given ironDbCodec[A, B](using DbCodec[A], Constraint[A, B]): DbCodec[A :| B] =
  DbCodec[A].biMap(_.refineUnsafe[B], identity)
