package org.littletear.zionomicon.projone

import com.typesafe.config.{Config, ConfigFactory}
import zio._
import domain.FileConfig

case class ConfigParser(private val config:Config) {
  private lazy val default: FileConfig = FileConfig("/root/","a.txt")

  def getFileConfig:Task[FileConfig] = ZIO.succeed(if(config.isEmpty) default else FileConfig(config))
}

object ConfigParser {

  private lazy val conf = ConfigFactory.load().getConfig("file")

  lazy val live: ZLayer[Config, Nothing, ConfigParser] = ZLayer.fromFunction(ConfigParser(_))

  lazy val make: ZLayer[Any, Nothing, ConfigParser] = ZLayer.succeed(conf) >>> live
}
