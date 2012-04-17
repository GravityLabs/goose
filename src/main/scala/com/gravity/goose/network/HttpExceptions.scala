package com.gravity.goose.network

/**
 * Created by IntelliJ IDEA.
 * Author: Robbie Coleman
 * Date: 11/2/11
 * Time: 10:25 AM
 */

class LoggableException(msg: String, innerEx: Exception = null) extends Exception(msg, innerEx) {
  override lazy val getMessage = {
    val innerMessage = if (innerEx != null) {
      "%n\tand inner Exception of type %s:%n\t\tmessage: %s".format(innerEx.getClass.getName, innerEx.getMessage)
    } else {
      ""
    }
    getClass.getName + " ==> " + msg + innerMessage
  }
}

class NotFoundException(url: String) extends LoggableException("SERVER RETURNED 404 FOR LINK: " + url)
class BadRequestException(url: String) extends LoggableException("Bad Request for URL: " + url)
class NotAuthorizedException(url: String, statusCode: Int = 403) extends LoggableException("Not authorized (statusCode: %d) to access URL: %s".format(statusCode, url))
class ServerErrorException(url: String, statusCode: Int = 500) extends LoggableException("Server Error! Status code returned: %d for URL: %s".format(statusCode, url))
class UnhandledStatusCodeException(url: String, statusCode: Int)  extends LoggableException("Received HTTP statusCode: %d from URL: %s and did not know how to handle it!".format(statusCode, url))

object HttpStatusValidator {
  def validate(url: String, statusCode: Int): Either[Exception, String] = statusCode match {
    case 200 => Right("OK")
    case 400 => Left(new BadRequestException(url))
    case 404 => Left(new NotFoundException(url))
    case auth if (auth > 400 && auth < 500) => Left(new NotAuthorizedException(url, auth))
    case error if (error > 499) => Left(new ServerErrorException(url, error))
    case unk => Left(new UnhandledStatusCodeException(url, statusCode))
  }
}

class ImageFetchException(imgSrc: String, ex: Exception = null) extends LoggableException("Failed to fetch image file from imgSrc: " + imgSrc, ex)