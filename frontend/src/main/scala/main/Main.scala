package main

import com.raquo.laminar.api.L._
import org.scalajs.dom
import services.httpclient._
import zio._


object Main extends zio.App {

  final val layer = zio.console.Console.live ++ FHttpClient.live

  def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val program = for {
      _ <- console.putStrLn("Hello from Scala.js!")
      _ <- Task(render(dom.document.getElementById("root"), LoginForm(layer)))
    } yield 0

    program
      .provideSomeLayer[zio.ZEnv](layer)
      .exitCode
  }

}
