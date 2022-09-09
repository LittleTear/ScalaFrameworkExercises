package org.littletear.zionomicon.projone

import com.github.tototoshi.csv.CSVReader
import zio._


case class  ParseCsv(configParser: ConfigParser) extends ParseDsvDefault {


  private def readForCsv(filePath:String): ZIO[Any, Nothing, List[Map[String, String]]] = {
   ZIO.succeed(CSVReader.open(filePath).allWithHeaders())
  }


  override def csvReader: ZIO[Any, Throwable, List[Map[String, String]]] =
    for {
      fileConfig <- configParser.getFileConfig
      list       <- readForCsv(fileConfig.location+fileConfig.filename)
    } yield list
}



object ParseCsv {

 lazy val live: ZLayer[ConfigParser, Nothing, ParseCsv] = ZLayer.fromFunction(ParseCsv(_))


}











