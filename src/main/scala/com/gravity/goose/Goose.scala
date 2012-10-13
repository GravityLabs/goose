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

package com.gravity.goose

import network.HtmlFetcher
import java.io.File

/**
 * Created by Jim Plush - Gravity.com
 * Date: 8/14/11
 */
class Goose(config: Configuration = new Configuration) {


  initializeEnvironment()

  /**
  * Main method to extract an article object from a URL, pass in a url and get back a Article
  * @url The url that you want to extract
  */
  def extractContent(url: String, rawHTML: String): Article = {
    val cc = new CrawlCandidate(config, url, rawHTML)
    sendToActor(cc)
  }

  def extractContent(url: String): Article = {
    val cc = new CrawlCandidate(config, url, null)
    sendToActor(cc)
  }

  def shutdownNetwork() {
    HtmlFetcher.getHttpClient.getConnectionManager.shutdown()
  }

  def sendToActor(crawlCandidate: CrawlCandidate) = {
    val crawler = new Crawler(config)
    val article = crawler.crawl(crawlCandidate)
    article
  }

  def initializeEnvironment() {

    val f = new File(config.localStoragePath)
    try {
      if (!f.isDirectory) {
        f.mkdirs()
      }
    } catch {
      case e: Exception =>
    }
    if (!f.isDirectory) {
      throw new Exception(config.localStoragePath + " directory does not seem to exist, you need to set this for image processing downloads")
    }
    if (!f.canWrite) {
      throw new Exception(config.localStoragePath + " directory is not writeble, you need to set this for image processing downloads")
    }

    // todo cleanup any jank that may be in the tmp folder currently
  }

}

object Goose {

  implicit val config = new Configuration

  val logPrefix = "goose: "

  // create the crawling actor that will accept bulk crawls
//  val crawlingActor = Actor.actorOf[CrawlingActor]
//  crawlingActor.start()

}