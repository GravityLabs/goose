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
import org.joda.time.format.ISODateTimeFormat

class OpenGraphDataExtractor extends Extractor[OpenGraphData] {

  def extract(rootElement: Element): OpenGraphData = {
    val openGraphData: OpenGraphData = new OpenGraphData
    val dateParser = ISODateTimeFormat.dateTimeParser
    for(el <- rootElement.select("meta")) {
      val property = el.attr("property")
      val value = el.attr("content")
      property match {
        case "og:title" => openGraphData.title = value
        case "og:site_name" => openGraphData.siteName = value
        case "og:url" => openGraphData.url = value
        case "og:description" => openGraphData.description = value
        case "og:image" => openGraphData.image = value
        case "og:type" => openGraphData.ogType = value
        case "og:locale" => openGraphData.locale = value
        case "article:author" => openGraphData.author = value
        case "article:publisher" => openGraphData.publisher = value
        case "article:section" => openGraphData.section = value
        case "article:tag" => openGraphData.tags ++= value.split(",").map(_.trim)
        case "article:published_time" => openGraphData.publishedTime = dateParser.parseDateTime(value)
        case "article:modified_time" => openGraphData.modifiedTime = dateParser.parseDateTime(value)
        case _ => ()
      }
    }
    openGraphData
  }
}
