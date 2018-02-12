package tscfg.generators.scala

import tscfg.generators.java.javaUtil
import tscfg.util

/**
  * By default [[scalaIdentifier]] uses underscores as needed for various cases
  * that need translation of the given identifier to make it valid Scala.
  * Similarly, [[getClassName]] also does some related logic.
  *
  * With this flag set to true, both methods will change their logic to use
  * backticks instead of replacing or removing the characters that would
  * make the resulting identifiers invalid.
  *
  * @param useBackticks  False by default
  */
class ScalaUtil(useBackticks: Boolean = false) {
  import ScalaUtil._

  /**
    * Returns a valid scala identifier from the given symbol:
    *
    * - encloses the symbol in backticks if the symbol is a scala reserved word;
    * - appends an underscore if the symbol corresponds to a no-arg method in scope;
    * - otherwise:
    *   - returns symbol if it is a valid java identifier
    *   - otherwise:
    * if useBackticks is true, encloses symbol in backticks
    * otherwise, returns `javaGenerator.javaIdentifier(symbol)`
    */
  def scalaIdentifier(symbol: String): String = {
    if (scalaReservedWords.contains(symbol)) "`" + symbol + "`"
    else if (noArgMethodInScope.contains(symbol)) symbol + "_"
    else if (javaUtil.isJavaIdentifier(symbol)) symbol
    else if (useBackticks) "`" + symbol + "`"
    else javaUtil.javaIdentifier(symbol)
  }

  /**
    * Returns a class name from the given symbol.
    * If useBackticks:
    *   This is basically capitalizing the first character that
    *   can be capitalized. If none, then a `U` is prepended.
    * Otherwise:
    * Since underscores are specially used in generated code,
    * this method camelizes the symbol in case of any underscores.
    */
  def getClassName(symbol: String): String = {
    if (useBackticks) {
      val scalaId = scalaIdentifier(symbol)
      val search = scalaId.zipWithIndex.find { case (c, _) ⇒ c.toUpper != c }
      search match {
        case Some((c, index)) ⇒
          scalaId.substring(0, index) + c.toUpper + scalaId.substring(index + 1)

        case None ⇒
          if (scalaId.head == '`')
            "`U" + scalaId.substring(1)
          else
            "U" + scalaId
      }
    }
    else { // preserve behavior until v0.8.4
      // regular javaUtil.javaIdentifier as it might generate underscores:
      val id = javaUtil.javaIdentifier(symbol)
      // note: not scalaIdentifier because we are going to camelize anyway
      val parts = id.split("_")
      val name = parts.map(util.upperFirst).mkString
      if (name.nonEmpty) scalaIdentifier(name)
      else "U" + id.count(_ == '_') // named based on # of underscores ;)
    }
  }
}

object ScalaUtil {
  /**
    * The ones from Sect 1.1 of the Scala Language Spec, v2.9
    * plus `_`
    */
  val scalaReservedWords: List[String] = List(
    "_",
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
