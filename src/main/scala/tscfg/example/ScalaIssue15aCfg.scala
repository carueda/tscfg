package tscfg.example

object ScalaIssue15aCfg {
  def apply(c: com.typesafe.config.Config): ScalaIssue15aCfg = {
    ScalaIssue15aCfg(
      $list$int(c.getList("ii"))
    )
  }

  private def $list$int(cl: com.typesafe.config.ConfigList): List[Int] = {
    import scala.collection.JavaConversions._
    cl.map($int).toList
  }

  private def $int(cv: com.typesafe.config.ConfigValue): Int = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER)
      || !u.isInstanceOf[Integer]) throw $expE(cv, "integer")
    u.asInstanceOf[Integer]
  }

  private def $expE(cv: com.typesafe.config.ConfigValue, exp: String): RuntimeException = {
    val u: Any = cv.unwrapped
    new RuntimeException(cv.origin.lineNumber +
      ": expecting: " + exp + " got: " +
      (if (u.isInstanceOf[String]) "\"" + u + "\"" else u))
  }
}
case class ScalaIssue15aCfg(
  ii : scala.collection.immutable.List[scala.Int]
)

