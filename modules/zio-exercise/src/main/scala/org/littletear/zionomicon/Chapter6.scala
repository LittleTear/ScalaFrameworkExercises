package org.littletear.zionomicon
import zio._

object Chapter6 {

  lazy val doSomething:UIO[Unit] = ???
  lazy val doSomethingElse:UIO[Unit] = ???

  lazy val example1 = for {
    _ <- doSomething
    _ <- doSomethingElse
  } yield ()

  lazy val example2 = for{
    _ <- doSomething.fork
    _ <- doSomethingElse
  } yield ()


 val child = {
   Console.printLine("Child fiber beginning execution...").orDie *>
     Clock.sleep(5.second) *>
     Console.printLine("Hello from a child fiber!").orDie

 }
}
