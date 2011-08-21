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
* Time: 3:53 PM
*/

import java.util.regex.Pattern

class StringSplitter {
  def this(pattern: String) {
    this ()
    this.pattern = Pattern.compile(pattern)
  }

  def split(input: String): Array[String] = {
    if (string.isNullOrEmpty(input)) return string.emptyArray
    pattern.split(input)
  }

  private var pattern: Pattern = null
}



