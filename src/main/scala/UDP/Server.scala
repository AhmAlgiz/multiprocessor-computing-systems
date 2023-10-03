package UDP

import java.io.{BufferedReader, InputStreamReader}
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class Server {

  private var in : BufferedReader = null
  private var str : String = null
  private var buffer: Array[Byte] = null
  private var packet : DatagramPacket = null
  private var address : InetAddress  = null
  private var port : Int = 1502
  private var socket: DatagramSocket = null

  def ProcessServer(host: String, port: Int): Unit = {
    this.socket = new DatagramSocket()
    this.address = InetAddress.getByName(host)
    this.port = port
    this.transmit()
  }

  private def transmit(): Unit = {
    try {
      println("UDP сервер запущен! Вводите сообщения.")
      in = new BufferedReader(new InputStreamReader(System.in))
      while(true){
        str = in.readLine()
        buffer = str.getBytes()
        packet = new DatagramPacket(buffer, buffer.length, address, port)
        socket.send(packet)
        println("UDP сервер отправил сообщение: " + str.trim)
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      try {
        in.close()
        socket.close()
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
  }
}
