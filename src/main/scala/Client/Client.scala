package Client

import java.io.{BufferedInputStream, BufferedOutputStream, DataOutputStream, PrintStream, PrintWriter}
import java.net.{DatagramPacket, InetAddress, InetSocketAddress, MulticastSocket, Socket}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Client() {

  private var address: InetAddress = null
  private var buffer: Array[Byte] = null
  private var packet: DatagramPacket = null
  private var str: String = null
  private var UDPPort: Int = 1502
  private var TCPPort: Int = 5000
  private var id: Int = 0

  def ProcessClient(host: String, UDPPort: Int, TCPPort: Int, id: Int): Unit = {
    this.address = InetAddress.getByName(host)
    this.UDPPort = UDPPort
    this.TCPPort = TCPPort
    this.id = id
    this.run()
  }
  
  private def processUDPMessages(): Unit =  {
    val socket = new MulticastSocket(UDPPort)
    socket.joinGroup(address)
    while(true) {
      buffer = new Array[Byte](256)
      packet = new DatagramPacket(buffer, buffer.length)
      socket.receive(packet)
      str = new String(packet.getData, 0, packet.getLength)
      println("Клиент " + id + " получил сообщение: " + str)
    }
  }

  private def processTCPMessages(): Unit = {
    while (true) {
      val socket = new Socket("localhost", TCPPort)
      val os = new DataOutputStream(socket.getOutputStream())
      val msg = math.round(math.random()*100)
      os.writeBytes(id + ": " + msg)

      socket.close()
      os.close()
      Thread.sleep(math.round(math.random() * 10000))
    }
  }

  private def run(): Unit = {
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
      println("Клиент " + id + " запущен!")
      while (true) {}
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      processTCP.interrupt()
      processUDP.interrupt()
    }
  }
}
