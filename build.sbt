import sbt.Keys._
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

name := "Laminar-Play-ZIO"
version := "0.3"

val circeVersion = "0.14.1"
val zioVersion = "1.0.12"
val laminarVersion = "0.13.1"

ThisBuild / scalaVersion := "2.13.4"
ThisBuild / testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
ThisBuild / scalacOptions := List(
  "-deprecation",
  "-feature"
)

lazy val `shared` = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio" %%% "zio" % zioVersion,
      "dev.zio" %%% "zio-streams" % zioVersion
    ),
    libraryDependencies ++= Seq( // circe for json serialisation
      "io.circe" %%% "circe-core",
      "io.circe" %%% "circe-generic",
      "io.circe" %%% "circe-parser",
      "io.circe" %%% "circe-shapes",
      "io.circe" %%% "circe-generic-extras"
    ).map(_ % circeVersion),
    libraryDependencies ++= Seq(
      "dev.zio" %%% "zio-test" % zioVersion % "test",
      "dev.zio" %%% "zio-test-sbt" % zioVersion % "test"
    ),
    libraryDependencies ++= Seq(
      "be.doeraene" %%% "url-dsl" % "0.2.0"
    )
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      // java.time library support for Scala.js
      "io.github.cquiroz" %%% "scala-java-time-tzdb" % "2.0.0"
    )
  )


lazy val `backend` = (project in file("./backend"))
  .enablePlugins(PlayScala)
  .settings(
    libraryDependencies ++= Seq(
      // binding of slick for play
      "com.typesafe.play" %% "play-slick" % "5.0.0",
      // handle db connection pool
      "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
      // db evolutions
      evolutions,
      "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
      // dependency injection
      guice,
      // in memory database for illustration purposes
      "com.h2database" % "h2" % "1.4.200",
      // BCrypt library for hashing password
      "org.mindrot" % "jbcrypt" % "0.4"
    )
  )
  .dependsOn(shared.jvm)

lazy val `frontend` = (project in file("./frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    copyFrontendFastOpt := {
      (Compile / fastOptJS).value.data
    },
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % laminarVersion
    )
  )
  .dependsOn(shared.js)

val copyFrontendFastOpt = taskKey[File]("Return main process fast compiled file directory.")
lazy val fastOptCompileCopy = taskKey[Unit]("Compile and copy paste projects and generate corresponding json file.")
val copyPath: String = "backend/public/"

fastOptCompileCopy := {
  val frontendDirectory = (`frontend` / copyFrontendFastOpt).value
  IO.copyFile(frontendDirectory, baseDirectory.value / copyPath / "frontend-scala.js")
  IO.copyFile(
    frontendDirectory.getParentFile / "frontend-fastopt.js.map",
    baseDirectory.value / copyPath / "frontend-fastopt.js.map"
  )
}
