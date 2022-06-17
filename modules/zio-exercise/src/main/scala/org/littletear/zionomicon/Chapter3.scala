package org.littletear.zionomicon
import zio._


object Chapter3 {

  def safeDivision(x:Int,y:Int): ZIO[Any, Unit, Int] =
    ZIO.attempt(x/y).catchAll(t => ZIO.fail(()))

  val greet: ZIO[Any, Nothing, Unit] =
    for{
      name <- Console.readLine.orDie
      _ <- Console.printLine(s"Hello, $name!").orDie
    } yield ()

  val goShopping: ZIO[Any, Nothing, Unit] = Console.printLine("Going shopping !").orDie.delay(1.hour)
//  val a: URIO[Console with Clock, Fiber.Runtime[Nothing, Unit]] = goShopping.fork
//  val b: ZIO[Console with Clock, Nothing, IO[Nothing, Unit]] = a.map(c => c.join)
}



