import de.heikoseeberger.sbtheader.HeaderPlugin

Global / onLoad := {
  val GREEN = "\u001b[32m"
  val RESET = "\u001b[0m"
  println(s"""$GREEN
             |$GREEN               ,--,
             |$GREEN       ,----,,--.'|
             |$GREEN     .'   .`||  |,
             |$GREEN  .'   .'  .'`--'_       ,---.
             |$GREEN,---, '   ./ ,' ,'|     /      |
             |$GREEN;   | .'  /  '  | |    /    /  |
             |$GREEN`---' /  ;--,|  | :   .    ' / |
             |$GREEN  /  /  / .`|'  : |__ '   ;   /|
             |$GREEN./__;     .' |  | '.'|'   |  / |
             |$GREEN;   |  .'    ;  :    ;|   :    |
             |$GREEN`---'        |  ,   /  |   |  /
             |$GREEN              ---`-'    `----'
             |$RESET        v.${version.value}
             |""".stripMargin)
  (Global / onLoad).value
}

ThisBuild / resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.sonatypeRepo("public"),
  Resolver.sonatypeRepo("snapshots"),
  "New snapshots" at "https://s01.oss.sonatype.org/content/repositories/snapshots/"
)

lazy val assemblySettings = Seq(
  ThisBuild / assemblyMergeStrategy := {
    case "application.conf"                              => MergeStrategy.concat
    case x if x.endsWith(".txt") || x.endsWith(".proto") => MergeStrategy.first
    case x if x.endsWith("module-info.class")            => MergeStrategy.first
    case x if x.endsWith(".properties")                  => MergeStrategy.deduplicate
    case x =>
      val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
      oldStrategy(x)
  }
)

lazy val commonConfiguration: Project => Project =
  _.settings(Information.value)
    .settings(ProjectSetting.value)
    .settings(ProjectSetting.noPublish)
    .settings(commands ++= Commands.value)
    .settings(assemblySettings)
    .settings(
      semanticdbEnabled := true, // enable SemanticDB
      semanticdbVersion := scalafixSemanticdb.revision,
      scalafixOnCompile := true
    )

lazy val zim = (project in file("."))
  .settings(name := "framework-exercise")
  .aggregate(`zio-exercise`, `cats-exercise`, `akka-exercise`)
  .configure(commonConfiguration)

lazy val `zio-exercise` = (project in file("modules/zio-exercise"))
  .settings(
    libraryDependencies ++= Dependencies.serverDeps,
    Compile / scalacOptions ++= List("-Ymacro-annotations")
  )
  .settings(assembly / mainClass := Some("org.littletear.zio.server.ZioServer"))
  .configure(commonConfiguration)
  .enablePlugins(ScalafmtPlugin, HeaderPlugin)
  .dependsOn(`cats-exercise`, `akka-exercise`)

lazy val `cats-exercise` = (project in file("modules/cats-exercise"))
  .settings(BuildInfoSettings.value)
  .settings(libraryDependencies ++= Dependencies.domainDeps)
  .configure(commonConfiguration)
  .enablePlugins(GitVersioning, BuildInfoPlugin, ScalafmtPlugin, HeaderPlugin)

lazy val `akka-exercise` = (project in file("modules/akka-exercise"))
  .settings(libraryDependencies ++= Dependencies.cacheDeps)
  .configure(commonConfiguration)
  .enablePlugins(ScalafmtPlugin, HeaderPlugin)

