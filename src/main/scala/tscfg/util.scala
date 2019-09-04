package tscfg

object util {

  def upperFirst(symbol: String): String = {
    if (symbol.isEmpty) ""
    else symbol.charAt(0).toUpper + symbol.substring(1)
  }

  def escapeString(s: String): String = s.replaceAll("\\\"", "\\\\\"")

  def escapeValue(s: String): String = {
    def escapeChar(c: Char) = c match {
      case '\n' ⇒ "\\n"
      case '\t' ⇒ "\\t"
      case '\f' ⇒ "\\f"
      case '\r' ⇒ "\\r"
      case '\b' ⇒ "\\b"
      case '\\' ⇒ "\\\\"
      case _    ⇒ String.valueOf(c)
    }
    s.flatMap(escapeChar)
  }

  val TypesafeConfigClassName: String = classOf[com.typesafe.config.Config].getName
}
