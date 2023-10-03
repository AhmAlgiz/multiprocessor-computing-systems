package main

import Client.Client
import Server.Server

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object main {
  def main(args: Array[String]): Unit = {
    val processServer = new Thread(new Runnable {
      override def run(): Unit = {
        var s = new Server()
        s.ProcessServer("233.0.0.1", 1502, 5000)
      }
    })
    processServer.start()
    Thread.sleep(500)

    val processClient1 = new Thread(new Runnable {
      override def run(): Unit = {
        var c = new Client()
        c.ProcessClient("233.0.0.1", 1502, 5000, 1)
      }
    })
    processClient1.start()

    val processClient2 = new Thread(new Runnable {
      override def run(): Unit = {
        var c = new Client()
        c.ProcessClient("233.0.0.1", 1502, 5000, 2)
      }
    })
    processClient2.start()

    val processClient3 = new Thread(new Runnable {
      override def run(): Unit = {
        var c = new Client()
        c.ProcessClient("233.0.0.1", 1502, 5000, 3)
      }
    })
    processClient3.start()

    while (true) {}
  }
}
