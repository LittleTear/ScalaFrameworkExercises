package org.littletear.zionomicon.projone
import org.littletear.zionomicon.projone.http.Http4s
import zio._
object MainServer extends ZIOAppDefault {




  //step1、读取CSV


  //step2、创建httpClient


  //step3、调用高德接口


  //step4、结果写入CSV

  override def run: ZIO[ Scope, Any, Any] = {
    def program(getGaoDe:GetResultForGaoDe) =
      for{
        listZio <- getGaoDe.resultStream
        _       <- ZIO.succeed(listZio.map{ str => str.flatMap(Console.printLine(_))})
      } yield ()

    ZLayer
      .make[GetResultForGaoDe](
        GetResultForGaoDe.live,
        Http4s.make,
        ParseCsv.live,
        ConfigParser.make
      )
      .build
      .map(_.get[GetResultForGaoDe])
      .flatMap(program)


  }
}
