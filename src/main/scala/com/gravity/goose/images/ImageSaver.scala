/**
 * Licensed to Gravity.com under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  Gravity.com licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

  def fetchEntity(httpClient: HttpClient, imageSrc: String): Option[HttpEntity] = {

    val localContext: HttpContext = new BasicHttpContext
    localContext.setAttribute(ClientContext.COOKIE_STORE, HtmlFetcher.emptyCookieStore)
    val httpget = new HttpGet(imageSrc)
    val response = httpClient.execute(httpget, localContext)
    val respStatus: String = response.getStatusLine.toString
    if (!respStatus.contains("200")) {
      None
    } else {
      try {
        Some(response.getEntity)
      } catch {
        case e: Exception => warn(e, e.toString); None
      } finally {
        httpget.abort()
      }
    }
  }


  def copyInputStreamToLocalImage(entity: HttpEntity, linkhash: String, config: Configuration): String = {
    val generator: Random = new Random
    val randInt: Int = generator.nextInt
    val localSrcPath = config.localStoragePath + "/" + linkhash + "_" + randInt
    val instream: InputStream = entity.getContent
    val outstream: OutputStream = new FileOutputStream(localSrcPath)
    try {
      trace("Storing image locally: " + localSrcPath)
      IOUtils.copy(instream, outstream)
      val fileExtension = ImageSaver.getFileExtension(config, localSrcPath)
      if (fileExtension == "" || fileExtension == null) {
        trace("EMPTY FILE EXTENSION: " + localSrcPath)
        return null
      }
      val f: File = new File(localSrcPath)
      if (f.length < config.minBytesForImages) {
        if (logger.isDebugEnabled) {
          logger.debug("TOO SMALL AN IMAGE: " + localSrcPath + " bytes: " + f.length)
        }
        return null
      }
      val newFilename = localSrcPath + fileExtension
      val newFile: File = new File(newFilename)
      f.renameTo(newFile)
      //      localSrcPath = localSrcPath + fileExtension
      trace("Image successfully Written to Disk")
      newFilename
    }
    catch {
      case e: Exception => {
        throw e
      }
    }
    finally {
      //            entity.consumeContent
      instream.close()
      outstream.close()
    }
  }

  /**
  * stores an image to disk and returns the path where the file was written
  *
  * @param imageSrc
  * @return
  */
  def storeTempImage(httpClient: HttpClient, linkhash: String, imageSrcMaster: String, config: Configuration): String = {
    var imageSrc = imageSrcMaster


    try {
      imageSrc = imageSrc.replace(" ", "%20")
      trace("Starting to download image: " + imageSrc)

      fetchEntity(httpClient, imageSrc) match {
        case Some(entity) => {

            try {
              return copyInputStreamToLocalImage(entity, linkhash, config)
            }
            catch {
              case e: SecretGifException => {
                throw e
              }
              case e: Exception => {
                logger.error(e.getMessage); null
              }
            }

        }
        case None => trace("Unable to get entity for: " + imageSrc); null
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
        e.printStackTrace()
        logger.error(e.toString)
        e.printStackTrace()
      }
    }
    finally {

    }
    null

  }

  private def raise(e: SecretGifException): Unit = {
  }


}


