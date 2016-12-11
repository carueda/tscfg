package tscfg.example

case class ScalaIssue15Cfg(
  positions : scala.List[ScalaIssue15Cfg.Positions$Elm]
)
object ScalaIssue15Cfg {
  case class Positions$Elm(
    numbers   : scala.List[scala.Int],
    positions : scala.List[scala.List[ScalaIssue15Cfg.Positions$Elm.Positions$Elm]]
  )
  object Positions$Elm {
    case class Positions$Elm(
      other : scala.Int,
      stuff : java.lang.String
    )
    object Positions$Elm {
      def apply(c: com.typesafe.config.Config): ScalaIssue15Cfg.Positions$Elm.Positions$Elm = {
        ScalaIssue15Cfg.Positions$Elm.Positions$Elm(
          other = c.getInt("other"),
          stuff = c.getString("stuff")
        )
      }
    }
          
    def apply(c: com.typesafe.config.Config): ScalaIssue15Cfg.Positions$Elm = {
      ScalaIssue15Cfg.Positions$Elm(
        numbers   = $_L$_int(c.getList("numbers")),
        positions = $_L$_LScalaIssue15Cfg_Positions$Elm_Positions$Elm(c.getList("positions"))
      )
    }
    private def $_L$_LScalaIssue15Cfg_Positions$Elm_Positions$Elm(cl:com.typesafe.config.ConfigList): scala.List[scala.List[ScalaIssue15Cfg.Positions$Elm.Positions$Elm]] = {
      import scala.collection.JavaConversions._
      cl.map(cv => $_LScalaIssue15Cfg_Positions$Elm_Positions$Elm(cv.asInstanceOf[com.typesafe.config.ConfigList])).toList
    }
    private def $_LScalaIssue15Cfg_Positions$Elm_Positions$Elm(cl:com.typesafe.config.ConfigList): scala.List[ScalaIssue15Cfg.Positions$Elm.Positions$Elm] = {
      import scala.collection.JavaConversions._
      cl.map(cv => ScalaIssue15Cfg.Positions$Elm.Positions$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue15Cfg = {
    ScalaIssue15Cfg(
      positions = $_LScalaIssue15Cfg_Positions$Elm(c.getList("positions"))
    )
  }
  private def $_LScalaIssue15Cfg_Positions$Elm(cl:com.typesafe.config.ConfigList): scala.List[ScalaIssue15Cfg.Positions$Elm] = {
    import scala.collection.JavaConversions._
    cl.map(cv => ScalaIssue15Cfg.Positions$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
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
