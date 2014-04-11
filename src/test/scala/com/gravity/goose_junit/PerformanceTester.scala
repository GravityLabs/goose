package com.gravity.goose

import org.apache.commons.lang.time.StopWatch
import java.io.InputStream
import org.apache.commons.io.IOUtils
import utils.FileHelper

/**
 * Created by Jim Plush
 * User: jim
 * Date: 5/13/11
 */
object PerformanceTester {

  val resDir = "/com/gravity/goose/statichtml/"
  /**
  * testing the performance of the extraction algos only
  * current best time on local macbook
  * run this 3 times and take the best time
  * 32329 ms for 100 articles v1
  * 27047 ms for 100 articles v2 (erraggy perf improvements)
  */
  def main(args: Array[String]) {

    System.out.println("testing performance of general goose extraction algos")
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    val goose = new Goose(config)
    val html = FileHelper.loadResourceFile(TestUtils.staticHtmlDir + "scribd1.txt", Goose.getClass)
    val url = "http://www.scribd.com/doc/52584146/Microfinance-and-Poverty-Reduction?in_collection=2987942"

    val clock: StopWatch = new StopWatch
    System.out.println("How long does it take to extract an article?")
    clock.start()

    for (i <- 0 to 100) {
      goose.extractContent(url, html)
    }
    clock.stop()
    System.out.println("It takes " + clock.getTime + " milliseconds")
  }


}


