package tscfg

object util {

  def upperFirst(symbol: String): String = {
    if (symbol.isEmpty) ""
    else symbol.charAt(0).toUpper + symbol.substring(1)
  }

  def escapeString(s: String): String = s.replaceAll("\\\"", "\\\\\"")

  val TypesafeConfigClassName: String = classOf[com.typesafe.config.Config].getName
}
