package com.gravity.goose.network.gae

import org.apache.http.conn.ClientConnectionManager
import org.apache.http.conn.ClientConnectionRequest
import org.apache.http.conn.ManagedClientConnection
import org.apache.http.conn.routing.HttpRoute
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.scheme.SchemeRegistry
import org.apache.http.conn.scheme.SchemeSocketFactory
import org.apache.http.params.HttpParams
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.TimeUnit
//import scala.reflect.BeanProperty
import scala.beans.BeanProperty
import com.gravity.goose.network.GAEClientConnection
import org.apache.http.conn.HttpClientConnectionManager

class GAEConnectionManager extends ClientConnectionManager {
  @BeanProperty
  var schemeRegistry: SchemeRegistry = new SchemeRegistry()

  val no_socket_factory = new SchemeSocketFactory() {

    override def isSecure(sock: Socket): Boolean = false

    override def createSocket(params: HttpParams): Socket = null

    override def connectSocket(sock: Socket, remoteAddress: InetSocketAddress, localAddress: InetSocketAddress, params: HttpParams): Socket = {
      null
    }
  }

  schemeRegistry.register(new Scheme("http", 80, no_socket_factory))

  schemeRegistry.register(new Scheme("https", 443, no_socket_factory))

  override def requestConnection(route: HttpRoute, state: AnyRef): ClientConnectionRequest = {
    new ClientConnectionRequest() {

      def abortRequest() {
      }

      def getConnection(timeout: Long, tunit: TimeUnit): ManagedClientConnection = {
        GAEConnectionManager.this.getConnection(route, state)
      }
    }
  }

  override def releaseConnection(conn: ManagedClientConnection, validDuration: Long, timeUnit: TimeUnit) {
  }

  override def closeIdleConnections(idletime: Long, tunit: TimeUnit) {
  }

  override def closeExpiredConnections() {
  }

  override def shutdown() {
  }

  private def getConnection(route: HttpRoute, state: AnyRef): ManagedClientConnection = {
    new GAEClientConnection(this, route, state)
  }
}
