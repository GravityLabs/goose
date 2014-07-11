package com.gravity.goose.network

import org.apache.http._
import org.apache.http.conn.ClientConnectionManager
import org.apache.http.conn.ManagedClientConnection
import org.apache.http.conn.routing.HttpRoute
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.message.BasicHttpResponse
import org.apache.http.params.HttpParams
import org.apache.http.protocol.HttpContext
import com.google.appengine.api.urlfetch._
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.InetAddress
import java.net.URI
import java.net.URISyntaxException
import java.util.concurrent.TimeUnit
import GAEClientConnection._
import scala.reflect.{ BeanProperty, BooleanBeanProperty }
import scala.collection.JavaConversions._
import org.apache.http.conn.HttpClientConnectionManager
import java.net.Socket

object GAEClientConnection {

  private var urlFS: URLFetchService = URLFetchServiceFactory.getURLFetchService
}

class GAEClientConnection(cm: ClientConnectionManager, @BeanProperty var route: HttpRoute, @BeanProperty var state: AnyRef) extends ManagedClientConnection {
  def getId(): String = ???
  def bind(socket: Socket) = ???
  def getSocket() = ???
  override def isSecure(): Boolean = route.isSecure

  override def getSSLSession(): javax.net.ssl.SSLSession = null

  override def open(route: HttpRoute, context: HttpContext, params: HttpParams) {
    close()
    this.route = route
  }

  override def tunnelTarget(secure: Boolean, params: HttpParams) {
    throw new IOException("tunnelTarget() not supported")
  }

  override def tunnelProxy(next: HttpHost, secure: Boolean, params: HttpParams) {
    throw new IOException("tunnelProxy() not supported")
  }

  override def layerProtocol(context: HttpContext, params: HttpParams) {
    throw new IOException("layerProtocol() not supported")
  }

  override def markReusable() {
    reusable = true
  }

  override def unmarkReusable() {
    reusable = false
  }

  override def isMarkedReusable(): Boolean = reusable

  override def setIdleDuration(duration: Long, unit: TimeUnit) {
  }

  override def isResponseAvailable(timeout: Int): Boolean = response != null

  override def sendRequestHeader(request: HttpRequest) {
    val host = route.getTargetHost
    val uri = new URI(host.getSchemeName + "://" + host.getHostName + (if ((host.getPort == -1)) "" else (":" + host.getPort)) + request.getRequestLine.getUri)
    this.request = new HTTPRequest(uri.toURL(), HTTPMethod.valueOf(request.getRequestLine.getMethod), FetchOptions.Builder.disallowTruncate())
    for (h <- request.getAllHeaders) {
      this.request.addHeader(new HTTPHeader(h.getName, h.getValue))
    }
  }

  override def sendRequestEntity(request: HttpEntityEnclosingRequest) {
    val baos = new ByteArrayOutputStream()
    if (request.getEntity != null) {
      request.getEntity.writeTo(baos)
    }
    this.request.setPayload(baos.toByteArray())
  }

  override def receiveResponseHeader(): HttpResponse = {
    if (this.response == null) {
      flush()
    }
    val response = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), this.response.getResponseCode, null)
    for (h <- this.response.getHeaders) {
      response.addHeader(h.getName, h.getValue)
    }
    response
  }

  override def receiveResponseEntity(response2: HttpResponse) {
    //TODO review response2 and this.response
    if (this.response == null) {
      throw new IOException("receiveResponseEntity() called on closed connection")
    }
    val bae = new ByteArrayEntity(this.response.getContent)
    bae.setContentType(response2.getFirstHeader("Content-Type"))
    response2.setEntity(bae)
    this.response = null
  }

  override def flush() {
    if (request != null) {
      try {
        response = urlFS.fetch(request)
        request = null
      } catch {
        case ex: IOException => {
          ex.printStackTrace()
          throw ex
        }
      }
    } else {
      response = null
    }
  }

  override def close() {
    request = null
    response = null
    closed = true
  }

  override def isOpen(): Boolean = request != null || response != null

  override def isStale(): Boolean = !isOpen && !closed

  override def setSocketTimeout(timeout: Int) {
  }

  override def getSocketTimeout(): Int = -1

  override def shutdown() {
    close()
  }

  override def getMetrics(): HttpConnectionMetrics = null

  override def getLocalAddress(): InetAddress = null

  override def getLocalPort(): Int = 0

  override def getRemoteAddress(): InetAddress = null

  override def getRemotePort(): Int = {
    val host = route.getTargetHost
    connManager.getSchemeRegistry.getScheme(host).resolvePort(host.getPort)
  }

  override def releaseConnection() {
    connManager.releaseConnection(this, java.lang.Long.MAX_VALUE, TimeUnit.MILLISECONDS)
  }

  override def abortConnection() {
    unmarkReusable()
    shutdown()
  }

  private var connManager: ClientConnectionManager = cm

  private var reusable: Boolean = _

  private var request: HTTPRequest = _

  private var response: HTTPResponse = _

  private var closed: Boolean = true
}
