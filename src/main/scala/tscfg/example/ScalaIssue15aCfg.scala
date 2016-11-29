package tscfg.example

case class ScalaIssue15aCfg(
  ii : scala.collection.immutable.List[scala.Int]
)

object ScalaIssue15aCfg {
  def apply(c: com.typesafe.config.Config): ScalaIssue15aCfg = {
    ScalaIssue15aCfg(
      $list$int(c.getList("ii"))
    )
  }

  private def $expE(cv:com.typesafe.config.ConfigValue, exp:java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(cv.origin.lineNumber +
      ": expecting: " + exp + " got: " +
      (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u))
  }
  private def $int(cv:com.typesafe.config.ConfigValue): scala.Int = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER)
      || !u.isInstanceOf[Integer]) throw $expE(cv, "integer")
    u.asInstanceOf[Integer]
  }
  private def $list$int(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[scala.Int] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $int(cv)).toList
  }
}
