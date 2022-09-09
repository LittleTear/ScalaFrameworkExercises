package org.littletear.zionomicon
import zio._
object Chapter19 {

  val modified: ZLayer[Any, Nothing, Config] = Config.default.update[Config]{ config =>
    new Config {
      override def defaultParallelism: Int = config.defaultParallelism * 2
    }
  }

  lazy val zio:ZIO[Config,Nothing,Unit] = ???

  lazy val withUpdateConfig =
    zio.updateService[Config]{config =>
      new Config {
        override def defaultParallelism: Int = config.defaultParallelism * 2
      }
    }


}

//接口定义
trait  Logging {
  def logLine(line:String): UIO[Unit]
}

//接口实现
object Logging {
  val console: ULayer[Logging] = ZLayer.succeed{
    new Logging {
      override def logLine(line: String): UIO[Unit] = ZIO.succeed(println(line))
    }
  }

  //访问器方法
  def logLine(line:String):URIO[Logging,Unit] = ZIO.serviceWithZIO(_.logLine(line))


}

trait Config{
  def defaultParallelism:Int
}
object Config {
  val default: ULayer[Config] = ZLayer.succeed(
    new Config {
      override def defaultParallelism: Int = 4
    }
  )
}