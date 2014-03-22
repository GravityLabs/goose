/**
 * Licensed to Gravity.com under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Gravity.com licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gravity.goose.network

import org.apache.http.Header
import org.apache.http.HeaderElement
import org.apache.http.HttpEntity
import org.apache.http.HttpHost
import org.apache.http.HttpVersion
import org.apache.http.{HttpRequest, HttpRequestInterceptor, HttpResponse, HttpResponseInterceptor, HeaderElementIterator}
import org.apache.http.client.entity.GzipDecompressingEntity
import org.apache.http.client.CookieStore
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.params.CookiePolicy
import org.apache.http.client.protocol.ClientContext
import org.apache.http.conn.params.ConnRoutePNames
import org.apache.http.conn.ConnectionKeepAliveStrategy
import org.apache.http.conn.scheme.PlainSocketFactory
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.conn.scheme.{Scheme, SchemeRegistry}
import org.apache.http.cookie.Cookie
import org.apache.http.impl.conn.PoolingClientConnectionManager
import org.apache.http.message.BasicHeaderElementIterator
import org.apache.http.params.{HttpParams, BasicHttpParams, HttpConnectionParams, HttpProtocolParams}
import org.apache.http.protocol.{HTTP, BasicHttpContext, HttpContext}
import org.apache.http.util.EntityUtils
import org.apache.http.entity.ContentType
import java.io._
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.URL
import java.net.URLConnection
import java.util.ArrayList
import java.util.Date
import java.util.List
import com.gravity.goose.utils.Logging
import com.gravity.goose.Configuration
import org.apache.http.impl.client.{DefaultHttpRequestRetryHandler, AbstractHttpClient, DefaultHttpClient}
import org.apache.commons.io.IOUtils
import org.mozilla.universalchardet.UniversalDetector

/**
 * User: Jim Plush
 * Date: 12/16/10
 * This guy is kind of a doozy because goose is meant to pull millions of articles per day so the legitimacy of these links
 * is in question. For example many times you'll see mp3, mov, wav, etc.. files mislabeled as HTML with HTML content types,
 * only through inspection of the actual content will you learn what the real type of content is. Also spam sites could
 * contain up to 1GB of text that is just wasted resources so we set a max bytes level on how much content we're going
 * to try and pull back before we say screw it.
 */
object HtmlFetcher extends AbstractHtmlFetcher with Logging {
  /**
   * holds a reference to our override cookie store, we don't want to store
   * cookies for head requests, only slows shit down
   */
  var emptyCookieStore: CookieStore = null
  
  /**
   * holds the HttpClient object for making requests
   */
  private var httpClient: HttpClient = null
  initClient()

  def getHttpClient: HttpClient = {
    httpClient
  }

  /**
   * Makes an http fetch to go retrieve the HTML from a url, store it to disk and pass it off
   * @param config Goose Configuration
   * @param url The web address to fetch
   * @return If all goes well, a `Some[String]` otherwise `None`
   * @throws NotFoundException(String)
   * @throws BadRequestException(String)
   * @throws NotAuthorizedException(String, Int)
   * @throws ServerErrorException(String, Int)
   * @throws UnhandledStatusCodeException(String, Int)
   * @throws MaxBytesException()
   */
  def getHtml(config: Configuration, url: String): Option[String] = {
    var httpget: HttpGet = null
    var htmlResult: String = null
    var entity: HttpEntity = null
    var instream: InputStream = null
    var contentType: ContentType = null
    
    // Identified the the apache http client does not drop URL fragments before opening the request to the host
    // more info: http://stackoverflow.com/questions/4251841/400-error-with-httpclient-for-a-link-with-an-anchor
    val cleanUrl = {
      val foundAt = url.indexOf("#")
      if (foundAt >= 0) url.substring(0, foundAt) else url
    }

    try {
      val localContext: HttpContext = new BasicHttpContext
      localContext.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore)
      httpget = new HttpGet(cleanUrl)
      httpget.setHeader("referer", config.getBrowserReferer())

      val params = httpClient.getParams
      HttpProtocolParams.setUserAgent(params, config.getBrowserUserAgent())
      trace("Setting UserAgent To: " + HttpProtocolParams.getUserAgent(httpClient.getParams))

      HttpConnectionParams.setConnectionTimeout(params, config.getConnectionTimeout())
      HttpConnectionParams.setSoTimeout(params, config.getSocketTimeout())

      val response: HttpResponse = httpClient.execute(httpget, localContext)

      HttpStatusValidator.validate(cleanUrl, response.getStatusLine.getStatusCode) match {
        case Left(ex) => throw ex
        case _ =>
      }

      entity = response.getEntity
      if (entity != null) {
        instream = entity.getContent
        var encodingType: String = "UTF-8"

        try {
          contentType = ContentType.get(entity)
          trace("Got contentType: " + contentType)
          if (contentType == null) {
            encodingType = "UTF-8"
          } else {
            encodingType = contentType.getCharset().name
          }
        }
        catch {
          case e: Exception => {
            if (logger.isDebugEnabled) {
              trace("Unable to get charset for: " + cleanUrl)
              trace("Encoding Type is: " + encodingType)
            }
          }
        }
        try {
          htmlResult = HtmlFetcher.convertStreamToString(instream, encodingType).trim
        }
        finally {
          EntityUtils.consume(entity)
        }
      }
      else {
        trace("Unable to fetch URL Properly: " + cleanUrl)
      }
    }
    catch {
      case e: NullPointerException => {
        logger.warn(e.toString + " " + e.getMessage + " Caught for URL: " + cleanUrl)
      }
      case e: MaxBytesException => {
        trace("GRVBIGFAIL: " + cleanUrl + " Reached max bytes size")
        throw e
      }
      case e: SocketException => {
        logger.warn(e.getMessage + " Caught for URL: " + cleanUrl)
      }
      case e: SocketTimeoutException => {
        trace(e.toString)
        throw new GatewayTimeoutException(e.toString + " " + e.getMessage)
      }
      case e: LoggableException => {
        logger.warn(e.getMessage)
        throw e
      }
      case e: Exception => {
        trace("FAILURE FOR LINK: " + cleanUrl + " " + e.toString)
        throw e
      }
    }
    finally {
      if (instream != null) {
        try {
          instream.close()
        }
        catch {
          case e: Exception => {
            logger.warn(e.getMessage + " Caught for URL: " + cleanUrl)
          }
        }
      }
      if (httpget != null) {
        try {
          httpget.abort()
          httpget.releaseConnection()
          entity = null
        }
        catch {
          case e: Exception => {
          }
        }
      }
    }
    if (logger.isDebugEnabled) {
      logger.debug("starting...")
    }
    if (htmlResult == null || htmlResult.length < 1) {
      if (logger.isDebugEnabled) {
        logger.debug("HTMLRESULT is empty or null")
      }
      throw new NotHtmlException(cleanUrl)
    }
    var is: InputStream = null
    var mimeType: String = null
    try {
      is = new ByteArrayInputStream(htmlResult.getBytes("UTF-8"))
      mimeType = URLConnection.guessContentTypeFromStream(is)
      if (mimeType != null || contentType != null) {
        if(mimeType == null) {
          mimeType = contentType.getMimeType()
          trace("no guessed mimetype? using contentType: " + mimeType + " - " + cleanUrl)
        }

        if ((mimeType == "text/html") || (mimeType == "application/xml") || (mimeType == "application/xhtml+xml") || (mimeType == "text/xml") ) {
          return Some(htmlResult)
        }
        else {
          if (htmlResult.contains("<title>") == true && htmlResult.contains("<p>") == true) {
            return Some(htmlResult)
          }
          trace("GRVBIGFAIL: " + mimeType + " - " + cleanUrl)
          throw new NotHtmlException(cleanUrl)
        }
      } 
      else {
        trace("no mimetype?: " + mimeType + " - " + cleanUrl)
        throw new NotHtmlException(cleanUrl)
      }
    }
    catch {
      case e: UnsupportedEncodingException => {
        logger.warn(e.getMessage + " Caught for URL: " + cleanUrl)
      }
      case e: IOException => {
        logger.warn(e.getMessage + " Caught for URL: " + cleanUrl)
      }
    }
    None
  }

  private def initClient() {

    trace("Initializing HttpClient")

    val httpParams: HttpParams = new BasicHttpParams
    HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000) // 10 seconds
    HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000) // 10 seconds
    HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1)
    emptyCookieStore = new CookieStore {
      def addCookie(cookie: Cookie) {
      }

      def getCookies: List[Cookie] = {
        emptyList
      }

      def clearExpired(date: Date): Boolean = {
        false
      }

      def clear() {
      }

      private[network] var emptyList: ArrayList[Cookie] = new ArrayList[Cookie]
    }
    
    httpParams.setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY)
    httpParams.setParameter("http.User-Agent", "Mozilla/5.0 (X11; U; Linux x86_64; de; rv:1.9.2.8) Gecko/20100723 Ubuntu/10.04 (lucid) Firefox/3.6.8")
    httpParams.setParameter("http.language.Accept-Language", "en-us")
    httpParams.setParameter("http.protocol.content-charset", "UTF-8")
    httpParams.setParameter("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5")
    httpParams.setParameter("Cache-Control", "max-age=0")
    httpParams.setParameter("http.connection.stalecheck", true)
    val schemeRegistry: SchemeRegistry = new SchemeRegistry
    schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory))
    schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory))
    val cm = new PoolingClientConnectionManager(schemeRegistry)
    cm.setMaxTotal(4000)
    cm.setDefaultMaxPerRoute(20)

    httpClient = new DefaultHttpClient(cm, httpParams)
    httpClient.asInstanceOf[AbstractHttpClient].setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
    httpClient.getParams.setParameter("http.connection-manager.timeout", 20000L) // timeout for retrieving a connection from the pool
    httpClient.getParams.setParameter("http.protocol.wait-for-continue", 5000L)  // timeout for how long the client waits for 100-continue before sending request body
    httpClient.getParams.setParameter("http.tcp.nodelay", true)

    // First check proxy configured from java properties, otherwise use env var if set
    if (scala.sys.props.isDefinedAt("http.proxyHost")) {
      val host = scala.sys.props.getOrElse("http.proxyHost", "")
      val port = scala.sys.props.getOrElse("http.proxyPort", "80").toInt
      httpClient.getParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(host, port))
    } else if (sys.env.isDefinedAt("http_proxy")) {
      val url  = new URL(sys.env.getOrElse("http_proxy", ""))
      val host = url.getHost
      val port = url.getPort
      httpClient.getParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost(host, port))
    }
    
    // http://hc.apache.org/httpcomponents-client-ga/httpclient/examples/org/apache/http/examples/client/ClientGZipContentCompression.java
    httpClient.asInstanceOf[AbstractHttpClient].addRequestInterceptor(new HttpRequestInterceptor() {
      def process( request: HttpRequest, context: HttpContext) {
        if (!request.containsHeader("Accept-Encoding")) {
          request.addHeader("Accept-Encoding", "gzip")
        }
      }
    })

    httpClient.asInstanceOf[AbstractHttpClient].addResponseInterceptor(new HttpResponseInterceptor() {
      def process( response: HttpResponse, context: HttpContext) {
        val entity: HttpEntity = response.getEntity()
        if (entity != null) {
          val ceheader: Header = entity.getContentEncoding()
          if (ceheader != null) {
            val codecs = ceheader.getElements()
            for ( c <- codecs) {
              if (c.getName().equalsIgnoreCase("gzip")) {
                response.setEntity(
                  new GzipDecompressingEntity(response.getEntity()))
                return
              }
            }
          }
        }
      }
    })

    httpClient.asInstanceOf[AbstractHttpClient].setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
      def getKeepAliveDuration( response: HttpResponse, context: HttpContext): Long = {
        // Honor 'keep-alive' header
        val it: HeaderElementIterator = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE))

        while (it.hasNext()) {
          val he: HeaderElement = it.nextElement()
          val param: String = he.getName()
          val value: String = he.getValue()
          if (value != null && param.equalsIgnoreCase("timeout")) {
            try {
              return value.toLong * 1000
            } catch {
              case e: NumberFormatException => {} // ignore numberformat errors 
            }
          }
        }

        // otherwise keep alive for 10 seconds
        return 10 * 1000
      }
    })

  }

  /**
   * reads bytes off the string and returns a string
   *
   * @param is the source stream from the response
   * @param maxBytes The max bytes that we want to read from the input stream
   * @return String
   */
  def convertStreamToString(is: InputStream, httpEncodingType: String): String = {
    try {
      var buf : Array[Byte] = IOUtils.toByteArray(is)
      var finalEncodingType = detectEncoding(buf, httpEncodingType)
      return new String(buf, finalEncodingType)
    }
    catch {
      case e: SocketTimeoutException => {
        logger.warn(e.toString + " " + e.getMessage)
      }
      case e: UnsupportedEncodingException => {
        logger.warn(e.toString + " " + e.getMessage)
      }
      case e: IOException => {
        logger.warn(e.toString + " " + e.getMessage)
      }
    }
    null
  }
  
  def detectEncoding(buf : Array[Byte], httpEncodingType : String) : String = {
    var detector : UniversalDetector = new UniversalDetector(null)
    detector.handleData(buf, 0, buf.size)
    detector.dataEnd()
    
    var detectedEncoding = detector.getDetectedCharset()
    
    if(detectedEncoding != null) {
    	return detectedEncoding
    } else {
    	return httpEncodingType
    }
  }
}


