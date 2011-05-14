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
package com.jimplush.goose.network;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jim plush
 * Date: 12/16/10
 */


public class HtmlFetcher {

  private static final Logger logger = LoggerFactory.getLogger(HtmlFetcher.class);

  /**
   * holds a reference to our override cookie store, we don't want to store
   * cookies for head requests, only slows shit down
   */
  public static CookieStore emptyCookieStore;


  /**
   * holds the HttpClient object for making requests
   */
  private static HttpClient httpClient;


  static {
    initClient();
  }


  public static HttpClient getHttpClient() {
    return httpClient;
  }


  /**
   * makes an http fetch to go retreive the HTML from a url, store it to disk and pass it off
   *
   * @param url
   * @return
   * @throws MaxBytesException
   * @throws NotHtmlException
   */
  public static String getHtml(String url) throws MaxBytesException, NotHtmlException {
    HttpGet httpget = null;
    String htmlResult = null;
    HttpEntity entity = null;
    InputStream instream = null;
    try {
      HttpContext localContext = new BasicHttpContext();
      localContext.setAttribute(ClientContext.COOKIE_STORE, HtmlFetcher.emptyCookieStore);
      httpget = new HttpGet(url);

      HttpResponse response = httpClient.execute(httpget, localContext);

      entity = response.getEntity();


      if (entity != null) {
        instream = entity.getContent();

        // set the encoding type if utf-8 or otherwise
        String encodingType = "UTF-8";
        try {

          //todo encoding detection could be improved
          encodingType = EntityUtils.getContentCharSet(entity);

          if (encodingType == null) {
            encodingType = "UTF-8";
          }
        } catch (Exception e) {
          if (logger.isDebugEnabled()) {
            logger.debug("Unable to get charset for: " + url);
            logger.debug("Encoding Type is: " + encodingType);
          }
        }

        try {
          htmlResult = HtmlFetcher.convertStreamToString(instream, 15728640, encodingType).trim();
        } finally {
          entity.consumeContent();
        }


      } else {
        logger.error("Unable to fetch URL Properly: " + url);
      }

    } catch (NullPointerException e) {
      logger.warn(e.toString() + " " + e.getMessage());

    } catch (MaxBytesException e) {

      logger.error("GRVBIGFAIL: " + url + " Reached max bytes size");
      throw e;
    } catch (SocketException e) {
      logger.warn(e.getMessage());

    } catch (SocketTimeoutException e) {
      logger.error(e.toString());
    } catch (Exception e) {
      logger.error("FAILURE FOR LINK: " + url + " " + e.toString());
      return null;
    } finally {

      if (instream != null) {
        try {
          instream.close();
        } catch (Exception e) {
          logger.warn(e.getMessage());
        }
      }
      if (httpget != null) {
        try {
          httpget.abort();
          entity = null;
        } catch (Exception e) {

        }
      }

    }

    if (logger.isDebugEnabled()) {
      logger.debug("starting...");
    }
    if (htmlResult == null || htmlResult.length() < 1) {
      if (logger.isDebugEnabled()) {
        logger.debug("HTMLRESULT is empty or null");
      }
      throw new NotHtmlException();
    }


    InputStream is;
    String mimeType = null;
    try {
      is = new ByteArrayInputStream(htmlResult.getBytes("UTF-8"));

      mimeType = URLConnection.guessContentTypeFromStream(is);

      if (mimeType != null) {

        if (mimeType.equals("text/html") == true || mimeType.equals("application/xml") == true) {
          return htmlResult;
        } else {
          if (htmlResult.contains("<title>") == true && htmlResult.contains("<p>") == true) {
            return htmlResult;
          }

          logger.error("GRVBIGFAIL: " + mimeType + " - " + url);
          throw new NotHtmlException();
        }

      } else {
        throw new NotHtmlException();
      }


    } catch (UnsupportedEncodingException e) {
      logger.warn(e.getMessage());

    } catch (IOException e) {
      logger.warn(e.getMessage());
    }

    return htmlResult;
  }


  private static void initClient() {
    if (logger.isDebugEnabled()) {
      logger.debug("Initializing HttpClient");
    }
    HttpParams httpParams = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
    HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);
    ConnManagerParams.setMaxTotalConnections(httpParams, 20000);

    ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRoute() {
      public int getMaxForRoute(HttpRoute route) {
        return 500;
      }
    });

    HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);


    /**
     * we don't want anything to do with cookies at this time
     */
    emptyCookieStore = new CookieStore() {

      public void addCookie(Cookie cookie) {

      }

      ArrayList<Cookie> emptyList = new ArrayList<Cookie>();

      public List<Cookie> getCookies() {
        return emptyList;
      }

      public boolean clearExpired(Date date) {
        return false;
      }

      public void clear() {

      }
    };

    // set request params
    httpParams.setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
    httpParams.setParameter("http.User-Agent", "Mozilla/5.0 (X11; U; Linux x86_64; de; rv:1.9.2.8) Gecko/20100723 Ubuntu/10.04 (lucid) Firefox/3.6.8");
    httpParams.setParameter("http.language.Accept-Language", "en-us");
    httpParams.setParameter("http.protocol.content-charset", "UTF-8");
    httpParams.setParameter("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
    httpParams.setParameter("Cache-Control", "max-age=0");
    httpParams.setParameter("http.connection.stalecheck", false); // turn off stale check checking for performance reasons


    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

    final ClientConnectionManager cm = new ThreadSafeClientConnManager(httpParams, schemeRegistry);

    httpClient = new DefaultHttpClient(cm, httpParams);

    httpClient.getParams().setParameter("http.conn-manager.timeout", 120000L);
    httpClient.getParams().setParameter("http.protocol.wait-for-continue", 10000L);
    httpClient.getParams().setParameter("http.tcp.nodelay", true);
  }

  /**
   * reads bytes off the string and returns a string
   *
   * @param is
   * @param maxBytes The max bytes that we want to read from the input stream
   * @return String
   */
  public static String convertStreamToString(InputStream is, int maxBytes, String encodingType) throws MaxBytesException {

    char[] buf = new char[2048];
    Reader r = null;
    try {
      r = new InputStreamReader(is, encodingType);
      StringBuilder s = new StringBuilder();
      int bytesRead = 2048;
      while (true) {

        if (bytesRead >= maxBytes) {
          throw new MaxBytesException();
        }

        int n = r.read(buf);
        bytesRead += 2048;
        if (n < 0)
          break;
        s.append(buf, 0, n);
      }

      return s.toString();

    } catch (SocketTimeoutException e) {
      logger.warn(e.toString() + " " + e.getMessage());
    } catch (UnsupportedEncodingException e) {
      logger.warn(e.toString() + " Encoding: " + encodingType);

    } catch (IOException e) {
      logger.warn(e.toString() + " " + e.getMessage());
    } finally {
      if (r != null) {
        try {
          r.close();
        } catch (Exception e) {
        }
      }
    }
    return null;

  }
}
