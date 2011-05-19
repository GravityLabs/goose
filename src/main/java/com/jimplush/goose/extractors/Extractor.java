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
package com.jimplush.goose.extractors;

import org.jsoup.nodes.Element;

/**
 * Created by IntelliJ IDEA.
 * User: robbie
 * Date: 5/19/11
 * Time: 2:45 PM
 */

/**
 * Encapsulates the process of extracting some type <code>T</code> from an article
 * @param <T> the type of {@link Object} the implementing class will return
 */
public interface Extractor<T> {
  /**
   * Given the specified {@link Element}, extract @param <T>
   *
   * @param rootElement passed in from the {@link com.jimplush.goose.ContentExtractor} after the article has been parsed
   * @return an instance of type <code>T</code>
   */
  T extract(Element rootElement);
}


