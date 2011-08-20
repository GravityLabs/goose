package com.gravity.goose.images

import org.jsoup.select.Elements
import org.apache.http.client.methods.HttpHead
import org.apache.http.protocol.{BasicHttpContext, HttpContext}
import org.apache.http.client.protocol.ClientContext
import org.apache.http.{Header, HttpEntity, HttpResponse}
import org.apache.http.client.HttpClient
import org.jsoup.nodes.{Element, Document}
import com.gravity.goose.{Article, Configuration}
import java.util.ArrayList
import collection.mutable.HashMap
import scala.collection.JavaConversions._
import com.gravity.goose.text.{string, HashUtils}
import java.net.{MalformedURLException, URL}
import com.gravity.goose.network.HtmlFetcher
import java.io.{IOException, File}
import java.util.regex.{Pattern, Matcher}

/**
* Created by Jim Plush
* User: jim
* Date: 8/18/11
*/


/**
  * This image extractor will attempt to find the best image nearest the article.
  * Unfortunately this is a slow process since we're actually downloading the image itself
  * to inspect it's actual height/width and area metrics since most of the time these aren't
  * in the image tags themselves or can be falsified.
  * We'll weight the images in descending order depending on how high up they are compared to the top node content
  *
  * //todo this is a straight java to scala conversion, need to add the nicities of scala, all these null checks make me dizzy
  */
class StandardImageExtractor(httpClient: HttpClient, article: Article, config: Configuration) extends ImageExtractor {


  /**
  * holds the document that we're extracting the image from
  */
  private var doc: Document = null

  /**
  * What's the minimum bytes for an image we'd accept is
  */
  private var minBytesForImages: Int = 0
  /**
  * location to store temporary image files if need be
  */
  private var tempStoragePath: String = null

  /**
  * this lists all the known bad button names that we have
  */
  var matchBadImageNames: Matcher = null
  val NODE_ID_FORMAT: String = "tag: %s class: %s ID: %s"
  val KNOWN_IMG_DOM_NAMES = "yn-story-related-media" :: "cnn_strylccimg300cntr" :: "big_photo" :: Nil

  var sb: StringBuilder = new StringBuilder
  // create negative elements
  sb.append(".html|.gif|.ico|button|twitter.jpg|facebook.jpg|digg.jpg|digg.png|delicious.png|facebook.png|reddit.jpg|doubleclick|diggthis|diggThis|adserver|/ads/|ec.atdmt.com")
  sb.append("|mediaplex.com|adsatt|view.atdmt")
  matchBadImageNames = Pattern.compile(sb.toString()).matcher(string.empty)

  /**
  * holds the result of our image extraction
  */
  val image = new Image
  /**
  * the webpage url that we're extracting content from
  */
  val targetUrl = article.finalUrl
  /**
  * stores a hash of our url for reference and image processing
  */
  val linkhash = article.linkhash


  override def getBestImage(doc: Document, topNode: Element): Image = {
    this.doc = doc
    if (logger.isDebugEnabled) {
      logger.debug("Starting to Look for the Most Relavent Image")
    }
    if (image.getImageSrc == "") {
      this.checkForKnownElements()
    }
    if (image.getImageSrc == "") {
      this.checkForLargeImages(topNode, 0, 0)
    }
    if (image.getImageSrc == "") {
      this.checkForMetaTag
    }
    image
  }

  private def checkForMetaTag: Boolean = {
    if (this.checkForLinkTag) {
      return true
    }
    if (this.checkForOpenGraphTag) {
      return true
    }
    if (logger.isDebugEnabled) {
      logger.debug("unable to find meta image")
    }
    false
  }

  /**
  * checks to see if we were able to find open graph tags on this page
  *
  * @return
  */
  private def checkForOpenGraphTag: Boolean = {
    try {
      val meta: Elements = doc.select("meta[property~=og:image]")
      import scala.collection.JavaConversions._
      for (item <- meta) {
        if (item.attr("content").length < 1) {
          return false
        }
        var imagePath: String = this.buildImagePath(item.attr("content"))
        this.image.imageSrc = imagePath
        this.image.imageExtractionType = "opengraph"
        this.image.confidenceScore = 100
        this.image.bytes = this.getBytesForImage(imagePath)
        trace(logPrefix + "open graph tag found, using it")

        return true
      }
      return false
    }
    catch {
      case e: Exception => {
        e.printStackTrace
        return false
      }
    }
  }

  /**
  * checks to see if we were able to find open graph tags on this page
  *
  * @return
  */
  private def checkForLinkTag: Boolean = {
    try {
      var meta: Elements = doc.select("link[rel~=image_src]")
      import scala.collection.JavaConversions._
      for (item <- meta) {
        if (item.attr("href").length < 1) {
          return false
        }
        this.image.imageSrc = this.buildImagePath(item.attr("href"))
        this.image.imageExtractionType = "linktag"
        this.image.confidenceScore = 100
        this.image.bytes = getBytesForImage(buildImagePath(item.attr("href")))
        trace(logPrefix + "link tag found, using it")

        return true
      }
      false
    }
    catch {
      case e: Exception => {
        logger.error(e.toString, e)
        false
      }
    }
  }

  def getAllImages: ArrayList[Element] = {
    null
  }

  /**
  * although slow the best way to determine the best image is to download them and check the actual dimensions of the image when on disk
  * so we'll go through a phased approach...
  * 1. get a list of ALL images from the parent node
  * 2. filter out any bad image names that we know of (gifs, ads, etc..)
  * 3. do a head request on each file to make sure it meets our bare requirements
  * 4. any images left over let's do a full GET request, download em to disk and check their dimensions
  * 5. Score images based on different factors like height/width and possibly things like color density
  *
  * @param node
  */
  private def checkForLargeImages(node: Element, parentDepthLevel: Int, siblingDepthLevel: Int) {
    var siblingDepth = siblingDepthLevel
    var parentDepth = parentDepthLevel
    if (node == null) return
    val images: Elements = node.select("img")
    val nodeId: String = this.getNodeIds(node)
    if (logger.isDebugEnabled) {
      logger.debug("checkForLargeImages: Checking for large images, found: " + images.size + " - parent depth: " + parentDepth + " sibling depth: " + siblingDepth + " for node: " + nodeId)
    }
    var goodImages: ArrayList[Element] = null
    goodImages = this.filterBadNames(images)
    if (logger.isDebugEnabled) {
      logger.debug("checkForLargeImages: After filterBadNames we have: " + goodImages.size)
    }
    if (logger.isDebugEnabled) {
      goodImages = findImagesThatPassByteSizeTest(goodImages)
    }
    if (logger.isDebugEnabled) {
      logger.debug("checkForLargeImages: After findImagesThatPassByteSizeTest we have: " + goodImages.size)
    }
    // todo re-fix
    val imageResults: HashMap[Element, Float] = downloadImagesAndGetResults(goodImages, parentDepth)
    //    val imageResults: HashMap[Element, Float] = new HashMap[Element, Float].empty
    var highScoreImage: Element = null

    imageResults.foreach {
      case (key, value) =>
        if (highScoreImage == null) {
          highScoreImage = key
        }
        if (value > imageResults.get(highScoreImage).get) {
          highScoreImage = key
        }
    }

    if (highScoreImage != null) {
      val f: File = new File(highScoreImage.attr("tempImagePath"))
      this.image.topImageNode = highScoreImage
      this.image.imageSrc = this.buildImagePath(highScoreImage.attr("src"))
      this.image.imageExtractionType = "bigimage"
      this.image.bytes = f.length.asInstanceOf[Int]
      if (imageResults.size > 0) {
        this.image.confidenceScore = (100 / imageResults.size)
      }
      else {
        this.image.confidenceScore = 0
      }
      trace(logPrefix + "High Score Image is: " + this.buildImagePath(highScoreImage.attr("src")))
    }
    else {
      if (logger.isDebugEnabled) {
        logger.debug("unable to find a large image, going to fall back modez. depth: " + parentDepth)
      }
      if (parentDepth < 2) {
        val prevSibling: Element = node.previousElementSibling
        if (prevSibling != null) {
          if (logger.isDebugEnabled) {
            logger.debug("About to do a check against the sibling element, tagname: '" + prevSibling.tagName + "' class: '" + prevSibling.attr("class") + "' id: '" + prevSibling.id + "'")
          }
          ({
            siblingDepth += 1;
            siblingDepth
          })
          this.checkForLargeImages(prevSibling, parentDepth, siblingDepth)
        }
        else {
          if (logger.isDebugEnabled) {
            logger.debug("no more sibling nodes found, time to roll up to parent node")
          }
          ({
            parentDepth += 1;
            parentDepth
          })
          this.checkForLargeImages(node.parent, parentDepth, siblingDepth)
        }
      }
    }
  }

  /**
  * loop through all the images and find the ones that have the best bytez to even make them a candidate
  *
  * @param images
  * @return
  */
  private def findImagesThatPassByteSizeTest(images: ArrayList[Element]): ArrayList[Element] = {
    var cnt: Int = 0
    var goodImages: ArrayList[Element] = new ArrayList[Element]
    import scala.collection.JavaConversions._
    for (image <- images) {
      if (cnt > 30) {
        if (logger.isDebugEnabled) {
          logger.debug("Abort! they have over 30 images near the top node: " + this.doc.baseUri)
        }
        return goodImages
      }
      var bytes: Int = this.getBytesForImage(image.attr("src"))
      if ((bytes == 0 || bytes > this.minBytesForImages) && bytes < 15728640) {
        if (logger.isDebugEnabled) {
          logger.debug("findImagesThatPassByteSizeTest: Found potential image - size: " + bytes + " src: " + image.attr("src"))
        }
        goodImages.add(image)
      }
      else {
        image.remove
      }
      ({
        cnt += 1;
        cnt
      })
    }
    return goodImages
  }

  /**
  * takes a list of image elements and filters out the ones with bad names
  *
  * @param images
  * @return
  */
  private def filterBadNames(images: Elements): ArrayList[Element] = {
    var goodImages: ArrayList[Element] = new ArrayList[Element]
    import scala.collection.JavaConversions._
    for (image <- images) {
      if (this.isOkImageFileName(image)) {
        goodImages.add(image)
      }
      else {
        image.remove
      }
    }
    return goodImages
  }

  /**
  * will check the image src against a list of bad image files we know of like buttons, etc...
  *
  * @return
  */
  private def isOkImageFileName(imageNode: Element): Boolean = {
    var imgSrc: String = imageNode.attr("src")
    if (string.isNullOrEmpty(imgSrc)) {
      return false
    }
    matchBadImageNames.reset(imgSrc)
    if (matchBadImageNames.find) {
      if (logger.isDebugEnabled) {
        logger.debug("Found bad filename for image: " + imgSrc)
      }
      return false
    }
    true
  }

  /**
  * returns a string with debug info about this node
  *
  * @param node
  * @return
  */
  private def getNodeIds(node: Element): String = {
    val stuff: String = String.format(NODE_ID_FORMAT, node.tagName, node.className, node.id)
    stuff
  }

  /**
  * in here we check for known image contains from sites we've checked out like yahoo, techcrunch, etc... that have
  * known  places to look for good images.
  * //todo enable this to use a series of settings files so people can define what the image ids/classes are on specific sites
  */
  def checkForKnownElements(): Unit = {

    var knownImage: Element = null
    trace(logPrefix + "Checking for known images from large sites")

    for (knownName <- KNOWN_IMG_DOM_NAMES) {
      println("NAMEZ: " + knownName)
      try {
        var known: Element = article.rawDoc.getElementById(knownName)
        if (known == null) {
          known = article.rawDoc.getElementsByClass(knownName).first
        }
        if (known != null) {
          var mainImage: Element = known.getElementsByTag("img").first
          if (mainImage != null) {
            knownImage = mainImage
            if (logger.isDebugEnabled) {
              logger.debug("Got Image: " + mainImage.attr("src"))
            }
          }
        }

      }
      catch {
        case e: NullPointerException => {
          if (logger.isDebugEnabled) {
            logger.debug(e.toString, e)
          }
        }
      }
    }
    if (knownImage != null) {
      val knownImgSrc: String = knownImage.attr("src")
      this.image.imageSrc = this.buildImagePath(knownImgSrc)
      this.image.imageExtractionType = "known"
      this.image.confidenceScore = 90
      this.image.bytes = this.getBytesForImage(knownImgSrc)
    }
    else {
      if (logger.isDebugEnabled) {
        logger.debug("No known images found")
      }
    }

  }

  /**
  * This method will take an image path and build out the absolute path to that image
  * using the initial url we crawled so we can find a link to the image if they use relative urls like ../myimage.jpg
  *
  * @param image
  * @return
  */
  private def buildImagePath(image: String): String = {
    var pageURL: URL = null
    var newImage: String = image.replace(" ", "%20")
    try {
      pageURL = new URL(this.targetUrl)
      var imageURL: URL = new URL(pageURL, image)
      newImage = imageURL.toString
    }
    catch {
      case e: MalformedURLException => {
        logger.error("Unable to get Image Path: " + image)
      }
    }
    newImage
  }

  /**
  * does the HTTP HEAD request to get the image bytes for this images
  *
  * @param src
  * @return
  */
  private def getBytesForImage(src: String): Int = {
    var bytes: Int = 0
    var httpget: HttpHead = null
    try {
      var link: String = this.buildImagePath(src)
      link = link.replace(" ", "%20")
      var localContext: HttpContext = new BasicHttpContext
      localContext.setAttribute(ClientContext.COOKIE_STORE, HtmlFetcher.emptyCookieStore)
      httpget = new HttpHead(link)
      var response: HttpResponse = null
      response = httpClient.execute(httpget, localContext)
      var entity: HttpEntity = response.getEntity
      bytes = this.minBytesForImages + 1
      try {
        var currentBytes: Int = entity.getContentLength.asInstanceOf[Int]
        var contentType: Header = entity.getContentType
        if (contentType.getValue.contains("image")) {
          bytes = currentBytes
        }
      }
      catch {
        case e: NullPointerException => {
        }
      }
    }
    catch {
      case e: Exception => {
        logger.error(e.toString)
      }
    }
    finally {
      try {
        httpget.abort
      }
      catch {
        case e: NullPointerException => {
          logger.error("HttpGet is null, can't abortz")
        }
      }
    }
    return bytes
  }

  /**
  * download the images to temp disk and set their dimensions
  * <p/>
  * we're going to score the images in the order in which they appear so images higher up will have more importance,
  * we'll count the area of the 1st image as a score of 1 and then calculate how much larger or small each image after it is
  * we'll also make sure to try and weed out banner type ad blocks that have big widths and small heights or vice versa
  * so if the image is 3rd found in the dom it's sequence score would be 1 / 3 = .33 * diff in area from the first image
  *
  * @param images
  * @return
  */

  private def downloadImagesAndGetResults(images: ArrayList[Element], depthLevel: Int): HashMap[Element, Float] = {
    val imageResults: HashMap[Element, Float] = new HashMap[Element, Float]
    var cnt: Int = 1
    var initialArea: Int = 0

    for (image <- images) {
      var continueVar = true // major haxor during java to scala conversion -> this whole section needs a rewrite
      if (cnt > 30) {
        if (logger.isDebugEnabled) {
          logger.debug("over 30 images attempted, that's enough for now")
        }
        return imageResults
      }
      try {
        val imageSource: String = this.buildImagePath(image.attr("src"))
        val localSrcPath: String = ImageSaver.storeTempImage(this.httpClient, this.linkhash, imageSource, config)
        if (localSrcPath == null) {
          if (logger.isDebugEnabled) {
            logger.debug("unable to store this image locally: IMGSRC: " + image.attr("src") + " BUILD SRC: " + imageSource)
          }
          continueVar = false
        }
        if (logger.isDebugEnabled) {
          logger.debug("Starting image: " + localSrcPath)
        }
        var width: Int = 0
        var height: Int = 0
        if (continueVar) {
          image.attr("tempImagePath", localSrcPath)
          try {
            var imageDims: ImageDetails = ImageUtils.getImageDimensions(config.imagemagickIdentifyPath, localSrcPath)
            width = imageDims.getWidth
            height = imageDims.getHeight
            if (depthLevel > 1) {
              if (width < 300) {
                if (logger.isDebugEnabled) {
                  logger.debug("going depthlevel: " + depthLevel + " and img was only: " + width + " wide: " + localSrcPath)
                }
                continueVar = false
              }
            }
          }
          catch {
            case e: IOException => {
              throw e
            }
          }
        }
        if (continueVar) {
          if (this.isBannerDimensions(width, height)) {
            if (logger.isDebugEnabled) {
              logger.debug(image.attr("src") + " seems like a fishy image dimension wise, skipping it")
            }
            image.remove
            continueVar = false
          }
        }
        if (continueVar) {
          if (width < 50) {
            if (logger.isDebugEnabled) {
              logger.debug(image.attr("src") + " is too small width: " + width + " removing..")
            }
            image.remove
            continueVar = false
          }
        }
        if (continueVar) {
          var sequenceScore: Float = 1.asInstanceOf[Float] / cnt
          var area: Int = width * height
          var totalScore: Float = 0
          if (initialArea == 0) {
            initialArea = area
            totalScore = 1
          }
          else {
            var areaDifference: Float = area.asInstanceOf[Float] / initialArea
            totalScore = sequenceScore.asInstanceOf[Float] * areaDifference
          }
          if (logger.isDebugEnabled) {
            logger.debug(imageSource + " Area is: " + area + " sequence score: " + sequenceScore + " totalScore: " + totalScore)
          }
          ({
            cnt += 1;
            cnt
          })
          imageResults.put(image, totalScore)
        }
      }
      catch {
        case e: SecretGifException => {

        }
        case e: Exception => {
          logger.error(e.toString)

        }
      }
    }
    imageResults
  }


  /**
  * returns true if we think this is kind of a bannery dimension
  * like 600 / 100 = 6 may be a fishy dimension for a good image
  *
  * @param width
  * @param height
  */
  private def isBannerDimensions(width: Integer, height: Integer): Boolean = {
    if (width eq height) {
      return false
    }
    if (width > height) {
      var diff: Float = width.asInstanceOf[Float] / height
      if (diff > 5) {
        return true
      }
    }
    if (height > width) {
      var diff: Float = height.asInstanceOf[Float] / width
      if (diff > 5) {
        return true
      }
    }
    return false
  }

  def getMinBytesForImages: Int = {
    return minBytesForImages
  }

  def setMinBytesForImages(minBytesForImages: Int): Unit = {
    this.minBytesForImages = minBytesForImages
  }

  def getTempStoragePath: String = {
    return tempStoragePath
  }

  def setTempStoragePath(tempStoragePath: String): Unit = {
    this.tempStoragePath = tempStoragePath
  }


}