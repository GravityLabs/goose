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

package com.gravity.goose.text

/**
* Created by IntelliJ IDEA.
* User: robbie
* Date: 5/13/11
* Time: 12:11 AM
*/

object string {
  def isNullOrEmpty(input: String): Boolean = {
    if (input == null) return true
    if (input.length == 0) return true
    false
  }

  val empty: String = ""
  val emptyArray: Array[String] = Array[String](empty)
  var SPACE_SPLITTER: StringSplitter = new StringSplitter(" ")

  def tryToInt(input: String): Option[Int] = {
    try {
      Some(input.toInt)
    } catch {
      case _: Exception => None
    }
  }
}


