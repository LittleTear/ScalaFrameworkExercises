import sbt.Command


object Commands {

    val FmtSbtCommand = Command.command("fmt")(state => "scalafmtSbt" :: "scalafmtAll" :: state)

    val FIXSbtCommand = Command.command("fix")(state => "scalafixEnable" :: "scalafixAll RemoveUnused" :: state)

    val FmtSbtCheckCommand =
      Command.command("check")(state => "scalafmtSbtCheck" :: "scalafmtCheckAll" :: state)

    val value = Seq(
      FmtSbtCommand,
      FmtSbtCheckCommand,
      FIXSbtCommand
    )
}
