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
package com.gravity.goose.extractors

import org.jsoup.nodes.Element
import com.github.nscala_time.time.Imports._

/**
* Implement this class to extract the {@link DateTime} of when this article was published.
*/
/**
 * Created by IntelliJ IDEA.
 * User: robbie
 * Date: 5/19/11
 * Time: 2:50 PM
 */
abstract class PublishDateExtractor extends Extractor[DateTime] {
  /**
  * Intended to search the DOM and identify the {@link DateTime} of when this article was published.
  * <p>This will be called by the {@link com.jimplush.goose.ContentExtractor#extractContent(String)} method and will be passed to {@link com.jimplush.goose.Article#setPublishDate(org.joda.time.DateTime)}</p>
  *
  * @param rootElement passed in from the {@link com.jimplush.goose.ContentExtractor} after the article has been parsed
  * @return {@link DateTime} of when this particular article was published or <code>null</code> if no date could be found.
  */
  def extract(rootElement: Element): DateTime
}



