package org.littletear.zionomicon.projone.http
import io.circe.Decoder
import zio._

trait HttpClient {

  def get[T](resources: String, parameters: Map[String,String])(implicit d:Decoder[T]): Task[T]
}
