/**
Copyright [2014] Robby Pond

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.gravity.goose.opengraph;

import scala.collection.mutable.Set
import com.github.nscala_time.time.Imports._

case class OpenGraphData() {
  var title: String = ""
  var siteName: String = ""
  var url: String = ""
  var description: String = ""
  var image: String = ""
  var ogType: String = ""
  var locale: String = ""
  var author: String = ""
  var publisher: String = ""
  var publishedTime : DateTime = null
  var modifiedTime : DateTime = null
  var tags : Set[String] = Set()
  var section : String = ""
}
