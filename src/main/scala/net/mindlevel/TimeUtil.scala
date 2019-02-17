package net.mindlevel

import java.sql.Timestamp
import java.time.Instant
import spray.json.DefaultJsonProtocol._

object TimeUtil {
  def now(): Long = Instant.now.getEpochSecond * 1000

  def timestamp(): Timestamp = new Timestamp(now())
}
