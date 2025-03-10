package com.example.api.endpoint

import io.github.iltotore.iron.*
import zio.http.*
import zio.http.codec.*

trait Codecs:
  inline def idCodec[Description](
    name: String = "id"
  )(using Constraint[Int, Description]): PathCodec[Int :| Description] =
    int(name).transformOrFail[Int :| Description](_.refineEither[Description])(Right(_))
