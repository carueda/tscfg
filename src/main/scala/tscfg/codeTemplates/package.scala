package tscfg

import java.util.regex.Pattern

import scala.util.control.NonFatal

package object codeTemplates {
  private val beginTemplatePattern = Pattern.compile("\\s*//<([^>]+)>.*$")

  private val javaMap = getMap("codeTemplates/JavaCodeTemplates")
  private val scalaMap = getMap("codeTemplates/ScalaCodeTemplates")

  def getJavaTemplate(key: String): String = javaMap(key)

  def getScalaTemplate(key: String): String = scalaMap(key)

  private def getMap(templateName: String): Map[String, String] = try {
    //println(s"codeTemplates.getMap $templateName")
    val map = collection.mutable.HashMap[String, String]()
    val is = getClass.getClassLoader.getResourceAsStream(templateName)
    assert(is != null)
    val source = io.Source.fromInputStream(is, "utf-8")
    assert(source != null)
    var key: String = null
    val template = new StringBuilder
    for (line ← source.getLines()) {
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
    is.close()
    map.toMap
  }
  catch {
    case NonFatal(ex) ⇒
      throw new RuntimeException("Unexpected exception. Please report this bug.", ex)
  }
}
