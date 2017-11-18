package net.mindlevel

import java.io.File

import awscala.s3.Bucket
import awscala.Region
import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.nio.JpegWriter
import jp.co.bizreach.s3scala.S3
import com.typesafe.config.ConfigFactory

object S3Util {
  val conf = ConfigFactory.load()
  implicit val region = Region.Frankfurt
  implicit val s3 =
    S3(accessKeyId = conf.getString("s3.accessKeyId"), secretAccessKey = conf.getString("s3.secretAccessKey"))
  val bucket: Bucket = s3.bucket("mindlevel").get // TODO: Handle AWS errors

  def put(file: File): String = {
    val name: String = s"${java.util.UUID.randomUUID.toString}"
    val postfix = ".jpg"
    val thumbnail = scaleImage(file, 256)
    val shrinked = scaleImage(file, 1024)
    bucket.put(s"${name}_original$postfix", file)
    bucket.put(s"${name}_thumb$postfix", thumbnail)
    bucket.put(s"$name$postfix", shrinked)
    s"$name$postfix"
  }

  private def scaleImage(file: File, max: Int): File = {
    implicit val writer: JpegWriter = JpegWriter().withCompression(50)
    val in = Image.fromFile(file)
    val out = File.createTempFile("upload", "tmp")
    in.max(max, max).output(out)
  }
}