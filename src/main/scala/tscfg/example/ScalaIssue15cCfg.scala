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
}
