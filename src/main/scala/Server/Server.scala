package Server

import java.io.{BufferedReader, InputStreamReader, ObjectInputStream}
import java.net._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import java.net.ServerSocket
import java.io.BufferedInputStream
import java.io.PrintStream
import java.io.BufferedOutputStream


class Server {

  private var in : BufferedReader = null
  private var messages : List[String] = List()
  private var buffer: Array[Byte] = null
  private var packet : DatagramPacket = null
  private var address : InetAddress  = null
  private var host : String = null
  private var UDPPort : Int = 1502
  private var TCPPort : Int = 5000

  def ProcessServer(host: String, UDPPort: Int, TCPPort: Int): Unit = {
    this.UDPPort = UDPPort
    this.TCPPort = TCPPort
    this.address = InetAddress.getByName(host)
    this.host = host
    this.transmit()
  }

  private def processTCPMessages(): Unit = {
    val TCPSocket = new ServerSocket()
    TCPSocket.bind(new InetSocketAddress("localhost", 5000))
    while(true) {
      val clientSocket = TCPSocket.accept()
      val is = new BufferedInputStream(clientSocket.getInputStream)
      val reader = new BufferedReader(new InputStreamReader(is))
      val msg = reader.readLine()
      messages = messages ::: List(msg)
      clientSocket.close()
      is.close()
    }
    TCPSocket.close()
  }

  private def processUDPMessages(): Unit ={
    val UPDSocket = new DatagramSocket()
    while(true){
      messages.foreach(x => {
        buffer = x.getBytes()
        packet = new DatagramPacket(buffer, buffer.length, address, UDPPort)
        UPDSocket.send(packet)
      })
      println("Текущее содержимое messages: " + messages)
      messages = List()
      Thread.sleep(10000)
    }
    UPDSocket.close()
  }

  private def transmit(): Unit = {
    val processUDP = new Thread(new Runnable {
      override def run(): Unit = {
        processUDPMessages()
      }
    })
    val processTCP = new Thread(new Runnable {
      override def run(): Unit = {
        processTCPMessages()
      }
    })
    try {

      processUDP.start()
      processTCP.start()
      println("Cервер запущен!")
      while (true) {}
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      processTCP.interrupt()
      processUDP.interrupt()
    }
  }
}
