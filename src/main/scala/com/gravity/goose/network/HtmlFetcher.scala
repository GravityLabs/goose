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

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.HttpVersion
import org.apache.http.client.CookieStore
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.params.CookiePolicy
import org.apache.http.client.protocol.ClientContext
import org.apache.http.conn.ClientConnectionManager
import org.apache.http.conn.params.ConnManagerParams
import org.apache.http.conn.params.ConnPerRoute
import org.apache.http.conn.routing.HttpRoute
import org.apache.http.conn.scheme.PlainSocketFactory
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.scheme.SchemeRegistry
import org.apache.http.cookie.Cookie
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager
import org.apache.http.params.BasicHttpParams
import org.apache.http.params.HttpConnectionParams
import org.apache.http.params.HttpParams
import org.apache.http.params.HttpProtocolParams
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.protocol.HttpContext
import org.apache.http.util.EntityUtils
import java.io._
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.URLConnection
import java.util.ArrayList
import java.util.Date
import java.util.List
import com.gravity.goose.utils.Logging
import com.gravity.goose.Configuration
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
object HtmlFetcher extends Logging {
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
   * makes an http fetch to go retreive the HTML from a url, store it to disk and pass it off
   *
   * @param url
   * @return
   * @throws MaxBytesException
   * @throws NotHtmlException
   */
  def getHtml(config: Configuration, url: String): Option[String] = {
    var httpget: HttpGet = null
    var htmlResult: String = null
    var entity: HttpEntity = null
    var instream: InputStream = null

    // Identified the the apache http client does not drop URL fragments before opening the request to the host
    // more info: http://stackoverflow.com/questions/4251841/400-error-with-httpclient-for-a-link-with-an-anchor
    val cleanUrl = {
      val foundAt = url.indexOf("#")
      if (foundAt >= 0) url.substring(0, foundAt) else url
    }

    try {
      val localContext: HttpContext = new BasicHttpContext
      localContext.setAttribute(ClientContext.COOKIE_STORE, HtmlFetcher.emptyCookieStore)
      httpget = new HttpGet(cleanUrl)
      HttpProtocolParams.setUserAgent(httpClient.getParams, config.getBrowserUserAgent());

      trace("Setting UserAgent To: " + HttpProtocolParams.getUserAgent(httpClient.getParams))
      val response: HttpResponse = httpClient.execute(httpget, localContext)

      // TODO: Only continue with a 200 status code and handle all other cases
      if (response.getStatusLine.getStatusCode == 404) throw new NotFoundException

      entity = response.getEntity
      if (entity != null) {
        instream = entity.getContent
        var encodingType: String = "UTF-8"
        try {
          encodingType = EntityUtils.getContentCharSet(entity)
          if (encodingType == null) {
            encodingType = "UTF-8"
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
          entity.consumeContent()
        }
      }
      else {
        trace("Unable to fetch URL Properly: " + cleanUrl)
      }
    }
    catch {
      case e: NullPointerException => {
        logger.warn(e.toString + " " + e.getMessage)
      }
      case e: MaxBytesException => {
        trace("GRVBIGFAIL: " + cleanUrl + " Reached max bytes size")
        throw e
      }
      case e: SocketException => {
        logger.warn(e.getMessage)
      }
      case e: SocketTimeoutException => {
        trace(e.toString)
      }
      case e: NotFoundException => {
        logger.warn("SERVER RETURNED 404 FOR LINK: " + cleanUrl)
        return None
      }
      case e: Exception => {
        trace("FAILURE FOR LINK: " + cleanUrl + " " + e.toString)
        return None
      }
    }
    finally {
      if (instream != null) {
        try {
          instream.close()
        }
        catch {
          case e: Exception => {
            logger.warn(e.getMessage)
          }
        }
      }
      if (httpget != null) {
        try {
          httpget.abort()
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
      throw new NotHtmlException
    }
    var is: InputStream = null
    var mimeType: String = null
    try {
      is = new ByteArrayInputStream(htmlResult.getBytes("UTF-8"))
      mimeType = URLConnection.guessContentTypeFromStream(is)
      if (mimeType != null) {
        if ((mimeType == "text/html") == true || (mimeType == "application/xml") == true) {
          return Some(htmlResult)
        }
        else {
          if (htmlResult.contains("<title>") == true && htmlResult.contains("<p>") == true) {
            return Some(htmlResult)
          }
          trace("GRVBIGFAIL: " + mimeType + " - " + cleanUrl)
          throw new NotHtmlException
        }
      }
      else {
        throw new NotHtmlException
      }
    }
    catch {
      case e: UnsupportedEncodingException => {
        logger.warn(e.getMessage)
      }
      case e: IOException => {
        logger.warn(e.getMessage)
      }
    }
    None
  }

  private def initClient() {

    trace("Initializing HttpClient")

    val httpParams: HttpParams = new BasicHttpParams
    HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000)
    HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000)
    ConnManagerParams.setMaxTotalConnections(httpParams, 20000)
    ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRoute {
      def getMaxForRoute(route: HttpRoute): Int = {
        500
      }
    })
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
    httpParams.setParameter("http.connection.stalecheck", false)
    val schemeRegistry: SchemeRegistry = new SchemeRegistry
    schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory, 80))
    schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory, 443))
    val cm: ClientConnectionManager = new ThreadSafeClientConnManager(httpParams, schemeRegistry)
    httpClient = new DefaultHttpClient(cm, httpParams)
    httpClient.getParams.setParameter("http.conn-manager.timeout", 120000L)
    httpClient.getParams.setParameter("http.protocol.wait-for-continue", 10000L)
    httpClient.getParams.setParameter("http.tcp.nodelay", true)
  }

  /**
   * reads bytes off the string and returns a string
   *
   * @param is
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


