package misc

import com.typesafe.config.ConfigFactory

/*
   See https://github.com/carueda/tscfg/issues/41#issuecomment-449521552

   sbt> runMain misc.QuotedUnquoted
    input line          cv.toString   cv.valueType
   -------------- ---------------- --------------
   sizeU: 50G        Quoted("50G")         STRING
   sizeQ: "50G"      Quoted("50G")         STRING
   fooU:  foo      Unquoted("foo")         STRING
   fooQ:  "foo"      Quoted("foo")         STRING
*/
object QuotedUnquoted {
  def main(args: Array[String]): Unit = {
    val inputLines = List(
      """ sizeU: 50G   """,
      """ sizeQ: "50G" """,
      """ fooU:  foo   """,
      """ fooQ:  "foo" """
    )
    val config = ConfigFactory.parseString(inputLines.mkString("\n"))

    println(" %-14s %16s %14s" format("input line", "cv.toString", "cv.valueType"))
    println(" %-14s %16s %14s" format("-"*14, "-"*16, "-"*14))
    for ((key, inputLine) ← List("sizeU", "sizeQ", "fooU", "fooQ") zip inputLines) {
      val cv = config.getValue(key)
      println(" %-14s %16s %14s" format(inputLine.trim, cv.toString, cv.valueType()))
    }
  }
}
