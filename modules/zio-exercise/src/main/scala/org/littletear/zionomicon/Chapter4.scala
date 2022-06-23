package org.littletear.zionomicon
import zio._

import java.io.IOException


object Chapter4 {

  def readFile(file:String) : ZIO[Any,IOException,String ] = ???
  lazy val result: URIO[Any, String] = readFile("data.txt").orDie
  def readFile2(file:String) = readFile(file).refineOrDie{
    case e:IOException => e
  }

  def shareDocument(
      doc:String
                   ):ZIO[Any,InsufficientPermission,Unit] = ???

  def moveDocument(
      doc:String,
      folder:String
                  ):ZIO[Any,FileIsLocked,Unit] = ???

  lazy val result2: ZIO[Any, Any, Unit] = shareDocument("347823").zip(moveDocument("347823","/temp/"))
  type DocumentError = Either[InsufficientPermission, FileIsLocked]
  lazy val result3: ZIO[Any, DocumentError, Unit] = shareDocument("347823").mapError(Left(_))
    .zip(moveDocument("347823","/temp/").mapError(Right(_))).unit

  lazy val effect : ZIO[Any,IOException,String] = ???
  effect.tapErrorCause(cause => Console.printLine(cause.prettyPrint))


  trait DatabaseError
  trait Userprofile
  def lookupProfile(
      userId:String
                   ):ZIO[Any,DatabaseError,Option[Userprofile]] = ???

  def lookupProfile2(
      userId:String
                    ) =
    lookupProfile(userId).foldZIO(
      error   => ZIO.fail(Some(error)),
      success => success match {
          case None => ZIO.fail(None)
          case Some(profile) => ZIO.succeed(profile)
        }
    )

  def lookupProfile3(
      userId:String
                    ) = lookupProfile(userId).some
}


final case class ZIK[-R,+E,+A] (run: R => Either[E,A]){ self =>

  def foldZIO[R1 <: R,E1,B](
      failure: E => ZIK[R1,E1,B],
      success: A => ZIK[R1,E1,B]
                           ):ZIK[R1,E1,B] =
    ZIK(r => self.run(r).fold(failure,success).run(r))
}
final case class InsufficientPermission(
                                         user: String,
                                         operation: String
                                       )
final case class FileIsLocked(file: String)