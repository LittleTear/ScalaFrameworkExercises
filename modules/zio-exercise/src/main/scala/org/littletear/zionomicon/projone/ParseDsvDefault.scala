package org.littletear.zionomicon.projone
import zio._
trait ParseDsvDefault {

  def csvReader: ZIO[Any, Throwable, List[Map[String, String]]]

//  def csvWriter(iter:Iterator[Map[String, String]]):Boolean

}
