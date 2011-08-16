package com.gravity.goose

import org.junit.Test
import org.junit.Assert._
import scala.actors.Future
import scala.actors.Futures._

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/14/11
 */

class GooseTest {

  @Test
  def getLink {

    val url = "http://techcrunch.com/2011/08/13/2005-zuckerberg-didnt-want-to-take-over-the-world/"
    val goose = new Goose
    val article = goose.extractContent(url)(new Configuration)

    println("FINAL TALLY\n\n")
    println("TEXT: "+article.topNode.text())

  }

  @Test
  def futureTest {

    println("I'm outside")
    for (i <- 1 to 10) {
      val f = future {
        Thread.sleep(100)

        println("----Thread %s : oh hai".format(Thread.currentThread().getName))
      }
      f()
    }
    println("I'm done!")


  }
}