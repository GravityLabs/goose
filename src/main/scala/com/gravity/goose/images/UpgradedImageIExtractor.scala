package com.gravity.goose.images

import org.apache.http.client.HttpClient
import com.gravity.goose.{ Configuration, Article }
import org.jsoup.nodes.{ Element, Document }
import java.util.regex.{ Pattern, Matcher }
import com.gravity.goose.text.string
import java.net.{ MalformedURLException, URL }
import org.jsoup.select.Elements
import scala.collection.JavaConversions._
import java.util.ArrayList
import collection.mutable.{ ListBuffer, HashMap }
import com.gravity.goose.utils.FileHelper
import io.Source

/**
 * Created by Jim Plush
 * User: jim
 * Date: 9/22/11
 */

case class DepthTraversal(node: Element, parentDepth: Int, siblingDepth: Int)

class UpgradedImageIExtractor(httpClient: HttpClient, article: Article, config: Configuration) extends ImageExtractor {

  import UpgradedImageIExtractor._

  /**
   * What's the minimum bytes for an image we'd accept is
   */
  private val minBytesForImages: Int = 4000

  /**
   * the webpage url that we're extracting content from
   */
  val targetUrl = article.finalUrl
  /**
   * stores a hash of our url for reference and image processing
   */
  val linkhash = article.linkhash

  /**
   * this lists all the known bad button names that we have
   */
  val matchBadImageNames: Matcher = {
    val sb = new StringBuilder
    // create negative elements
  sb.append(".html|.gif|.ico|button|twitter.jpg|facebook.jpg|ap_buy_photo|digg.jpg|digg.png|delicious.png|facebook.png|reddit.jpg|doubleclick|diggthis|diggThis|adserver|/ads/|ec.atdmt.com")
    sb.append("|mediaplex.com|adsatt|view.atdmt")
    Pattern.compile(sb.toString()).matcher(string.empty)
  }

  def getBestImage(doc: Document, topNode: Element): Image = {
    trace("Starting to Look for the Most Relevant Image")
    checkForKnownElements() match {
      case Some(image) => return image
      case None => {
        trace("No known images found")
      }
    }

    checkForMetaTag match {
      case Some(image) => return image
      case None => trace("No Meta Tag Images found")
    }

    checkForLargeImages(topNode, 0, 0) match {
      case Some(image) => return image
      case None => {
        trace("No big images found")
      }
    }

    new Image
  }

  def getAllImages(node: Element): List[Image] = {
    getImageCandidates(node) match {
      case Some(goodImages) => {
        val scoredImages = downloadImagesAndGetResults(goodImages, 0)
        scoredImages.map((scoredImage: (LocallyStoredImage, Float)) => scoredImageToResultImage(scoredImage._1, scoredImages.size)).toList
      }
      case None => {
        Nil
      }
    }
  }

  /**
   * Prefer Twitter images (as they tend to have the right size for us), then Open Graph images
   * (which seem to be smaller), and finally linked images.
   */
  private def checkForMetaTag: Option[Image] = {

    checkForTwitterTag match {
      case Some(image) => return Some(image)
      case None => trace("No twitter image found")
    }

    checkForOpenGraphTag match {
      case Some(image) => return Some(image)
      case None => trace("No open graph images found")
    }

    checkForLinkTag match {
      case Some(image) => return Some(image)
      case None => trace("No link tag images found")
    }

    None
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
  private def checkForLargeImages(node: Element, parentDepthLevel: Int, siblingDepthLevel: Int): Option[Image] = {
    trace("Checking for large images - parent depth " + parentDepthLevel + " sibling depth: " + siblingDepthLevel)

    getImageCandidates(node) match {
      case Some(goodImages) => {
        trace("checkForLargeImages: After findImagesThatPassByteSizeTest we have: " + goodImages.size + " at parent depth: " + parentDepthLevel)
        val scoredImages = downloadImagesAndGetResults(goodImages, parentDepthLevel)
        // get the high score image in a tuple
        scoredImages.sortBy(-_._2).take(1).headOption match {
          case Some(highScoreImage) => {
            val mainImage = scoredImageToResultImage(highScoreImage._1, scoredImages.size)
            trace("IMAGE COMPLETE: High Score Image is: " + mainImage.imageSrc + " Score is: " + highScoreImage._2)
            return Some(mainImage)
          }
          case None => {
            trace("No good images found after filtering")
            getDepthLevel(node, parentDepthLevel, siblingDepthLevel) match {
              case Some(depthObj) => {
                return checkForLargeImages(depthObj.node, depthObj.parentDepth, depthObj.siblingDepth)
              }
              case None => trace("Image iteration is over!")
            }
          }
        }

      }
      case None => {
        getDepthLevel(node, parentDepthLevel, siblingDepthLevel) match {
          case Some(depthObj) => {
            return checkForLargeImages(depthObj.node, depthObj.parentDepth, depthObj.siblingDepth)
          }
          case None => trace("Image iteration is over!")
        }
      }
    }

    None
  }

  private def scoredImageToResultImage(scoredImage: LocallyStoredImage, scoredImagesLength: Int): Image = {
    val mainImage = new Image
    // mainImage.topImageNode = highScoreImage
    mainImage.imageSrc = scoredImage.imgSrc
    mainImage.imageExtractionType = "bigimage"
    mainImage.bytes = scoredImage.bytes
    mainImage.width = scoredImage.width
    mainImage.height = scoredImage.height
    mainImage.confidenceScore = if (scoredImagesLength > 0) (100 / scoredImagesLength) else 0
    mainImage
  }

  def getDepthLevel(node: Element, parentDepth: Int, siblingDepth: Int): Option[DepthTraversal] = {
    if (node == null) return None

    val MAX_PARENT_DEPTH = 2
    if (parentDepth > MAX_PARENT_DEPTH) {
      trace("ParentDepth is greater than " + MAX_PARENT_DEPTH + ", aborting depth traversal")
      None
    } else {
      val siblingNode = node.previousElementSibling()
      if (siblingNode == null) {
        Some(DepthTraversal(node.parent, parentDepth + 1, 0))
      } else {
        Some(DepthTraversal(siblingNode, parentDepth, siblingDepth + 1))
      }
    }
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
  private def downloadImagesAndGetResults(images: ArrayList[Element], depthLevel: Int): ListBuffer[(LocallyStoredImage, Float)] = {
    val imageResults = new ListBuffer[(LocallyStoredImage, Float)]()
    var initialArea: Float = 0
    var cnt = 1.0f
    val MIN_WIDTH = 50
    val MIN_HEIGHT = 0

    images.take(30).foreach((image: Element) => {
      for {
        locallyStoredImage <- getLocallyStoredImage(buildImagePath(image.attr("src")))
        width = locallyStoredImage.width
        if (width > MIN_WIDTH)
        height = locallyStoredImage.height
        if (height > MIN_HEIGHT)
        fileExtension = locallyStoredImage.fileExtension
        //why not gif?:  if (fileExtension != ".gif" && fileExtension != "NA")
        if (fileExtension != "NA")
        	imageSrc = locallyStoredImage.imgSrc
        if ((depthLevel >= 1 && locallyStoredImage.width > 300) || depthLevel < 1)
        if (!isBannerDimensions(width, height))
      } {
        val sequenceScore: Float = 1.0f / cnt
        val area: Float = width * height
        var totalScore: Float = 0
        if (initialArea == 0) {
          // give the initial image a little area boost as well
          initialArea = area * 1.48f
          totalScore = 1
        } else {
          val areaDifference: Float = area / initialArea
          totalScore = sequenceScore * areaDifference
        }
        trace("IMG: " + imageSrc + " Area is: " + area + " sequence score: " + sequenceScore + " totalScore: " + totalScore)
        cnt += 1

        imageResults += locallyStoredImage -> totalScore
        cnt += 1
      }
    })

    imageResults
  }

  def getAllImages(topNode: Element, parentDepthLevel: Int = 0, siblingDepthLevel: Int = 0): List[Image] = {
    trace("getting All Images")
    var images: ListBuffer[Image] = new ListBuffer()
    getImageCandidates(topNode) match {
      case Some(candidateImages) => {
        for {
          cadidateImg <- candidateImages
          locallyStoredImg <- getLocallyStoredImage(buildImagePath(cadidateImg.attr("src")))
        } {
          var img = new Image 
          img.imageSrc = locallyStoredImg.imgSrc
          img.width = locallyStoredImg.width
          img.height = locallyStoredImg.height
          img.bytes = locallyStoredImg.bytes
          images += img
  }
        return images.toList
      }
      case None => {
        getDepthLevel(topNode, parentDepthLevel, siblingDepthLevel) match {
          case Some(depthObj) => {
            return getAllImages(depthObj.node, depthObj.parentDepth, depthObj.siblingDepth)
          }
          case None => return images.toList
        }
      } 
    }
  }

  /**
   * returns true if we think this is kind of a bannery dimension
   * like 600 / 100 = 6 may be a fishy dimension for a good image
   *
   * @param width
   * @param height
   */
  private def isBannerDimensions(width: Int, height: Int): Boolean = {
    if (width == height) {
      return false
    }
    if (width > height) {
      val diff: Float = (width.asInstanceOf[Float] / height.asInstanceOf[Float])
      if (diff > 5) {
        return true
      }
    }
    if (height > width) {
      val diff: Float = height.asInstanceOf[Float] / width.asInstanceOf[Float]
      if (diff > 5) {
        return true
      }
    }
    false
  }

  def getImagesFromNode(node: Element): Option[Elements] = {
    val images: Elements = node.select("img")

    if (images == null || images.isEmpty) {
      None
    } else {
      Some(images)
    }
  }

  /**
   * takes a list of image elements and filters out the ones with bad names
   *
   * @param images
   * @return
   */
  private def filterBadNames(images: Elements): Option[ArrayList[Element]] = {
    val goodImages: ArrayList[Element] = new ArrayList[Element]
    for (image <- images) {
      if (this.isOkImageFileName(image)) {
        goodImages.add(image)
      } else {
        image.remove()
      }
    }
    if (goodImages == null || goodImages.isEmpty) None else Some(goodImages)
  }

  /**
   * will check the image src against a list of bad image files we know of like buttons, etc...
   *
   * @return
   */
  private def isOkImageFileName(imageNode: Element): Boolean = {
    val imgSrc: String = imageNode.attr("src")
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

  def getImageCandidates(node: Element): Option[ArrayList[Element]] = {

    for {
      n <- getNode(node)
      images <- getImagesFromNode(node)
      filteredImages <- filterBadNames(images)
      goodImages <- findImagesThatPassByteSizeTest(filteredImages)
    } {
      //return Some(filteredImages)
      return Some(goodImages)
    }
    None

  }

  /**
   * loop through all the images and find the ones that have the best bytez to even make them a candidate
   *
   * @param images
   * @return
   */
  private def findImagesThatPassByteSizeTest(images: ArrayList[Element]): Option[ArrayList[Element]] = {
    var cnt: Int = 0
    val MAX_BYTES_SIZE: Int = 15728640
    val goodImages: ArrayList[Element] = new ArrayList[Element]
    images.foreach(image => {
      try {
        if (cnt > 30) {
          trace("Abort! they have over 30 images near the top node: ")
          return Some(goodImages)
        }
        val imageSrc = image.attr("src")
        getLocallyStoredImage(buildImagePath(imageSrc)) match {
          case Some(locallyStoredImage) => {

            val bytes = locallyStoredImage.bytes
            if ((bytes == 0 || bytes > minBytesForImages) && bytes < MAX_BYTES_SIZE) {
              trace("findImagesThatPassByteSizeTest: Found potential image - size: " + bytes + " src: " + image.attr("src"))
              goodImages.add(image)
            } else {
              trace("Removing image: " + image.attr("src"))
              image.remove()
            }

          }
          case None => trace(imageSrc + " unable to fetch")
        }

      } catch {
        case e: Exception => warn(e, e.toString)
      }
      cnt += 1
    })

    trace(" Now leaving findImagesThatPassByteSizeTest")
    if (goodImages == null || goodImages.isEmpty) None else Some(goodImages)

  }

  def getNode(node: Element): Option[Element] = {
    if (node == null) None else Some(node)
  }

  /**
   * checks to see if we were able to find open graph tags on this page
   *
   * @return
   */
  private def checkForLinkTag: Option[Image] = {
    if (article.rawDoc == null) return None

    try {
      val meta: Elements = article.rawDoc.select("link[rel~=image_src]")
      for (item <- meta) {
        val href = item.attr("href")
        if (href.isEmpty) {
          return None
        }
        val mainImage = new Image
        mainImage.imageSrc = buildImagePath(href)
        mainImage.imageExtractionType = "linktag"
        mainImage.confidenceScore = 100

        getLocallyStoredImage(mainImage.imageSrc) match {
          case Some(locallyStoredImage) => {
            mainImage.bytes = locallyStoredImage.bytes
            mainImage.height = locallyStoredImage.height
            mainImage.width = locallyStoredImage.width
          }
          case None =>
        }
        trace("link tag found, using it")

        return ensureMinimumImageSize(mainImage)
      }
      None
    } catch {
      case e: Exception => {
        warn("Unexpected exception caught in checkForLinkTag. Handled by returning None.", e)
        None
      }
    }

  }

  /**
   * checks to see if we were able to find open graph tags on this page
   *
   * @return
   */
  private def checkForOpenGraphTag: Option[Image] = {
    try {
      val meta: Elements = article.rawDoc.select("meta[property~=og:image]")

      for (item <- meta) {
        if (item.attr("content").length < 1) {
          return None
        }
        val imagePath: String = this.buildImagePath(item.attr("content"))
        val mainImage = new Image
        mainImage.imageSrc = imagePath
        mainImage.imageExtractionType = "opengraph"
        mainImage.confidenceScore = 100
        getLocallyStoredImage(mainImage.imageSrc) match {
          case Some(locallyStoredImage) => {
            mainImage.bytes = locallyStoredImage.bytes
            mainImage.height = locallyStoredImage.height
            mainImage.width = locallyStoredImage.width
          }
          case None =>
        }
        trace("open graph tag found, using it: %s".format(imagePath))

        return ensureMinimumImageSize(mainImage)
      }
      None
    } catch {
      case e: Exception => {
        warn(e, e.toString)
        None
      }
    }
  }

  private def checkForTwitterTag: Option[Image] = {
    try {
      val meta: Elements = article.rawDoc.select("meta[property~=twitter:image]")

      for (item <- meta) {
        if (item.attr("content").length < 1) {
          return None
        }
        val imagePath: String = this.buildImagePath(item.attr("content"))
        val mainImage = new Image
        mainImage.imageSrc = imagePath
        mainImage.imageExtractionType = "twitter"
        mainImage.confidenceScore = 100
        getLocallyStoredImage(mainImage.imageSrc) match {
          case Some(locallyStoredImage) => {
            mainImage.bytes = locallyStoredImage.bytes
            mainImage.height = locallyStoredImage.height
            mainImage.width = locallyStoredImage.width
          }
          case None =>
        }
        trace("twitter image tag found, using it: %s".format(imagePath))

        return ensureMinimumImageSize(mainImage)
      }
      None

    } catch {
      case e: Exception => {
        warn(e, e.toString)
        None
      }
    }
  }

  private def ensureMinimumImageSize(mainImage: Image): Option[Image] = {
    trace("Checking image %s for proper size".format(mainImage.getImageSrc))
    if (mainImage.width >= config.minWidth && mainImage.height >= config.minHeight) {
      trace("Image accepted")
      return Some(mainImage)
    }
    trace("Image rejected as too small - actual size is %1$s wide by %2$s tall".format(mainImage.width, mainImage.height))
    None
  }

  /**
   * returns the bytes of the image file on disk
   */
  def getLocallyStoredImage(imageSrc: String): Option[LocallyStoredImage] = ImageUtils.storeImageToLocalFile(httpClient, linkhash, imageSrc, config)

  def getCleanDomain = {
    // just grab the very end of the domain
    dotRegex.split(article.domain).takeRight(2).mkString(".")
  }

  /**
   * in here we check for known image contains from sites we've checked out like yahoo, techcrunch, etc... that have
   * known  places to look for good images.
   * //todo enable this to use a series of settings files so people can define what the image ids/classes are on specific sites
   */
  def checkForKnownElements(): Option[Image] = {
    if (article.rawDoc == null) return None

    val domain = getCleanDomain
    customSiteMapping.get(domain).foreach(classes => {
      subDelimRegex.split(classes).foreach(c => KNOWN_IMG_DOM_NAMES += c)
    })

    var knownImage: Element = null
    trace("Checking for known images from large sites")

    for (knownName <- KNOWN_IMG_DOM_NAMES; if (knownImage == null)) {
      var known: Element = article.rawDoc.getElementById(knownName)
      if (known == null) {
        known = article.rawDoc.getElementsByClass(knownName).first
      }
      if (known != null) {
        val mainImage: Element = known.getElementsByTag("img").first
        if (mainImage != null) {
          knownImage = mainImage
          trace("Got Known Image: " + mainImage.attr("src"))
        }
      }
    }

    if (knownImage == null) return None

    val knownImgSrc: String = knownImage.attr("src")
    val mainImage = new Image
    mainImage.imageSrc = buildImagePath(knownImgSrc)
    mainImage.imageExtractionType = "known"
    mainImage.confidenceScore = 90

    getLocallyStoredImage(buildImagePath(mainImage.imageSrc)).foreach(locallyStoredImage => {
      mainImage.bytes = locallyStoredImage.bytes
      mainImage.height = locallyStoredImage.height
      mainImage.width = locallyStoredImage.width
    })

    ensureMinimumImageSize(mainImage)
  }

  /**
   * This method will take an image path and build out the absolute path to that image
   * using the initial url we crawled so we can find a link to the image if they use relative urls like ../myimage.jpg
   *
   * @param imageSrc
   * @return
   */
  private def buildImagePath(imageSrc: String): String = {

    try {
      val pageURL = new URL(this.targetUrl)
      return new URL(pageURL, ImageUtils.cleanImageSrcString(imageSrc)).toString
    } catch {
      case e: MalformedURLException => {
        warn("Unable to get Image Path: " + imageSrc)
      }
    }

    imageSrc
  }

}

object UpgradedImageIExtractor {
  val delimRegex = """\^""".r
  val dotRegex = """\.""".r
  val subDelimRegex = """\|""".r

  // custom site mapping is for major sites that we know what they generally
  // place images into, allows for higher accuracy of image extraction
  lazy val customSiteMapping = {
    val lines = Source.fromInputStream(getClass.getResourceAsStream("/com/gravity/goose/images/known-image-css.txt")).getLines()
    (for (line <- lines) yield {
      val Array(domain, css) = delimRegex.split(line)
      domain -> css
    }).toMap
  }

  val KNOWN_IMG_DOM_NAMES = ListBuffer("yn-story-related-media", "cnn_strylccimg300cntr", "big_photo", "ap-smallphoto-a")

}
