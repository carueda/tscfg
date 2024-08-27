package tscfg

import scribe.Logger
import scribe.format._

object util {

  def upperFirst(symbol: String): String = symbol.capitalize

  def escapeString(s: String): String = s.replaceAll("\\\"", "\\\\\"")

  def escapeValue(s: String): String = {
    def escapeChar(c: Char) = c match {
      case '\n' => "\\n"
      case '\t' => "\\t"
      case '\f' => "\\f"
      case '\r' => "\\r"
      case '\b' => "\\b"
      case '\\' => "\\\\"
      case _    => String.valueOf(c)
    }
    s.flatMap(escapeChar)
  }

  // $COVERAGE-OFF$
  // debugging helper
  def setLogMinLevel(
      name: Option[String] = None,
      minimumLevel: Option[scribe.Level] = Some(scribe.Level.Debug),
  ): Unit = {
    val logger = name.map(Logger(_)).getOrElse(Logger.root)
    logger
      .clearHandlers()
      .clearModifiers()
      .withHandler(
        formatter =
          formatter"${string("[")}$levelColored${string("]")} ${green(positionAbbreviated)} - $messages$mdc",
        minimumLevel = minimumLevel
      )
      .replace()
  }

  // $COVERAGE-ON$
}
