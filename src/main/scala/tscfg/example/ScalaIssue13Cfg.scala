package tscfg.example

case class ScalaIssue13Cfg(
  issue : ScalaIssue13Cfg.Issue
)

object ScalaIssue13Cfg {
  case class Issue(
    optionalFoo : scala.Option[java.lang.String]
  )

  object Issue {
    def apply(c: com.typesafe.config.Config): Issue = {
      Issue(
        if(c.hasPathOrNull("optionalFoo")) Some(c.getString("optionalFoo")) else None
      )
    }
  }
  def apply(c: com.typesafe.config.Config): ScalaIssue13Cfg = {
    ScalaIssue13Cfg(
      Issue(c.getConfig("issue"))
    )
  }
}
