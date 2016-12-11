package tscfg.example

case class ScalaIssue15dCfg(
  baz : scala.List[scala.List[ScalaIssue15dCfg.Baz$Elm]]
)
object ScalaIssue15dCfg {
  case class Baz$Elm(
    aa : scala.Option[scala.Boolean],
    dd : scala.Double
  )
  object Baz$Elm {
    def apply(c: com.typesafe.config.Config): ScalaIssue15dCfg.Baz$Elm = {
      ScalaIssue15dCfg.Baz$Elm(
        aa = if(c.hasPathOrNull("aa")) Some(c.getBoolean("aa")) else None,
        dd = c.getDouble("dd")
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue15dCfg = {
    ScalaIssue15dCfg(
      baz = $_L$_LScalaIssue15dCfg_Baz$Elm(c.getList("baz"))
    )
  }
  private def $_L$_LScalaIssue15dCfg_Baz$Elm(cl:com.typesafe.config.ConfigList): scala.List[scala.List[ScalaIssue15dCfg.Baz$Elm]] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $_LScalaIssue15dCfg_Baz$Elm(cv.asInstanceOf[com.typesafe.config.ConfigList])).toList
  }
  private def $_LScalaIssue15dCfg_Baz$Elm(cl:com.typesafe.config.ConfigList): scala.List[ScalaIssue15dCfg.Baz$Elm] = {
    import scala.collection.JavaConversions._
    cl.map(cv => ScalaIssue15dCfg.Baz$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
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
