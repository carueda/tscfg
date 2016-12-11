package tscfg.example

case class ScalaIssue10Cfg(
  main : ScalaIssue10Cfg.Main
)
object ScalaIssue10Cfg {
  case class Main(
    email : scala.Option[ScalaIssue10Cfg.Main.Email],
    reals : scala.Option[scala.List[ScalaIssue10Cfg.Main.Reals$Elm]]
  )
  object Main {
    case class Email(
      password : java.lang.String,
      server   : java.lang.String
    )
    object Email {
      def apply(c: com.typesafe.config.Config): ScalaIssue10Cfg.Main.Email = {
        ScalaIssue10Cfg.Main.Email(
          password = c.getString("password"),
          server   = c.getString("server")
        )
      }
    }
          
    case class Reals$Elm(
      foo : scala.Double
    )
    object Reals$Elm {
      def apply(c: com.typesafe.config.Config): ScalaIssue10Cfg.Main.Reals$Elm = {
        ScalaIssue10Cfg.Main.Reals$Elm(
          foo = c.getDouble("foo")
        )
      }
    }
          
    def apply(c: com.typesafe.config.Config): ScalaIssue10Cfg.Main = {
      ScalaIssue10Cfg.Main(
        email = if(c.hasPathOrNull("email")) scala.Some(ScalaIssue10Cfg.Main.Email(c.getConfig("email"))) else None,
        reals = if(c.hasPathOrNull("reals")) scala.Some($_LScalaIssue10Cfg_Main_Reals$Elm(c.getList("reals"))) else None
      )
    }
    private def $_LScalaIssue10Cfg_Main_Reals$Elm(cl:com.typesafe.config.ConfigList): scala.List[ScalaIssue10Cfg.Main.Reals$Elm] = {
      import scala.collection.JavaConversions._
      cl.map(cv => ScalaIssue10Cfg.Main.Reals$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue10Cfg = {
    ScalaIssue10Cfg(
      main = ScalaIssue10Cfg.Main(c.getConfig("main"))
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
