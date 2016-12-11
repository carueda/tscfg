package tscfg.example

case class ScalaIssue15cCfg(
  positions : scala.List[ScalaIssue15cCfg.Positions$Elm],
  qaz       : ScalaIssue15cCfg.Qaz
)
object ScalaIssue15cCfg {
  case class Positions$Elm(
    attrs : scala.List[scala.List[ScalaIssue15cCfg.Positions$Elm.Attrs$Elm]],
    lat   : scala.Double,
    lon   : scala.Double
  )
  object Positions$Elm {
    case class Attrs$Elm(
      foo : scala.Long
    )
    object Attrs$Elm {
      def apply(c: com.typesafe.config.Config): ScalaIssue15cCfg.Positions$Elm.Attrs$Elm = {
        ScalaIssue15cCfg.Positions$Elm.Attrs$Elm(
          foo = c.getLong("foo")
        )
      }
    }
          
    def apply(c: com.typesafe.config.Config): ScalaIssue15cCfg.Positions$Elm = {
      ScalaIssue15cCfg.Positions$Elm(
        attrs = $_L$_LScalaIssue15cCfg_Positions$Elm_Attrs$Elm(c.getList("attrs")),
        lat   = c.getDouble("lat"),
        lon   = c.getDouble("lon")
      )
    }
    private def $_L$_LScalaIssue15cCfg_Positions$Elm_Attrs$Elm(cl:com.typesafe.config.ConfigList): scala.List[scala.List[ScalaIssue15cCfg.Positions$Elm.Attrs$Elm]] = {
      import scala.collection.JavaConversions._
      cl.map(cv => $_LScalaIssue15cCfg_Positions$Elm_Attrs$Elm(cv.asInstanceOf[com.typesafe.config.ConfigList])).toList
    }
    private def $_LScalaIssue15cCfg_Positions$Elm_Attrs$Elm(cl:com.typesafe.config.ConfigList): scala.List[ScalaIssue15cCfg.Positions$Elm.Attrs$Elm] = {
      import scala.collection.JavaConversions._
      cl.map(cv => ScalaIssue15cCfg.Positions$Elm.Attrs$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
    }
  }
        
  case class Qaz(
    aa : ScalaIssue15cCfg.Qaz.Aa
  )
  object Qaz {
    case class Aa(
      bb : scala.List[ScalaIssue15cCfg.Qaz.Aa.Bb$Elm]
    )
    object Aa {
      case class Bb$Elm(
        cc : java.lang.String
      )
      object Bb$Elm {
        def apply(c: com.typesafe.config.Config): ScalaIssue15cCfg.Qaz.Aa.Bb$Elm = {
          ScalaIssue15cCfg.Qaz.Aa.Bb$Elm(
            cc = c.getString("cc")
          )
        }
      }
            
      def apply(c: com.typesafe.config.Config): ScalaIssue15cCfg.Qaz.Aa = {
        ScalaIssue15cCfg.Qaz.Aa(
          bb = $_LScalaIssue15cCfg_Qaz_Aa_Bb$Elm(c.getList("bb"))
        )
      }
      private def $_LScalaIssue15cCfg_Qaz_Aa_Bb$Elm(cl:com.typesafe.config.ConfigList): scala.List[ScalaIssue15cCfg.Qaz.Aa.Bb$Elm] = {
        import scala.collection.JavaConversions._
        cl.map(cv => ScalaIssue15cCfg.Qaz.Aa.Bb$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
      }
    }
          
    def apply(c: com.typesafe.config.Config): ScalaIssue15cCfg.Qaz = {
      ScalaIssue15cCfg.Qaz(
        aa = ScalaIssue15cCfg.Qaz.Aa(c.getConfig("aa"))
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue15cCfg = {
    ScalaIssue15cCfg(
      positions = $_LScalaIssue15cCfg_Positions$Elm(c.getList("positions")),
      qaz       = ScalaIssue15cCfg.Qaz(c.getConfig("qaz"))
    )
  }
  private def $_LScalaIssue15cCfg_Positions$Elm(cl:com.typesafe.config.ConfigList): scala.List[ScalaIssue15cCfg.Positions$Elm] = {
    import scala.collection.JavaConversions._
    cl.map(cv => ScalaIssue15cCfg.Positions$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
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
