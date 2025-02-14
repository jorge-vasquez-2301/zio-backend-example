package com.example.api.endpoint

import io.github.iltotore.iron.*
import zio.http.*
import zio.http.codec.*

trait Codecs:
  inline def idCodec[T](using Constraint[Int, T]): PathCodec[Int :| T] =
    int("id").transformOrFail[IronType[Int, T]](_.refineEither[T])(Right(_))
