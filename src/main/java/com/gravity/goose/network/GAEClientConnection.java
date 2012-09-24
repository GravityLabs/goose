package com.gravity.goose.network;

import org.apache.http.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import com.google.appengine.api.urlfetch.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * HttpClient ManagedClientConnection implementation which works around the lack
 * of threads in GAE
 *
 * from - http://esxx.blogspot.com/2009/06/using-apaches-httpclient-on-google-app.html
 * 
 * @author Martin Blom (martin@blom.org)
 */
class GAEClientConnection implements ManagedClientConnection {

	public GAEClientConnection(ClientConnectionManager cm, HttpRoute route, Object state) {
		this.connManager = cm;
		this.route = route;
		this.state = state;
		this.closed = true;
	}

	// From interface ManagedClientConnection

	@Override
	public boolean isSecure() {
		return route.isSecure();
	}

	@Override
	public HttpRoute getRoute() {
		return route;
	}

	@Override
	public javax.net.ssl.SSLSession getSSLSession() {
		return null;
	}

	@Override
	public void open(HttpRoute route, HttpContext context, HttpParams params)
			throws IOException {
		close();
		this.route = route;
	}

	@Override
	public void tunnelTarget(boolean secure, HttpParams params)
			throws IOException {
		throw new IOException("tunnelTarget() not supported");
	}

	@Override
	public void tunnelProxy(HttpHost next, boolean secure, HttpParams params)
			throws IOException {
		throw new IOException("tunnelProxy() not supported");
	}

	@Override
	public void layerProtocol(HttpContext context, HttpParams params)
			throws IOException {
		throw new IOException("layerProtocol() not supported");
	}

	@Override
	public void markReusable() {
		reusable = true;
	}

	@Override
	public void unmarkReusable() {
		reusable = false;
	}

	@Override
	public boolean isMarkedReusable() {
		return reusable;
	}

	@Override
	public void setState(Object state) {
		this.state = state;
	}

	@Override
	public Object getState() {
		return state;
	}

	@Override
	public void setIdleDuration(long duration, TimeUnit unit) {
		// Do nothing
	}

	// From interface HttpClientConnection
	@Override
	public boolean isResponseAvailable(int timeout) throws IOException {
		return response != null;
	}

	@Override
	public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
		try {
			HttpHost host = route.getTargetHost();

			URI uri = new URI(host.getSchemeName() + "://" + host.getHostName()
					+ ((host.getPort() == -1) ? "" : (":" + host.getPort()))
					+ request.getRequestLine().getUri());

			this.request = new HTTPRequest(uri.toURL(),
                                           HTTPMethod.valueOf(request.getRequestLine().getMethod()),
										   FetchOptions.Builder.disallowTruncate());
		} catch (URISyntaxException ex) {
			throw new IOException("Malformed request URI: " + ex.getMessage(), ex);
		} catch (IllegalArgumentException ex) {
			throw new IOException(
					"Unsupported HTTP method: " + ex.getMessage(), ex);
		}

		for (Header h : request.getAllHeaders()) {
			this.request.addHeader(new HTTPHeader(h.getName(), h.getValue()));
		}
	}

	@Override
	public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (request.getEntity() != null) {
			request.getEntity().writeTo(baos);
		}
		this.request.setPayload(baos.toByteArray());
	}

	@Override
	public HttpResponse receiveResponseHeader() throws HttpException,
			IOException {
		if (this.response == null) {
			flush();
		}

		HttpResponse response = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), this.response.getResponseCode(), null);

		for (HTTPHeader h : this.response.getHeaders()) {
			response.addHeader(h.getName(), h.getValue());
		}

		return response;
	}

	@Override
	public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
		if (this.response == null) {
			throw new IOException("receiveResponseEntity() called on closed connection");
		}

		ByteArrayEntity bae = new ByteArrayEntity(this.response.getContent());
		bae.setContentType(response.getFirstHeader("Content-Type"));
		response.setEntity(bae);

		response = null;
	}

	@Override
	public void flush() throws IOException {
		if (request != null) {
			try {
				// System.err.println("----");
				response = urlFS.fetch(request);
				request = null;
			} catch (IOException ex) {
				ex.printStackTrace();
				throw ex;
			}
		} else {
			response = null;
		}
	}

	// From interface HttpConnection
	@Override
	public void close() throws IOException {
		request = null;
		response = null;
		closed = true;
	}

	@Override
	public boolean isOpen() {
		return request != null || response != null;
	}

	@Override
	public boolean isStale() {
		return !isOpen() && !closed;
	}

	@Override
	public void setSocketTimeout(int timeout) {
	}

	@Override
	public int getSocketTimeout() {
		return -1;
	}

	@Override
	public void shutdown() throws IOException {
		close();
	}

	@Override
	public HttpConnectionMetrics getMetrics() {
		return null;
	}

	// From interface HttpInetConnection
	@Override
	public InetAddress getLocalAddress() {
		return null;
	}

	@Override
	public int getLocalPort() {
		return 0;
	}

	@Override
	public InetAddress getRemoteAddress() {
		return null;
	}

	@Override
	public int getRemotePort() {
		HttpHost host = route.getTargetHost();
		return connManager.getSchemeRegistry().getScheme(host).resolvePort(host.getPort());
	}

	// From interface ConnectionReleaseTrigger

	@Override
	public void releaseConnection() throws IOException {
		connManager.releaseConnection(this, Long.MAX_VALUE,
				TimeUnit.MILLISECONDS);
	}

	@Override
	public void abortConnection() throws IOException {
		unmarkReusable();
		shutdown();
	}

	private ClientConnectionManager connManager;
	private HttpRoute route;
	private Object state;
	private boolean reusable;

	private HTTPRequest request;
	private HTTPResponse response;
	private boolean closed;

	private static URLFetchService urlFS = URLFetchServiceFactory.getURLFetchService();
}