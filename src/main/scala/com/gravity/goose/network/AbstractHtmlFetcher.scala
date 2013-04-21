package com.gravity.goose.network

import com.gravity.goose.Configuration
import org.apache.http.client.HttpClient

/**
 * Created by IntelliJ IDEA.
 * Author: Robbie Coleman
 * Date: 10/13/12
 * Time: 1:02 AM
 *
 * The workhorse of goose. Override the {@see com.gravity.goose.network.HtmlFetcher} within your configuration for complete control.
 */
trait AbstractHtmlFetcher {
  /**
   * Access the `url` over the internet and retrieve the HTML from it
   * @param config overrides and tweaks
   * @param url the address to access and retrieve content from
   * @return `Some` `String` of the response from the specified `url` or `None` if failed to retrieve HTML.
   */
  def getHtml(config: Configuration, url: String): Option[String]

  /**
   * A shared accessor for making image calls
   * @return a fully configured and initialized instance for shared use
   */
  def getHttpClient: HttpClient
}
