package com.gravity.goose

import java.net.InetSocketAddress

import org.simpleframework.http.Request
import org.simpleframework.http.Response
import org.simpleframework.http.core.Container
import org.simpleframework.transport.connect.SocketConnection

object URLEndpoint {

  def main(args: Array[String]) {
    val container = new URLEndpoint()
    val connection = new SocketConnection(container)
    val address = new InetSocketAddress(8890)
    connection.connect(address)
  }
}

class URLEndpoint extends Container {
  def handle(request: Request, response: Response) {
    try {
      val url = request.getQuery.get("url")
      val body = response.getPrintStream
      val responseString = JsonMain.getArticleAsJson(url)
      body.println(responseString)
      response.set("Content-Type", "application/json")
      val time = System.currentTimeMillis()
      response.setDate("Date", time)
      response.setDate("Last-Modified", time)
      body.close()
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
