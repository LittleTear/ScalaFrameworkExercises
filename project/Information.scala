import sbt.Keys._
import sbt.{ url, Def, Developer, ScmInfo }
import sbt.URL
import de.heikoseeberger.sbtheader.HeaderPlugin.autoImport.headerCreateAll
import sbt.Compile

object Information {
  val value: Seq[Def.Setting[_]] = Seq(
    Compile / compile := (Compile / compile).dependsOn(Compile / headerCreateAll).value,
    organization := "org.bitlap",
    organizationName := "bitlap",
    startYear := Some(2022),
    description := "zim is a functional-style, asynchronous and streaming IM based on scala and zio",
    homepage := Some(url(s"https://github.com/LittleTear/framework-exercise")),
    licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
    developers := List(
      Developer(
        "jxnu-liguobin",
        "梦境迷离",
        "dreamylost@outlook.com",
        url("https://github.com/LittleTear")
      )
    ),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/LittleTear/framework-exercise"),
        "scm:git@github.com:LittleTear/framework-exercise.git"
      )
    )
  )
}
