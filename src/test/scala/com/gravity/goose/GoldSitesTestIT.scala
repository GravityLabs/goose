package com.gravity.goose

import org.junit.Test

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/16/11
 * This class hits live websites and is only run manually, not part of the tests lifecycle
 */
class GoldSitesTestIT {

  @Test
  def techCrunch() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url = "http://techcrunch.com/2011/08/13/2005-zuckerberg-didnt-want-to-take-over-the-world/"
    val content = "The Huffington Post has come across this fascinating five-minute interview"
    val image = "http://tctechcrunch2011.files.wordpress.com/2011/08/screen-shot-2011-08-13-at-4-55-35-pm.png?w=288"
    val title = "2005 Zuckerberg Didn’t Want To Take Over The World"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedImage = image, expectedStart = content)
  }


  @Test
  def cnn() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url = "http://www.cnn.com/2010/POLITICS/08/13/democrats.social.security/index.html"
    val article = TestUtils.getArticle(url)
    val title = "Democrats to use Social Security against GOP this fall"
    val content = "Washington (CNN) -- Democrats pledged "
    val image = "http://i.cdn.turner.com/cnn/2010/POLITICS/08/13/democrats.social.security/story.kaine.gi.jpg"
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedStart = content, expectedImage = image)
  }

  @Test
  def businessWeek: Unit = {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.businessweek.com/magazine/content/10_34/b4192066630779.htm"
    val article: Article = TestUtils.getArticle(url)
    val title = "Olivia Munn: Queen of the Uncool"
    val content = "Six years ago, Olivia Munn arrived in Hollywood with fading ambitions of making it as a sports reporter and set about deploying"
    val image = "http://images.businessweek.com/mz/10/34/370/1034_mz_66popmunnessa.jpg"
    TestUtils.runArticleAssertions(article = article, expectedTitle = title, expectedStart = content, expectedImage = image)
  }


  @Test
  def businessWeek2() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.businessweek.com/magazine/content/10_34/b4192048613870.htm"
    val article: Article = TestUtils.getArticle(url)
    val content = "There's discord on Wall Street: Strategists at major American investment banks see a"
    val image = "http://images.businessweek.com/mz/covers/current_120x160.jpg"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)

  }

  @Test
  def desertNews() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url = "http://www.deseretnews.com/article/705388385/High-school-basketball-Top-Utah-prospects-representing-well.html"
    val article = TestUtils.getArticle(url)
    val content = "In going up against some of the top AAU basketball teams in"
    TestUtils.runArticleAssertions(article, expectedStart = content)

  }

  @Test
  def foxNews() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.foxnews.com/politics/2010/08/14/russias-nuclear-help-iran-stirs-questions-improved-relations/"
    val article = TestUtils.getArticle(url)
    val content = "Russia's announcement that it will help Iran get nuclear fuel is raising questions"
    val image = "http://a57.foxnews.com/static/managed/img/Politics/396/223/startsign.jpg"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)

  }

  @Test
  def aolNews() {

    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.aolnews.com/nation/article/the-few-the-proud-the-marines-getting-a-makeover/19592478"
    val article = TestUtils.getArticle(url)
    val content = "WASHINGTON (Aug. 13) -- Declaring \"the maritime soul of the Marine Corps\" is"
    val image = "http://o.aolcdn.com/photo-hub/news_gallery/6/8/680919/1281734929876.JPEG"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)

  }

  @Test
  def wallStreetJournal() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://online.wsj.com/article/SB10001424052748704532204575397061414483040.html"
    val article = TestUtils.getArticle(url)
    val content = "The Obama administration has paid out less than a third of the nearly $230 billion"
    val image = "http://si.wsj.net/public/resources/images/OB-JO747_stimul_G_20100814113803.jpg"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)
  }

  @Test
  def usaToday() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://content.usatoday.com/communities/thehuddle/post/2010/08/brett-favre-practices-set-to-speak-about-return-to-minnesota-vikings/1"
    val article = TestUtils.getArticle(url)
    val content = "Brett Favre says he couldn't give up on one more chance"
    val image = "http://i.usatoday.net/communitymanager/_photos/the-huddle/2010/08/18/favrespeaksx-inset-community.jpg"
    TestUtils.runArticleAssertions(article = article, expectedStart = content, expectedImage = image)
  }

  @Test
  def usaToday2() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://content.usatoday.com/communities/driveon/post/2010/08/gm-finally-files-for-ipo/1"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "General Motors just filed with the Securities and Exchange ",
      expectedImage = "http://i.usatoday.net/communitymanager/_photos/drive-on/2010/08/18/cruzex-wide-community.jpg")
  }


  @Test
  def espn() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://sports.espn.go.com/espn/commentary/news/story?id=5461430"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "If you believe what college football coaches have said about sports",
      expectedImage = "http://a.espncdn.com/photo/2010/0813/ncf_i_mpouncey1_300.jpg")
  }

  @Test
  def washingtonpost() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.washingtonpost.com/wp-dyn/content/article/2010/12/08/AR2010120803185.html"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "The Supreme Court sounded ",
      expectedImage = "http://media3.washingtonpost.com/wp-dyn/content/photo/2010/10/09/PH2010100904575.jpg")
  }

  @Test
  def gizmodo() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://gizmodo.com/5833746/what-if-the-earthquake-had-hit-manhattan"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Today's 5.9 magnitude earthquake was felt throughout the Mid-Atlantic",
      expectedImage = "http://cache.gawkerassets.com/assets/images/4/2011/08/medium_aftershock-earthquake-in-new-york-original.jpg")
  }

  @Test
  def engadget() {
    implicit val config = TestUtils.DEFAULT_CONFIG
    val url: String = "http://www.engadget.com/2010/08/18/verizon-fios-set-top-boxes-getting-a-new-hd-guide-external-stor/"
    val article = TestUtils.getArticle(url)
    TestUtils.runArticleAssertions(article = article,
      expectedStart = "Streaming and downloading TV content to mobiles is nice",
      expectedImage = "http://www.blogcdn.com/www.engadget.com/media/2010/08/44ni600.jpg")
  }


}

