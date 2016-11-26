package tscfg.example

object ScalaIssue15bCfg {
  def apply(c: com.typesafe.config.Config): ScalaIssue15bCfg = {
    ScalaIssue15bCfg(
      $list$bln(c.getList("booleans")),
      $list$dbl(c.getList("doubles")),
      $list$list$int(c.getList("integers")),
      $list$lng(c.getList("longs")),
      $list$str(c.getList("strings"))
    )
  }

  private def $bln(cv:com.typesafe.config.ConfigValue): scala.Boolean = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.BOOLEAN)
      || !u.isInstanceOf[java.lang.Boolean]) throw $expE(cv, "boolean")
    u.asInstanceOf[java.lang.Boolean].booleanValue()
  }
  private def $dbl(cv:com.typesafe.config.ConfigValue): scala.Double = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER)
      || !u.isInstanceOf[java.lang.Number]) throw $expE(cv, "double")
    u.asInstanceOf[java.lang.Number].doubleValue()
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
  private def $list$bln(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[scala.Boolean] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $bln(cv)).toList
  }
  private def $list$dbl(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[scala.Double] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $dbl(cv)).toList
  }
  private def $list$int(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[scala.Int] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $int(cv)).toList
  }
  private def $list$list$int(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[scala.collection.immutable.List[scala.Int]] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $list$int(cv.asInstanceOf[com.typesafe.config.ConfigList])).toList
  }
  private def $list$lng(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[scala.Long] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $lng(cv)).toList
  }
  private def $list$str(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[java.lang.String] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $str(cv)).toList
  }
  private def $lng(cv:com.typesafe.config.ConfigValue): scala.Long = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER)
      || !u.isInstanceOf[java.lang.Integer] && !u.isInstanceOf[java.lang.Long]) throw $expE(cv, "long")
    u.asInstanceOf[java.lang.Number].longValue()
  }
  private def $str(cv:com.typesafe.config.ConfigValue) =
    java.lang.String.valueOf(cv.unwrapped())
}
case class ScalaIssue15bCfg(
  booleans : scala.collection.immutable.List[scala.Boolean],
  doubles  : scala.collection.immutable.List[scala.Double],
  integers : scala.collection.immutable.List[scala.collection.immutable.List[scala.Int]],
  longs    : scala.collection.immutable.List[scala.Long],
  strings  : scala.collection.immutable.List[java.lang.String]
)

