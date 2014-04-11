package com.gravity.goose.images

import org.junit.Test
import org.apache.http.client.HttpClient
import com.gravity.goose.network.HtmlFetcher
import com.gravity.goose.Configuration

/**
 * Created by Jim Plush
 * User: jim
 * Date: 9/22/11
 * Integration test for Image Utilities helper methods
 */

class ImageUtilsIT {

  @Test
  def storeImageLocally() {
    val httpClient: HttpClient = HtmlFetcher.getHttpClient
    val imgSrc = "http://tctechcrunch2011.files.wordpress.com/2011/09/aaaaa.png?w=288m"
    println(ImageUtils.storeImageToLocalFile(httpClient, "abc", imgSrc, new Configuration))
  }


  @Test
  def imageDimensions() {
    val tmpFile = "/tmp/goose/abc_5dd5d54ec1e9742a09cbe9fdf7c8a4ef"
//    println(ImageUtils.getFileExtensionName(tmpFile, new Configuration))
  }
}