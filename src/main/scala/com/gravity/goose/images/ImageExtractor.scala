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
package com.gravity.goose.images

import org.jsoup.nodes.{Element, Document}
import com.gravity.goose.utils.{Logging, CanLog}

/**
* Created by Jim Plush
* User: jim
* Date: 8/18/11
*/

// represents a file stored on disk that we've downloaded
case class LocallyStoredImage(imgSrc: String, localFileName: String, linkhash: String, bytes: Long, fileExtension: String = "", height: Int = 0, width: Int = 0)

trait ImageExtractor extends CanLog {

  def getBestImage(doc: Document, topNode: Element): Image

  def logPrefix: String = ImageExtractor.loggingPrefix

  def critical(msg: String, refs: Any*) {
    ImageExtractor.critical(msg, refs: _*)
  }

  def critical(t: Throwable, msg: String, refs: Any*) {
    ImageExtractor.critical(t, msg, refs: _*)
  }

  def debug(msg: String, refs: Any*) {
    ImageExtractor.debug(msg, refs: _*)
  }

  def debug(t: Throwable, msg: String, refs: Any*) {
    ImageExtractor.debug(t, msg, refs: _*)
  }

  def info(msg: String, refs: Any*) {
    ImageExtractor.info(msg, refs: _*)
  }

  def info(t: Throwable, msg: String, refs: Any*) {
    ImageExtractor.info(t, msg, refs: _*)
  }

  def logger = ImageExtractor.logger

  def trace(msg: String, refs: Any*) {
    ImageExtractor.trace(msg, refs: _*)
  }

  def trace(t: Throwable, msg: String, refs: Any*) {
    ImageExtractor.trace(t, msg, refs: _*)
  }

  def warn(msg: String, refs: Any*) {
    ImageExtractor.warn(msg, refs: _*)
  }

  def warn(t: Throwable, msg: String, refs: Any*) {
    ImageExtractor.warn(t, msg, refs: _*)
  }
}

object ImageExtractor extends Logging {
  val loggingPrefix = "images: "
}

