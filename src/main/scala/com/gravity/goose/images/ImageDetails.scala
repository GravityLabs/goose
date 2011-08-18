package com.gravity.goose.images

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/18/11
 */

/**
* holds the details of the result of inspecting an image
* @author Jim Plush
*
*/
class ImageDetails {
  def getWidth: Int = {
    return width
  }

  def setWidth(width: Int): Unit = {
    this.width = width
  }

  def getHeight: Int = {
    return height
  }

  def setHeight(height: Int): Unit = {
    this.height = height
  }

  def getMimeType: String = {
    return mimeType
  }

  def setMimeType(mimeType: String): Unit = {
    this.mimeType = mimeType
  }

  /**
  * the width of the image
  */
  private var width: Int = 0
  /**
  * height of the image
  */
  private var height: Int = 0
  /**
  * the mimeType of the image JPEG / PNG
  */
  private var mimeType: String = null
}