package eu.ec.dgempl.eessi.utils

import java.io._
import com.fasterxml.jackson.core._
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.SerializationFeature

object JsonUtil {

  private val LOG = org.slf4j.LoggerFactory.getLogger("JsonUtil")

  private val mapper = create()

  private def create(): ObjectMapper = {
    val mapper = new ObjectMapper()
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true)
    mapper
  }

  def toJson[T](data: T): String = {
    try {
      mapper.writeValueAsString(data)
    } catch {
      case e: IOException => {
        LOG.warn("can't format a json object from [" + data + "]", e)
        null
      }
    }
  }

  def toJsonNode[T](data: T): JsonNode = mapper.valueToTree(data)

  def fromJson[T](description: String, theClass: Class[T]): T = {
    //PlayUtils.fixClassloader(theClass)
    val parse = mapper.readValue(description, classOf[JsonNode])
    val fromJson = mapper.treeToValue(parse, theClass)
    fromJson
  }

  private def shorter(description: String): String = {
    val maxSize = 1000
    if (description == null || description.length < maxSize) {
      return description
    }
    description.substring(0, maxSize - 3) + "..."
  }

  def copy[T](data: T): T = {
    fromJson(toJson(data), data.getClass.asInstanceOf[Class[T]])
  }

  def clone[T](`object`: T, excludeFields: String): T = {
    val exported = JsonUtil.toJson(`object`)
    val obj = JsonUtil.fromJson(exported, `object`.getClass)
    obj.asInstanceOf[T]
  }
}
