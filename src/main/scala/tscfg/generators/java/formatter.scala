package tscfg.generators.java

import com.google.googlejavaformat.java.Formatter

object formatter {
  private val formatter = new Formatter()

  def format(source: String): String = formatter.formatSource(source).trim
}
