package tscfg.generators.java

import com.google.googlejavaformat.java.Formatter

import scala.util.control.NonFatal

object formatter {
  private val formatter = new Formatter()

  def format(source: String): String = {
    try formatter.formatSource(source).trim
    catch {
      case NonFatal(e) =>
        scribe.warn(s"error while running formatter: ${e.getMessage}")
        source
    }
  }
}
