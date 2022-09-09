package org.littletear.zionomicon.projone

import org.littletear.zionomicon.projone.http.Http4s
import zio._
case class GetResultForGaoDe(csvContent: ParseCsv, httpClient: Http4s) {

  def resultStream: ZIO[Any, Throwable, List[Task[String]]] = csvContent.csvReader.map { contentListMap =>
    val city = "北京"
    val resource = "/v3/geocode/geo"
    val getResponse = contentListMap.map { map =>
      val address = map.getOrElse("居住地址", "北京市")
      val parameters = Map("address" -> address, "city" -> city)
    httpClient.get[String](resource, parameters)
    }
    getResponse
  }




}

object GetResultForGaoDe {

  lazy val live: ZLayer[ParseCsv with Http4s, Nothing, GetResultForGaoDe] = ZLayer.fromFunction(GetResultForGaoDe(_,_))
}
