package org.littletear.zionomicon.zioapplicationex
import zio._

case class DB (connectionPool:ConnectionPool) {

  private def connection: ZIO[Scope, Throwable, Connection] =
    ZIO.acquireRelease(connectionPool.obtain){c =>
      connectionPool
        .release(c)
        .catchAll( t => ZIO.logErrorCause(
          "Exception when releasing a connection",Cause.fail(t)
        ))
    }



  def transact[R,E,A](dbProgram: ZIO[ Connection ,E,A]):ZIO[R,E,A] = ???

//    ZIO.scoped{
//      connection.flatMap{c =>
//        dbProgram.provideSomeLayer(ZLayer.succeed(c))
//    }
//
//}


}

object DB {

  lazy val live:ZLayer[ConnectionPool,Nothing,DB] = ZLayer.fromFunction(DB(_))
}
