package org.littletear.zionomicon

import zio._
object Chapter5 {

val runtime: Runtime[Any] = Runtime.default
//val runtimeLayer: Runtime.Scoped[Nothing] = Runtime.unsafeFromLayer()
 def doSomething:UIO[Int] = ???

 def doSomethingElse:UIO[Int] = {
//   val something = runtime.unsafeRun(doSomething)
   ???
 }
 val a =  ZIO.blockingExecutor.map(_.asExecutionContext)

}
