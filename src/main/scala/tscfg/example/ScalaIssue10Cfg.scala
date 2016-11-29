package tscfg.example

case class ScalaIssue10Cfg(
  main : ScalaIssue10Cfg.Main
)

object ScalaIssue10Cfg {
  case class Main(
    email : scala.Option[Main.Email],
    reals : scala.Option[scala.collection.immutable.List[Main.Reals$Elm]]
  )

  object Main {
    case class Email(
      password : java.lang.String,
      server   : java.lang.String
    )

    object Email {
      def apply(c: com.typesafe.config.Config): Email = {
        Email(
          c.getString("password"),
          c.getString("server")
        )
      }
    }
    case class Reals$Elm(
      foo : scala.Double
    )

    object Reals$Elm {
      def apply(c: com.typesafe.config.Config): Reals$Elm = {
        Reals$Elm(
          c.getDouble("foo")
        )
      }
    }
    def apply(c: com.typesafe.config.Config): Main = {
      Main(
        if(c.hasPathOrNull("email")) scala.Some(Email(c.getConfig("email"))) else None,
        if(c.hasPathOrNull("reals")) scala.Some($listReals$Elm(c.getList("reals"))) else None
      )
    }

    private def $listReals$Elm(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[Reals$Elm] = {
      import scala.collection.JavaConversions._
      cl.map(cv => Reals$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
    }
  }
  def apply(c: com.typesafe.config.Config): ScalaIssue10Cfg = {
    ScalaIssue10Cfg(
      Main(c.getConfig("main"))
    )
  }
}
