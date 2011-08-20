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
  def businessWeek2 {
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
    TestUtils.runArticleAssertions(article=article, expectedStart = content, expectedImage=image)

  }


}

