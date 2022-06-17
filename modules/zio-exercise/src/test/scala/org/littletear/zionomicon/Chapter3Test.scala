package org.littletear.zionomicon
import org.scalatest.funsuite.AnyFunSuite
import zio._
import zio.test._
import zio.test.Assertion._
import zio.test.TestAspect.nonFlaky
object Chapter3Test {

}

class ExampleSpec extends AnyFunSuite{
  test("addition works") {
    assert( 1 + 1 === 2)
  }
}

class ExampleSpec2 extends AnyFunSuite {
  val runtime: Runtime[Any] = Runtime.default
  test("addition works") {
    assert(runtime.unsafeRun(ZIO.succeed(1 + 1)) === 2 )
  }
}

class ExampleSpec3 extends  ZIOSpecDefault {
  def spec = suite("ExampleSpec3")(
    test("addition works") {
      assert(1 + 1)(equalTo(2))
    }
  )
}

object ExampleSpec4 extends ZIOSpecDefault {
  def spec = suite("ExampleSpec4")(
    test("ZIO.succeed succeeds with specified value") {
      assertZIO(ZIO.succeed(1 + 1))(equalTo(2))
    },
    test("testing an effect using map operator"){
      ZIO.succeed(1 + 1).map(n => assert(n)(equalTo(2)))
    },
    test("testing an effect using a for comprehension") {
      for{
        n <- ZIO.succeed(1+1)
      } yield assert(n)(equalTo(2))
    },
    test("and"){
      for{
        x <- ZIO.succeed(1)
        y <- ZIO.succeed(2)
      } yield assert(x)(equalTo(1)) && assert(y)(equalTo(2))
    },
    test("hasSameElements") {
      assert(List(1,2,3,4,5))(hasSameElements(List(4,5,2,3,1)))
    },
    test("fails"){
      for{
        exit <- ZIO.attempt(1/0).catchAll(t => ZIO.fail(())).exit
      } yield assert(exit)(fails(isUnit))
    },
    test("greet says hello to the user") {
      for {
        _ <- TestConsole.feedLines("jane")
        _ <- Chapter3.greet
        value <- TestConsole.output
      } yield assert(value)(equalTo(Vector("Hello, Jane!\n")))
    }

  )
}

object ExampleSpec6 extends ZIOSpecDefault {
  val intGen: Gen[Any, Int] = Gen.int
  val genName: Gen[Sized, String] = Gen.asciiString
  val genAge: Gen[Any, Int] = Gen.int(18,120)
  val getUser: Gen[Sized, User] =
    for{
      name <- genName
      age  <- genAge
    } yield User(name,age)

  def spec = suite("ExampleSpec")(
    test("goShopping delays for one hour") {
      for {
        fiber <-Chapter3.goShopping.fork
        _ <- TestClock.adjust(1.hour)
        _ <- fiber.join
      } yield assertCompletes
    },
    test("this test will be repeated to ensure it is stable"){
      assertZIO(ZIO.succeed(1+1))(equalTo(2))
    } @@ nonFlaky ,
    test("integer addition is associative") {
      check(intGen,intGen,intGen) {(x,y,z) =>
        val left = (x + y) + z
        val right = x + (y + z)
        assert(left)(equalTo(right))
      }
    }
  )
}

final case class User(name:String,age:Int)