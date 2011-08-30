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

import java.security.MessageDigest

/**
* Created by Jim Plush
* User: jim
* Date: 8/14/11
*/

object HashUtils {

  def md5(s: String): String = {
    val md5 = MessageDigest.getInstance("MD5")

    md5.reset()
    md5.update(s.getBytes)

    val result = md5.digest().map(0xFF & _).map { "%02x".format(_) }.mkString

    result
  }

}