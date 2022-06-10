package org.littletear.zionomicon
import zio._
object ExercisesWithZIO {

  /**
   * Implement a ZIO version of the function readFile by using the
     ZIO.attempt constructor.
   */
  object Exercise1 {
    def readFile(file: String): String = {
      val source = scala.io.Source.fromFile(file)

      try source.getLines.mkString
      finally source.close()
    }

    def readFilZio(file:String): Task[String] =
      ZIO.attempt(readFile(file))
  }

  /**
   * Implement a ZIO version of the function writeFile by using the
     ZIO.attempt constructor.
   */

  object Exercise2{
    def writeFile(file: String, text: String): Unit = {
      import java.io._
      val pw = new PrintWriter(new File(file))
      try pw.write(text)
      finally pw.close
    }

    def writeFileZio(file:String,text:String): Task[Unit] = {
      ZIO.attempt(writeFile(file,text))
    }
  }

  /**
   *
   * Using the `flatMap` method of ZIO effects, together with the `readFileZio`
   * and `writeFileZio` functions that you wrote, implement a ZIO version of
   * the function `copyFile`.
   */

  object Exercise3{
    import Exercise1._
    import Exercise2._
    def copyFile(source: String, dest: String): Unit = {
      val contents = readFile(source)
      writeFile(dest, contents)
    }
    def copyFileZio(source: String, dest: String):IO[Throwable,Unit] = {
      readFilZio(source).flatMap{line => writeFileZio(dest,line)}
    }

  }

  /**
   * Rewrite the following ZIO code that uses `flatMap` into a
   * _for comprehension_.
   */
  object Exercise4{

    def printLine(line: String): Task[Unit] = ZIO.attempt(println(line))
    val readLine: Task[String] = ZIO.attempt(scala.io.StdIn.readLine())
    printLine("What is your name?").flatMap(_ =>
      readLine.flatMap(name =>
        printLine(s"Hello, ${name}!")))

    for{
      _ <- printLine("What is your name?")
      name <- readLine
      _    <- printLine(s"Hello, ${name}!")
    } yield ()
  }


  /**
   * Rewrite the following ZIO code that uses flatMap into a for comprehension.
   */

  object Exercise5{
    val random = ZIO.attempt(scala.util.Random.nextInt(3) + 1)
    def printLine(line: String): Task[Unit] = ZIO.attempt(println(line))
    val readLine = ZIO.attempt(scala.io.StdIn.readLine())
    random.flatMap { int =>
      printLine("Guess a number from 1 to 3:").flatMap { _ =>
        readLine.flatMap { num =>
          if (num == int.toString) printLine("You guessed right!")
          else printLine(s"You guessed wrong, the number was $int!")
        }
      }
    }


    for{
      int <- random
      _ <- printLine("Guess a number from 1 to 3:")
      num <- readLine
      _ <- if (num == int.toString)  printLine("You guessed right!")
           else printLine(s"You guessed wrong, the number was $int!")
    } yield ()


  }

  /**
   * Implement the zipWith function in terms of the toy model of a ZIO
     effect. The function should return an effect that sequentially composes
     the specified effects, merging their results with the specified user-defined
     function.
   */
  object Exercise6{
    final case class ZIO[-R, +E, +A](run: R => Either[E, A])
    def zipWith[R, E, A, B, C](
                                self: ZIO[R, E, A],
                                that: ZIO[R, E, B])(f: (A, B) => C): ZIO[R, E, C] =
      ZIO(r => self.run(r).flatMap(a => that.run(r).map(b => f(a,b))))
  }

  /**
   * Implement the collectAll function in terms of the toy model of a ZIO
     effect. The function should return an effect that sequentially collects the
     results of the specified collection of effects.
   */

  object Exercise7{
    import Exercise6._
    def succeed[A](a:A): ZIO[Any, Nothing, A] = ZIO(_ => Right(a))
    def collectAll[R, E, A](
                             in: Iterable[ZIO[R, E, A]]
                           ): ZIO[R, E, List[A]] =
      if (in.isEmpty) succeed(List.empty)
      else zipWith(in.head,collectAll(in.tail))(_::_)
  }


  /**
   * Implement the foreach function in terms of the toy model of a ZIO effect.
     The function should return an effect that sequentially runs the specified
     function on every element of the specified collection.
   */

  object Exercise8{
    import Exercise7._
    def foreach[R, E, A, B](
                             in: Iterable[A]
                           )(f: A => ZIO[R, E, B]): ZIO[R, E, List[B]] =
      ???
  }
}
