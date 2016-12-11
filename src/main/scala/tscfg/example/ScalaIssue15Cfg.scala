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
