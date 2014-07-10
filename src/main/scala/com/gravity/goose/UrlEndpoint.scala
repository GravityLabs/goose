package com.gravity.goose

import com.gravity.goose._
import org.simpleframework.http.core.Container
import org.simpleframework.transport.connect.SocketConnection
import org.simpleframework.http.Response
import org.simpleframework.http.Request
import java.net.InetSocketAddress
import URLEndpoint._
import scala.collection.JavaConversions._
import com.gravity.goose.extractors.ContentExtractor
import scala.collection.mutable.Map
import java.util.HashMap
import eu.ec.dgempl.eessi.utils.JsonUtil

object URLEndpoint {

  def main(args: Array[String]) {
    val container = new URLEndpoint()
    val connection = new SocketConnection(container)
    val address = new InetSocketAddress(8890)
    connection.connect(address)
  }

  def encodeHTML(s: String): String = {
    if (s == null) {
      return ""
    }
    val out = new StringBuffer()
    for (i <- 0 until s.length) {
      val c = s.charAt(i)
      if (c > 127 || c == '"' || c == '<' || c == '>') {
        out.append("&#" + c.toInt + ";")
      } else {
        out.append(c)
      }
    }
    out.toString
  }
}

class URLEndpoint extends Container {

  def handle(request: Request, response: Response) {
    try {
      val body = response.getPrintStream
      val map = Map[String,Any]()
      val url = request.getQuery.get("url")
      println("read article from ["+url+"]")
      if (url == null || url.length == 0) {
        map.put("error", true)
        map.put("message", "No URL specified")
      } else {
        val config = new Configuration
        config.setImagemagickConvertPath("/usr/bin/convert")
        config.setImagemagickIdentifyPath("/usr/bin/identify")
        config.setLocalStoragePath("./storage")
	    val goose = new Goose(config)
	    val article = goose.extractContent(url)
        map.put("success", true)
        map.put("title", encodeHTML(article.getTitle))
        val image = article.getTopImage
        if (image != null) {
          map.put("image", article.getTopImage.getImageSrc)
        }
        map.put("images", article.getAllImages)
        map.put("link", article.getCanonicalLink)
        map.put("text", encodeHTML(article.getCleanedArticleText))
      }
      val responseString = JsonUtil.toJson(map)
      println(responseString)
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
