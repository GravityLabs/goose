package com.gravity.goose

import scala.io.Source
import sys.process._

object FetchMany {
  def main(args: Array[String]) {
    try {
      val config: Configuration = new Configuration
      config.enableImageFetching = true
      config.imagemagickConvertPath = "/usr/bin/convert"
      config.imagemagickIdentifyPath = "/usr/bin/identify"
      config.localStoragePath = "/tmp/goose"
      config.minBytesForImages = 4500
      val goose = new Goose(config)

      var i = 0
      for(line <- Source.fromFile(args(0) + "urllist.txt").getLines()) {
        val out = new java.io.FileWriter(args(0) + i)
        val url: String = line
        println("FETCH: Goose is fetching into " + i + ": " + url)
        var done: Boolean = false
        for(attempt <- 1 to 5) {
          try {
            if(!done) {
              println("FETCH: -- Attempt " + attempt)
              val article = goose.extractContent(url)
              println("FETCH: -- Got: " + article.title)
              out.write(article.cleanedArticleText + "\n" + article.topImage.imageSrc + "\n" + article.title)
              done = true
            }
          }
          catch {
            case e: Exception => { println(e) }
          }
        }
        out.close
        i = i + 1
      }
    }
    catch {
      case e: Exception => {
        System.out.println(e.toString)
      }
    }
  }
}


