package UDP

import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket

class Client() {

  private var address: InetAddress = null
  private var buffer: Array[Byte] = null
  private var packet: DatagramPacket = null
  private var str: String = null
  private var socket: MulticastSocket = null
  private var port: Int = 1502
  private var id: Int = 0

  def ProcessClient(host: String, port: Int, id: Int): Unit = {
    this.address = InetAddress.getByName(host)
    this.port = port
    this.id = id
    this.run()
  }

  private def run(): Unit = {
    try {
      println("UDP клиент запущен! Ждите сообщения.")
      socket = new MulticastSocket(port)
      // Регистрация клиента в группе
      socket.joinGroup(address)
      while (true) {
        buffer = new Array[Byte](256)
        packet = new DatagramPacket(buffer, buffer.length)
        socket.receive(packet)
        str = new String(packet.getData)
        println("UDP клиент " + this.id + " получил сообщение: " + str.trim)
      }
    } catch {
      case e: Exception =>
        e.printStackTrace()
    } finally {
      try {
        socket.leaveGroup(address)
        socket.close()
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
  }
}
