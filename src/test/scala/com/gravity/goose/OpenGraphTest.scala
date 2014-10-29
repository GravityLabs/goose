package com.gravity.goose

import org.junit.Test
import org.junit.Assert._

class OpenGraphTest {

  @Test
  def openGraph() {
    implicit val config = TestUtils.NO_IMAGE_CONFIG
    // og tags for http://www.telegraph.co.uk/foodanddrink/foodanddrinknews/8808120/Worlds-hottest-chilli-contest-leaves-two-in-hospital.html
    /*
<meta property="og:title" content="World's hottest chilli contest leaves two in hospital - Telegraph" />
<meta property="og:url" content="http://www.telegraph.co.uk/foodanddrink/foodanddrinknews/8808120/Worlds-hottest-chilli-contest-leaves-two-in-hospital.html" />

<meta property="og:image" content="http://i.telegraph.co.uk/multimedia/archive/02018/Kismot-Killer_2018476a.jpg" />
<meta property="og:type" content="article" />
     */
    val url: String = "http://www.telegraph.co.uk/foodanddrink/foodanddrinknews/8808120/Worlds-hottest-chilli-contest-leaves-two-in-hospital.html"
    val article = TestUtils.getArticle(url)
    assertEquals("og:description was not as expected!", article.openGraphData.description,
      "A 'world's hottest chilli' competition at a curry restaurant left two people   in hospital.")
    assertEquals("og:title was not as expected!", article.openGraphData.title,
      "World's hottest chilli contest leaves two in hospital - Telegraph")
    assertEquals("og:url was not as expected!", article.openGraphData.url,
      "http://www.telegraph.co.uk/foodanddrink/foodanddrinknews/8808120/Worlds-hottest-chilli-contest-leaves-two-in-hospital.html")
    assertEquals("og:image was not as expected!", article.openGraphData.image,
      "http://i.telegraph.co.uk/multimedia/archive/02018/Kismot-Killer_2018476a.jpg")
    assertEquals("og:type was not as expected!", article.openGraphData.ogType,
      "article")
  }
}