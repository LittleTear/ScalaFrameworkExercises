package org.littletear.zionomicon.projone.http

import io.circe.Decoder
import org.http4s._
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.implicits._
import zio._
import zio.interop.catz._
import org.http4s.ember.client._

case class Http4s(client:Client[Task]) extends HttpClient with  Http4sClientDsl[Task]{
  val rootUri = uri"https://restapi.amap.com"

  override def get[T](resources: String, parameters: Map[String, String])(implicit d: Decoder[T]): Task[T] = {
    val withPath = rootUri.withPath(Uri.Path.unsafeFromString(resources))
    val getWithQuery = withPath.withQueryParams(parameters)
    client
      .expect[T](getWithQuery)
      .foldZIO(
        failure => ZIO.log(s"Could not fetch data from $getWithQuery") *> ZIO.fail(failure),
        success => ZIO.succeed(success)
      )
  }
}

object Http4s {

  val clientScoped = EmberClientBuilder.default[Task].build.toScopedZIO

  lazy val live:ZLayer[Client[Task],Nothing,Http4s] = ZLayer.fromFunction(Http4s(_))

  lazy val make = ZLayer.scoped(clientScoped) >>> live

  def get[T](resources: String, parameters: Map[String, String])(implicit d: Decoder[T]): ZIO[Http4s, Throwable, T] =
    ZIO.serviceWithZIO[Http4s](_.get[T](resources,parameters))



}
