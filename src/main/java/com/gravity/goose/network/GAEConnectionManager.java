package com.gravity.goose.network;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * HttpClient ClientConnectionManager implementation which works around the lack
 * of threads in GAE
 *
 * from - http://esxx.blogspot.com/2009/06/using-apaches-httpclient-on-google-app.html
 * 
 * @author Martin Blom (martin@blom.org)
 */
public class GAEConnectionManager implements ClientConnectionManager {

	public GAEConnectionManager() {
		SchemeSocketFactory no_socket_factory = new SchemeSocketFactory() {
			@Override
			public boolean isSecure(Socket sock)
					throws IllegalArgumentException {
				return false;
			}
			@Override
			public Socket createSocket(HttpParams params) throws IOException {
				return null;
			}
			@Override
			public Socket connectSocket(Socket sock,
					InetSocketAddress remoteAddress,
					InetSocketAddress localAddress, HttpParams params)
					throws IOException, UnknownHostException,
					ConnectTimeoutException {
				return null;
			}
		};

		schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, no_socket_factory));
		schemeRegistry.register(new Scheme("https", 443, no_socket_factory));
	}

	@Override
	public SchemeRegistry getSchemeRegistry() {
		return schemeRegistry;
	}

	@Override
	public ClientConnectionRequest requestConnection(final HttpRoute route, final Object state) {
		return new ClientConnectionRequest() {
			public void abortRequest() {
				// Nothing to do
			}

			public ManagedClientConnection getConnection(long timeout, TimeUnit tunit) {
				return GAEConnectionManager.this.getConnection(route, state);
			}
		};
	}

	@Override
	public void releaseConnection(ManagedClientConnection conn, long validDuration, TimeUnit timeUnit) {
	}

	@Override
	public void closeIdleConnections(long idletime, TimeUnit tunit) {
	}

	@Override
	public void closeExpiredConnections() {
	}

	@Override
	public void shutdown() {
	}

	private ManagedClientConnection getConnection(HttpRoute route, Object state) {
		return new GAEClientConnection(this, route, state);
	}

	private SchemeRegistry schemeRegistry;
}