package tscfg

object scalaUtil {

  /**
    * Returns a valid scala identifier from the given symbol:
    *
    * - encloses the symbol in backticks if the symbol is a scala reserved word;
    * - appends an underscore if the symbol corresponds to a no-arg method in scope;
    * - otherwise, returns symbol if it is a valid java identifier
    * - otherwise, returns `javaGenerator.javaIdentifier(symbol)`
    */
  def scalaIdentifier(symbol: String): String = {
    if (scalaReservedWords.contains(symbol)) "`" + symbol + "`"
    else if (noArgMethodInScope.contains(symbol)) symbol + "_"
    else if (javaUtil.isJavaIdentifier(symbol)) symbol
    else javaUtil.javaIdentifier(symbol)
  }

  def getClassName(symbol:String) = util.upperFirst(scalaIdentifier(symbol))

  /**
    * from Sect 1.1 of the Scala Language Spec, v2.9
    */
  val scalaReservedWords: List[String] = List(
    "abstract", "case",     "catch",   "class",   "def",
    "do",       "else",     "extends", "false",   "final",
    "finally",  "for",      "forSome", "if",      "implicit",
    "import",   "lazy",     "match",   "new",     "null",
    "object",   "override", "package", "private", "protected",
    "return",   "sealed",   "super",   "this",    "throw",
    "trait",    "try",      "true",    "type",    "val",
    "var",      "while",    "with",    "yield"
  )

  val noArgMethodInScope: List[String] = List(
      "clone",
      "finalize",
      "getClass",
      "notify",
      "notifyAll",
      "toString",
      "wait"
  )
}
