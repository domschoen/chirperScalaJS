/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package sample.chirper.chirp.api

import scala.collection.immutable.Seq

case class LiveChirpsRequest(userIds: Seq[String])
