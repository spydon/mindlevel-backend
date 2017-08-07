package net.mindlevel

import java.io.File

import awscala.s3.Bucket
import awscala.Region
import jp.co.bizreach.s3scala.S3
import com.typesafe.config.ConfigFactory

object S3Util {
  val conf = ConfigFactory.load()
  implicit val region = Region.Frankfurt
  implicit val s3 =
    S3(accessKeyId = conf.getString("s3.accessKeyId"), secretAccessKey = conf.getString("s3.secretAccessKey"))
  val bucket: Bucket = s3.bucket("mindlevel").get // TODO: Handle AWS errors

  def put(file: File): String = {
    val name: String = s"${java.util.UUID.randomUUID.toString}.jpg"
    bucket.put(name, file)
    name
  }
}