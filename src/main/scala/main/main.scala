package main

import UDP.{Client, Server}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object main {
  def main(args: Array[String]): Unit = {
    val processServer: Future[Unit] = Future {
      var s = new Server()
      s.ProcessServer("233.0.0.1", 1502)
    }

    val processClient: Future[Unit] = Future {
      var c = new Client()
      c.ProcessClient("233.0.0.1", 1502, 1)
    }

    Thread.sleep(500000)
  }
}
