package org.littletear.zionomicon
import zio._
import scala.concurrent.{ExecutionContext, Future}
//import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn

object Chapter1 extends ZIOAppDefault {

  val goShopping: Task[Unit] = ZIO.attempt(println("Going to the grocery store"))
  val goShoppingLater: ZIO[Any with Clock, Throwable, Unit] = goShopping.delay(1.hour)


  val run = {
//    readIntOrRetry.provideLayer(Console.live).exitCode
    //goShopping.exitCode
    ???
  }


  val readLine: Task[String] = ZIO.attempt(StdIn.readLine())
  def printLine(line:String): Task[Unit] = ZIO.attempt(println(line))
  val echo: ZIO[Any, Throwable, Unit] = readLine.flatMap(line => printLine(line))

  val echo2: ZIO[Any, Throwable, Unit] =
    for {
    line <- readLine
     _ <- printLine(line)
  } yield ()

  val firstName =
    ZIO.attempt(StdIn.readLine("What is your first name?"))
  val lastName =
    ZIO.attempt(StdIn.readLine("What is your last name"))
  val fullName = firstName.zipWith(lastName)((first,last) => println(s"$first $last"))

  val helloWorld =
    ZIO.attempt(print("hello, ")) *> ZIO.attempt(print("World!\n"))

  val printNumbers: ZIO[Any, Throwable, List[Unit]] =
    ZIO.foreach(List(1,2,3)) {n =>
      printLine(n.toString)
    }

  val prints = List(
    printLine("The"),
    printLine("quick"),
    printLine("brown"),
    printLine("fox")
  )

  val printWords: ZIO[Any, Throwable, List[Unit]] =
    ZIO.collectAll(prints)

  val first: ZIO[Any,Nothing,Unit] = ZIO.succeed(println("Going to the grocery store"))
  val second: UIO[Unit] = ZIO.succeed(println("Going to the grocery store"))


//  val goShoppingF: Future[Unit] = Future(println("Going to the grocery store"))

  //ZIO async

//  def getUserByIdAsync(id:Int)(cb: Option[String] => Unit):Unit = ???

//  getUserByIdAsync(0){
//    case Some(name) => println(name)
//    case None => println("User not found")
//  }
//
//  def getUserById(id:Int):ZIO[Any,None.type ,String] =
//    ZIO.effectAsync{callback =>
//      getUserByIdAsync(id) {
//        case Some(name) => callback(ZIO.succeed(name))
//        case None => callback(ZIO.fail(None))
//      }
//    }

  def goShoppingFuture(implicit ec: ExecutionContext): Future[Unit] = Future(println("Going to the grocery store"))

  val goShoppingF = ZIO.fromFuture(implicit ec => goShoppingFuture(ec))


  val readInt:RIO[Console,Int] =
    for{
      line <- Console.readLine
      int  <- ZIO.attempt(line.toInt)
    } yield int

  lazy val readIntOrRetry:RIO[Console,Int] =
    readInt
      .orElse(Console.printLine("Please enter a valid integer")
      .zipRight(readIntOrRetry))
}


//
//final case class ZIP[-R,+E,+A](run: R => Either[E,A]) {self =>
//
//  def map[B](f: A => B):ZIP[R,E,B] = ZIP(r => self.run(r).map(f))
//
//  def flatMap[R1 <: R,E1 <:E,B] (
//      f: A => ZIO[R1,E1,B]
//                                ):ZIO[R1,E1,B] = ZIP(r => self.run(r).fold(ZIP.fail(_), f).run(r))
//}
//
//object ZIP {
//  def effect[A](a: => A):ZIP[Any,Throwable,A] = ZIP(_ => try Right(a) catch {case t: Throwable => Left(t)})
//
//  def fail[E](e: => E):ZIP[Any,E,Nothing] = ZIP(_ => Left(e))
//}

