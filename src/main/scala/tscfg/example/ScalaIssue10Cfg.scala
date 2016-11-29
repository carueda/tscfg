package tscfg.example

case class ScalaIssue10Cfg(
  main : ScalaIssue10Cfg.Main
)

object ScalaIssue10Cfg {
  case class Main(
    email : scala.Option[Main.Email]
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
    def apply(c: com.typesafe.config.Config): Main = {
      Main(
        if(c.hasPathOrNull("email")) scala.Some(Email(c.getConfig("email"))) else None
      )
    }
  }
  def apply(c: com.typesafe.config.Config): ScalaIssue10Cfg = {
    ScalaIssue10Cfg(
      Main(c.getConfig("main"))
    )
  }
}
