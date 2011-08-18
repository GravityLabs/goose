package com.gravity.goose.images

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/18/11
 */

import org.apache.commons.io.IOUtils
import org.apache.http.Header
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.protocol.ClientContext
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.protocol.HttpContext
import java.io._
import java.util.Random
import com.gravity.goose.utils.Logging
import com.gravity.goose.Configuration
import com.gravity.goose.network.HtmlFetcher

/**
* This class will be responsible for storing images to disk
*
* @author Jim Plush
*/
object ImageSaver extends Logging {
  private def getFileExtension(config: Configuration, fileName: String): String = {
    var fileExtension: String = ""
    var mimeType: String = null
    try {
      val imageDims: ImageDetails = ImageUtils.getImageDimensions(config.imagemagickIdentifyPath, fileName)
      mimeType = imageDims.getMimeType
      if (mimeType == "GIF") {
        if (logger.isDebugEnabled) {
          logger.debug("SNEAKY GIF! " + fileName)
        }
        throw new SecretGifException
      }
      if (mimeType == "JPEG") {
        fileExtension = ".jpg"
      }
      else if (mimeType == "PNG") {
        fileExtension = ".png"
      }
      else {
        throw new IOException("BAD MIME TYPE: " + mimeType + " FILENAME:" + fileName)
      }
    }
    catch {
      case e: SecretGifException => {
        throw e
      }
      case e: FileNotFoundException => {
        logger.error(e.getMessage)
      }
      case e: IOException => {
        logger.error(e.getMessage)
        throw e
      }
    }
    finally {
    }
    fileExtension
  }

  /**
  * stores an image to disk and returns the path where the file was written
  *
  * @param imageSrc
  * @return
  */
  def storeTempImage(httpClient: HttpClient, linkhash: String, imageSrcMaster: String, config: Configuration): String = {
    var imageSrc = imageSrcMaster
    var localSrcPath: String = null
    var httpget: HttpGet = null
    var response: HttpResponse = null
    try {
      imageSrc = imageSrc.replace(" ", "%20")
      if (logger.isDebugEnabled) {
        logger.debug("Starting to download image: " + imageSrc)
      }
      var localContext: HttpContext = new BasicHttpContext
      localContext.setAttribute(ClientContext.COOKIE_STORE, HtmlFetcher.emptyCookieStore)
      httpget = new HttpGet(imageSrc)
      response = httpClient.execute(httpget, localContext)
      var respStatus: String = response.getStatusLine.toString
      if (!respStatus.contains("200")) {
        return null
      }
      var entity: HttpEntity = response.getEntity
      var fileExtension: String = ""
      try {
        var contentType: Header = entity.getContentType
      }
      catch {
        case e: Exception => {
          logger.error(e.getMessage)
        }
      }
      var generator: Random = new Random
      var randInt: Int = generator.nextInt
      localSrcPath = config.localStoragePath + "/" + linkhash + "_" + randInt
      if (logger.isDebugEnabled) {
        logger.debug("Storing image locally: " + localSrcPath)
      }
      if (entity != null) {
        var instream: InputStream = entity.getContent
        var outstream: OutputStream = new FileOutputStream(localSrcPath)
        try {
          try {
            IOUtils.copy(instream, outstream)
          }
          catch {
            case e: Exception => {
              throw e
            }
          }
          finally {
            entity.consumeContent
            instream.close
            outstream.close
          }
          fileExtension = ImageSaver.getFileExtension(config, localSrcPath)
          if (fileExtension == "" || fileExtension == null) {
            if (logger.isDebugEnabled) {
              logger.debug("EMPTY FILE EXTENSION: " + localSrcPath)
            }
            return null
          }
          var f: File = new File(localSrcPath)
          if (f.length < config.minBytesForImages) {
            if (logger.isDebugEnabled) {
              logger.debug("TOO SMALL AN IMAGE: " + localSrcPath + " bytes: " + f.length)
            }
            return null
          }
          var newFile: File = new File(localSrcPath + fileExtension)
          f.renameTo(newFile)
          localSrcPath = localSrcPath + fileExtension
          if (logger.isDebugEnabled) {
            logger.debug("Image successfully Written to Disk")
          }
        }
        catch {
          case e: IOException => {
            logger.error(e.toString, e)
          }
          case e: SecretGifException => {
            throw e
          }
          case e: Exception => {
            logger.error(e.getMessage)
          }
        }
      }
    }
    catch {
      case e: IllegalArgumentException => {
        logger.warn(e.getMessage)
      }
      case e: SecretGifException => {
        raise(e)
      }
      case e: ClientProtocolException => {
        logger.error(e.toString)
      }
      case e: IOException => {
        logger.error(e.toString)
      }
      case e: Exception => {
        e.printStackTrace
        logger.error(e.toString)
        e.printStackTrace
      }
    }
    finally {
      httpget.abort
    }
    localSrcPath
  }

  private def raise(e: SecretGifException): Unit = {
  }


}


