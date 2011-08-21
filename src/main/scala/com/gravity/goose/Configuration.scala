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

import extractors.{AdditionalDataExtractor, PublishDateExtractor}
import org.jsoup.nodes.Element
import java.util.Date


/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/16/11
 */


class Configuration {

  /**
  * this is the local storage path used to place images to inspect them, should be writable
  */
  var localStoragePath: String = "/tmp/goose"
  /**
  * What's the minimum bytes for an image we'd accept is, alot of times we want to filter out the author's little images
  * in the beginning of the article
  */
  var minBytesForImages: Int = 4500
  /**
  * set this guy to false if you don't care about getting images, otherwise you can either use the default
  * image extractor to implement the ImageExtractor interface to build your own
  */
  var enableImageFetching: Boolean = true
  /**
  * path to your imagemagick convert executable, on the mac using mac ports this is the default listed
  */
  var imagemagickConvertPath: String = "/opt/local/bin/convert"
  /**
  *  path to your imagemagick identify executable
  */
  var imagemagickIdentifyPath: String = "/opt/local/bin/identify"

  var publishDateExtractor: PublishDateExtractor = new PublishDateExtractor {
    def extract(rootElement: Element): Date = {
      null
    }
  }
  var additionalDataExtractor: AdditionalDataExtractor = new AdditionalDataExtractor

  def getPublishDateExtractor: PublishDateExtractor = {
    publishDateExtractor
  }


  /**
  * Pass in to extract article publish dates.
  * @param extractor a concrete instance of {@link PublishDateExtractor}
  * @throws IllegalArgumentException if the instance passed in is <code>null</code>
  */
  def setPublishDateExtractor(extractor: PublishDateExtractor): Unit = {
    if (extractor == null) throw new IllegalArgumentException("extractor must not be null!")
    this.publishDateExtractor = extractor
  }

  def getAdditionalDataExtractor: AdditionalDataExtractor = {
    additionalDataExtractor
  }

  /**
  * Pass in to extract any additional data not defined within {@link Article}
  * @param extractor a concrete instance of {@link AdditionalDataExtractor}
  * @throws IllegalArgumentException if the instance passed in is <code>null</code>
  */
  def setAdditionalDataExtractor(extractor: AdditionalDataExtractor): Unit = {
    this.additionalDataExtractor = extractor
  }


}