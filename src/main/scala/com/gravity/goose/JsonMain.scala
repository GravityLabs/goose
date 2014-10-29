package com.gravity.goose

import scala.collection.mutable.Map

import com.gravity.goose.util.JsonUtil

object JsonMain {

  def main(args: Array[String]) {
    try {
      val url = args(0)
      val json = getArticleAsJson(url)
      println(json)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
  def getArticleAsJson(url: String) = {
    val map = Map[String, Any]()
    println("read article from [" + url + "]")
    if (url == null || url.length == 0) {
      map.put("error", true)
      map.put("message", "No URL specified")
    } else {
      val config = new Configuration
      config.setImagemagickConvertPath("/usr/bin/convert")
      config.setImagemagickIdentifyPath("/usr/bin/identify")
      config.setLocalStoragePath("./storage")
      config.setMinBytesForImages(500)
      val goose = new Goose(config)
      val article = goose.extractContent(url)
      map.put("success", true)

      
      map.put("title", encodeHTML(article.getTitle))
      val image = article.getTopImage
      if (image != null) {
        map.put("image", article.getTopImage.getImageSrc)
      }
      map.put("images", article.getAllImages)
      map.put("movies", article.getMovies)
      map.put("link", article.getCanonicalLink)
      map.put("tags", article.getTags)
      map.put("text", encodeHTML(article.getCleanedArticleText))
      map.put("date", article.getPublishDate)
      map.put("desc", encodeHTML(article.getMetaDescription))
      map.put("keywords", article.getMetaKeywords)
    }
    val responseString = JsonUtil.toJson(map)
    println(responseString)
    responseString
  }

  def encodeHTML(s: String): String = {
    if (s == null) {
      return ""
    }
    val out = new StringBuffer()
    for (i <- 0 until s.length) {
      val c = s.charAt(i)
      if (c > 127 || c == '"' || c == '<' || c == '>') {
        out.append("&#" + c.toInt + ";")
      } else {
        out.append(c)
      }
    }
    out.toString
  }
}
