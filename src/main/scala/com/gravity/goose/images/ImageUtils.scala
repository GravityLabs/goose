package com.gravity.goose.images

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/18/11
 */

import javax.imageio.ImageIO
import java.awt.color.CMMException
import java.awt.image.BufferedImage
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.HashMap
import com.gravity.goose.utils.Logging

object ImageUtils extends Logging {
  /**
  * User: Jim Plush
  * gets the image dimensions for an image file, pass in the path to the image who's dimensions you want to get
  * this will use imageMagick since the Java IO and imaging shit SUCKS for getting mime types and file info for jpg and png files
  *
  * @param filePath
  * @return
  */
  def getImageDimensions(identifyProgram: String, filePath: String): ImageDetails = {
    var command: ArrayList[String] = new ArrayList[String](10)
    command.add(identifyProgram)
    command.add(filePath)
    var imageInfo: String = execToString(command.toArray(new Array[String](1)).asInstanceOf[Array[String]])
    var imageDetails: ImageDetails = new ImageDetails
    if (imageInfo == null || imageInfo.contains("no decode delegate for this image format")) {
      throw new IOException("Unable to get Image Information (no decode delegate) for: " + filePath)
    }
    try {
      var infoParts: Array[String] = imageInfo.split(" ")
      imageDetails.setMimeType(infoParts(1))
      var dimensions: Array[String] = infoParts(2).split("x")
      imageDetails.setWidth(Integer.parseInt(dimensions(0)))
      imageDetails.setHeight(Integer.parseInt(dimensions(1)))
      return imageDetails
    }
    catch {
      case e: NullPointerException => {
        throw new IOException("Unable to get Image Information for: " + filePath)
      }
    }
  }

  /**
  * gets the image dimensions for an image file, pass in the path to the image who's dimensions you want to get, uses the built in java commands
  *
  * @param filePath
  * @return
  */
  def getImageDimensionsJava(filePath: String): HashMap[String, Integer] = {
    var image: BufferedImage = null
    try {
      var f: File = new File(filePath)
      image = ImageIO.read(f)
      var results: HashMap[String, Integer] = new HashMap[String, Integer]
      results.put("height", image.getHeight)
      results.put("width", image.getWidth)
      return results
    }
    catch {
      case e: CMMException => {
        logger.error("ERROR READING FILE: " + filePath + " \n", e)
        throw new IOException("Unable to read file: " + filePath)
      }
    }
    finally {
      if (image != null) {
        try {
          image.flush
        }
        catch {
          case e: Exception => {
          }
        }
      }
    }
  }

  /**
  * Tries to exec the command, waits for it to finish, logs errors if exit
  * status is nonzero, and returns true if exit status is 0 (success).
  *
  * @param command Description of the Parameter
  * @return Description of the Return Value
  */
  private def execToString(command: Array[String]): String = {
    var p: Process = null
    var in: BufferedReader = null
    try {
      p = Runtime.getRuntime.exec(command)
      in = new BufferedReader(new InputStreamReader(p.getInputStream))
      var line: String = null
      line = in.readLine
      p.waitFor
      return line
    }
    catch {
      case e: IOException => {
        logger.error(e.toString, e)
      }
      case e: InterruptedException => {
        logger.error(e.toString, e)
        throw new RuntimeException(e)
      }
    }
    finally {
      if (in != null) {
        try {
          in.close
        }
        catch {
          case e: IOException => {
          }
        }
      }
      if (p != null) {
        p.destroy
      }
    }
    return null
  }


}
