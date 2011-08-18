package com.gravity.goose.images

import org.jsoup.nodes.Element

/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/18/11
 */

class Image {


  /**
   * holds the Element node of the image we think is top dog
   */
  var topImageNode: Element = null

  /**
   * holds the src of the image
   */
  var imageSrc: String = null;

  /**
   * how confident are we in this image extraction? the most images generally the less confident
   */
  var confidenceScore: Double = 0.0;


  /**
   * what kind of image extraction was used for this? bestGuess, linkTag, openGraph tags?
   */
  var imageExtractionType: String = null;


  /**
   * stores how many bytes this image is.
   */
  var bytes: Int = 0;


  def getImageSrc = {
    imageSrc
  }
}