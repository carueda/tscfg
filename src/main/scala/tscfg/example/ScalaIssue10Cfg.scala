package tscfg.example

case class ScalaIssue10Cfg(
  main : ScalaIssue10Cfg.Main
)
object ScalaIssue10Cfg {
  case class Main(
    email : scala.Option[Main.Email],
    reals : scala.Option[scala.List[Main.Reals$Elm]]
  )
  object Main {
    case class Email(
      password : java.lang.String,
      server   : java.lang.String
    )
    object Email {
      def apply(c: com.typesafe.config.Config): Email = {
        Email(
          password = c.getString("password"),
          server   = c.getString("server")
        )
      }
    }
          
    case class Reals$Elm(
      foo : scala.Double
    )
    object Reals$Elm {
      def apply(c: com.typesafe.config.Config): Reals$Elm = {
        Reals$Elm(
          foo = c.getDouble("foo")
        )
      }
    }
          
    def apply(c: com.typesafe.config.Config): Main = {
      Main(
        email = if(c.hasPathOrNull("email")) scala.Some(Main.Email(c.getConfig("email"))) else None,
        reals = if(c.hasPathOrNull("reals")) scala.Some($_LMain_Reals$Elm(c.getList("reals"))) else None
      )
    }
    private def $_LMain_Reals$Elm(cl:com.typesafe.config.ConfigList): scala.List[Main.Reals$Elm] = {
      import scala.collection.JavaConversions._
      cl.map(cv => Main.Reals$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue10Cfg = {
    ScalaIssue10Cfg(
      main = ScalaIssue10Cfg.Main(c.getConfig("main"))
    )
  }
}
