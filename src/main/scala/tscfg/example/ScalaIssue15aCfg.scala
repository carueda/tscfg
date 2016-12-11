package tscfg.example

case class ScalaIssue15aCfg(
  ii : scala.List[scala.Int]
)
object ScalaIssue15aCfg {
  def apply(c: com.typesafe.config.Config): ScalaIssue15aCfg = {
    ScalaIssue15aCfg(
      ii = $_L$_int(c.getList("ii"))
    )
  }

  private def $_L$_int(cl:com.typesafe.config.ConfigList): scala.List[scala.Int] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_int(cv)).toList
  }
  private def $_expE(cv:com.typesafe.config.ConfigValue, exp:java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(cv.origin.lineNumber +
      ": expecting: " + exp + " got: " +
      (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u))
  }
  private def $_int(cv:com.typesafe.config.ConfigValue): scala.Int = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
      !u.isInstanceOf[Integer]) throw $_expE(cv, "integer")
    u.asInstanceOf[Integer]
  }
}
