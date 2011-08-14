package com.gravity.goose.text

import java.security.MessageDigest

/**
* Created by Jim Plush
* User: jim
* Date: 8/14/11
*/

object HashUtils {

  def md5(s: String): String = {
    val md5 = MessageDigest.getInstance("MD5")

    md5.reset()
    md5.update(s.getBytes)

    md5.digest().map(0xFF & _).map {
      "%02x".format(_)
    }.foldLeft("") {
      _ + _
    }
  }

}