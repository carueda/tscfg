package tscfg

object util {

  def upperFirst(symbol: String) = symbol.charAt(0).toUpper + symbol.substring(1)

  val TypesafeConfigClassName = classOf[com.typesafe.config.Config].getName
}
