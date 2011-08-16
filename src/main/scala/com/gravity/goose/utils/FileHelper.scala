package com.gravity.goose.utils

import org.apache.commons.io.IOUtils
import java.io.{IOException, InputStream}


/**
 * Created by Jim Plush
 * User: jim
 * Date: 8/16/11
 */

object FileHelper extends Logging {

  def loadResourceFile[A](filename: String, cls: Class[A]): String = {
    var filedata: String = ""
    val is: InputStream = cls.getResourceAsStream(filename)
    try {
      filedata = IOUtils.toString(is, "UTF-8")
    }
    catch {
      case e: IOException => warn(e, e.toString)
    }
    filedata
  }
}