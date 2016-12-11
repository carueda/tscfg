package tscfg.example

case class ScalaIssue15bCfg(
  booleans : scala.List[scala.Boolean],
  doubles  : scala.List[scala.Double],
  integers : scala.List[scala.List[scala.Int]],
  longs    : scala.List[scala.Long],
  strings  : scala.List[java.lang.String]
)
object ScalaIssue15bCfg {
  def apply(c: com.typesafe.config.Config): ScalaIssue15bCfg = {
    ScalaIssue15bCfg(
      booleans = $_L$_bln(c.getList("booleans")),
      doubles  = $_L$_dbl(c.getList("doubles")),
      integers = $_L$_L$_int(c.getList("integers")),
      longs    = $_L$_lng(c.getList("longs")),
      strings  = $_L$_str(c.getList("strings"))
    )
  }

  private def $_L$_L$_int(cl:com.typesafe.config.ConfigList): scala.List[scala.List[scala.Int]] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_L$_int(cv.asInstanceOf[com.typesafe.config.ConfigList])).toList
  }
  private def $_L$_bln(cl:com.typesafe.config.ConfigList): scala.List[scala.Boolean] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_bln(cv)).toList
  }
  private def $_L$_dbl(cl:com.typesafe.config.ConfigList): scala.List[scala.Double] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_dbl(cv)).toList
  }
  private def $_L$_int(cl:com.typesafe.config.ConfigList): scala.List[scala.Int] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_int(cv)).toList
  }
  private def $_L$_lng(cl:com.typesafe.config.ConfigList): scala.List[scala.Long] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_lng(cv)).toList
  }
  private def $_L$_str(cl:com.typesafe.config.ConfigList): scala.List[java.lang.String] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_str(cv)).toList
  }
  private def $_bln(cv:com.typesafe.config.ConfigValue): scala.Boolean = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.BOOLEAN) ||
      !u.isInstanceOf[java.lang.Boolean]) throw $_expE(cv, "boolean")
    u.asInstanceOf[java.lang.Boolean].booleanValue()
  }
  private def $_dbl(cv:com.typesafe.config.ConfigValue): scala.Double = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
      !u.isInstanceOf[java.lang.Number]) throw $_expE(cv, "double")
    u.asInstanceOf[java.lang.Number].doubleValue()
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
  private def $_lng(cv:com.typesafe.config.ConfigValue): scala.Long = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
      !u.isInstanceOf[java.lang.Integer] && !u.isInstanceOf[java.lang.Long]) throw $_expE(cv, "long")
    u.asInstanceOf[java.lang.Number].longValue()
  }
  private def $_str(cv:com.typesafe.config.ConfigValue) =
    java.lang.String.valueOf(cv.unwrapped())
}
