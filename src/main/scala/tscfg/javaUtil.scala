package tscfg

import tscfg.specs.types.{AtomicType, SpecType}
import tscfg.specs.types._

object javaUtil {

  /**
    * Returns a valid Java identifier from the given symbol:
    *
    * - appends a '_' in case the symbol is a java keyword or special literal ("null", "true", "false");
    * - otherwise, prepends a '_' in case the symbol is numerical literal;
    * - otherwise, returns the given symbol if already a valid java identifier;
    * - otherwise, prefixes the symbol with '_' if first character is valid but not at first position, and
    *   replaces any character that cannot be part of a java identifier with '_'.
    */
  def javaIdentifier(symbol: String): String = {
    if (javaKeywords.contains(symbol))
      symbol + "_"
    else if (isJavaIdentifier(symbol))
      symbol
    else {
      val c0 = symbol.charAt(0)
      if (Character.isDigit(c0))
        "_" + symbol
      else {
        val first: String = if (Character.isJavaIdentifierStart(c0)) String.valueOf(c0)
        else if (Character.isJavaIdentifierPart(c0)) "_" + c0 else "_"
        val rest = symbol.substring(1) map { c =>
          if (Character.isJavaIdentifierPart(c)) c else '_'
        }
        first + rest
      }
    }
  }

  def isJavaIdentifier(symbol: String): Boolean = {
    Character.isJavaIdentifierStart(symbol.charAt(0)) &&
      symbol.substring(1).forall(Character.isJavaIdentifierPart)
  }

  def getClassName(symbol:String) = upperFirst(javaIdentifier(symbol))

  private def upperFirst(symbol:String) = symbol.charAt(0).toUpper + symbol.substring(1)

  /**
    * Set of java keywords plus the literals "null", "true", "false".
    * (from Sect 3.9 of the Java Language Spec, Java SE 8 Edition)
    */
  val javaKeywords: List[String] = List(
    "abstract", "continue", "for",        "new",       "switch",
    "assert",   "default",  "if",         "package",   "synchronized",
    "boolean",  "do",       "goto",       "private",   "this",
    "break",    "double",   "implements", "protected", "throw",
    "byte",     "else",     "import",     "public",    "throws",
    "case",     "enum",     "instanceof", "return",    "transient",
    "catch",    "extends",  "int",        "short",     "try",
    "char",     "final",    "interface",  "static",    "void",
    "class",    "finally",  "long",       "strictfp",  "volatile",
    "const",    "float",    "native",     "super",     "while",

    "null",     "true",     "false"
  )

  val TypesafeConfigClassName = classOf[com.typesafe.config.Config].getName
}
