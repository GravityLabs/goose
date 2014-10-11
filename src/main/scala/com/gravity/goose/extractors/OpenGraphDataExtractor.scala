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
package com.gravity.goose.extractors

import org.jsoup.nodes.Element

import scala.collection.JavaConversions._
import com.gravity.goose.opengraph.OpenGraphData

class OpenGraphDataExtractor extends Extractor[OpenGraphData] {

  def extract(rootElement: Element): OpenGraphData = {
    val openGraphData: OpenGraphData = new OpenGraphData
    val elements : scala.collection.mutable.Buffer[Element] = rootElement.select("meta")
    for(el <- elements) {
      if(el.attr("property") == "og:title")
        openGraphData.title = el.attr("content")
      if(el.attr("property") == "og:site_name")
        openGraphData.siteName = el.attr("content")
      if(el.attr("property") == "og:url")
        openGraphData.url = el.attr("content")
      if(el.attr("property") == "og:description")
        openGraphData.description = el.attr("content")
      if(el.attr("property") == "og:image")
        openGraphData.image = el.attr("content")
      if(el.attr("property") == "og:type")
        openGraphData.ogType = el.attr("content")
      if(el.attr("property") == "og:locale")
        openGraphData.locale = el.attr("content")
      if(el.attr("property") == "article:author")
        openGraphData.author = el.attr("content")
      if(el.attr("property") == "article:publisher")
        openGraphData.publisher = el.attr("content")
    }
    openGraphData
  }
}