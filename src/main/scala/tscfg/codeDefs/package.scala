package tscfg

import java.util.regex.Pattern

import scala.util.control.NonFatal

package object codeDefs {
  private val beginTemplatePattern = Pattern.compile("\\s*//<([^>]+)>.*$")

  private val javaMap  = getMap(scala = false)
  private val scalaMap = getMap(scala = true)

  def javaDef(key: String): String = getDef("java", javaMap, key)

  def scalaDef(key: String): String = getDef("scala", scalaMap, key)

  private def getDef(
      lang: String,
      map: Map[String, String],
      key: String
  ): String = {
    try map(key)
    catch {
      // $COVERAGE-OFF$
      case NonFatal(e) =>
        val keys = map.keySet.toList.sorted
        val msg  =
          s"Unexpected: undefined key '$key' for $lang. Defined keys: $keys. Please report this bug"
        throw new RuntimeException(msg, e)
      // $COVERAGE-ON$
    }
  }

  private def getMap(scala: Boolean): Map[String, String] = {
    val (lang, resourceName) =
      if (scala) ("scala", "codeDefs/ScalaDefs.scala")
      else ("java", "codeDefs/JavaDefs.java")

    try {
      // println(s"codeDefs.getMap $resourceName")
      val map    = collection.mutable.HashMap[String, String]()
      val source = io.Source.fromResource(resourceName)
      assert(source != null)
      var key: String = null
      val template    = new StringBuilder
      for (line <- source.getLines()) {
        if (key == null) {
          val m = beginTemplatePattern.matcher(line)
          if (m.find) {
            key = m.group(1)
          }
        }
        else if (line.contains("//</" + key + ">")) {
          map.update(key, template.toString)
          key = null
          template.setLength(0)
        }
        else template.append(line).append("\n")
      }
      map.toMap
    }
    catch {
      case NonFatal(ex) =>
        throw new RuntimeException(
          s"Unexpected exception in getMap(resourceName=$resourceName)." +
            " Please report this bug.",
          ex
        )
    }
  }
}
