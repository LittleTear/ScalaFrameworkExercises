package org.littletear.zionomicon.projone


//import akka.actor.typed.ActorSystem
//import akka.actor.typed.scaladsl.Behaviors
//import akka.http.scaladsl.Http
//import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
//import akka.http.scaladsl.client.RequestBuilding.Get
//import akka.http.scaladsl.unmarshalling.Unmarshal
//import scala.util.{Failure, Success}
//import java.net.URLEncoder
//import scala.concurrent.Future


import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
//import org.http4s.client.Client
import org.http4s.implicits._
import org.http4s.ember.client._
import org.http4s.client.dsl.io._
import org.http4s.headers._
import org.http4s.MediaType
import cats.effect.unsafe.implicits.global


case class HttpClient() {


  def getHttpForGaoDe(address:String,key:String = "7b1035c7351a130bf3fa956558730929",city:String = "北京") = {
//    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
//    implicit val executionContext = system.executionContext
    val cityEncoded =
//      URLEncoder.encode(city,"UTF-8")
      Uri.encode(city)
    val addressEncoded =
//      URLEncoder.encode(address,"UTF-8")
      Uri.encode(address)
//    val url = s"https://restapi.amap.com/v3/geocode/geo?key=$key&city=$cityEncoded&address=$addressEncoded"
//    println(url)
//    val get: HttpRequest = Get(url)
//    val responseFuture: Future[HttpResponse] = Http().singleRequest(get)
//    responseFuture
//      .onComplete {
//        case Success(res) => println(Unmarshal(res.entity).to[String].onComplete{
//          case Success(res1) =>println(res1)
//          case _ => println("111")
//        })
//        case Failure(_)   => sys.error("something wrong")
//      }
    val root: Uri = uri"https://restapi.amap.com"
    val withPath = root.withPath(path"/v3/geocode/geo")
    val map = Map[String,String]("key" -> key,"city"-> city,"address"->address)
    val withQuery: Uri = withPath.withQueryParams(map)

//    val http4sUrl: Uri = Uri.unsafeFromString(s"https://restapi.amap.com/v3/geocode/geo?key=$key&city=$cityEncoded&address=$addressEncoded")
    val request: Request[IO] = GET(
      withQuery,
      Authorization(Credentials.Token(AuthScheme.Bearer, "open sesame")),
      Accept(MediaType.application.json)
    )

    val catResult: IO[String] = EmberClientBuilder
      .default[IO]
      .build
      .use{ client =>
       client.expect[String](withQuery)
    }
    catResult.unsafeRunAndForget()


  }




}

object HttpClient extends App {
  HttpClient().getHttpForGaoDe("朝阳区辛店路3号院")
}
