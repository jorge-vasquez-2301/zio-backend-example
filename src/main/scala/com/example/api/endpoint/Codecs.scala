package com.example.api.endpoint

import io.github.iltotore.iron.*
import zio.http.*
import zio.http.codec.*

trait Codecs:
  inline def idCodec[Description](using Constraint[Int, Description]): PathCodec[Int :| Description] =
    int("id").transformOrFail[Int :| Description](_.refineEither[Description])(Right(_))
