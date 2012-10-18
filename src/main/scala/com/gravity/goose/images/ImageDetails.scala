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

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/18/11
 */

/**
* holds the details of the result of inspecting an image
* @author Jim Plush
*
*/
class ImageDetails {
  def getWidth: Int = width

  def setWidth(width: Int) {
    this.width = width
  }

  def getHeight: Int = height

  def setHeight(height: Int) {
    this.height = height
  }

  def getMimeType: String = mimeType

  def setMimeType(mimeType: String) {
    this.mimeType = mimeType
  }

  /**
  * the width of the image
  */
  private var width: Int = 0
  /**
  * height of the image
  */
  private var height: Int = 0
  /**
  * the mimeType of the image JPEG / PNG
  */
  private var mimeType: String = null
}