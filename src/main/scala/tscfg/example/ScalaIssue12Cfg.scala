package tscfg.example

case class ScalaIssue12Cfg(
  Boolean : ScalaIssue12Cfg.Boolean,
  Option  : ScalaIssue12Cfg.Option,
  String  : ScalaIssue12Cfg.String,
  int     : ScalaIssue12Cfg.Int
)

object ScalaIssue12Cfg {
  case class Boolean(
    bar : java.lang.String
  )

  object Boolean {
    def apply(c: com.typesafe.config.Config): Boolean = {
      Boolean(
        c.getString("bar")
      )
    }
  }
  case class Option(
    bar : java.lang.String
  )

  object Option {
    def apply(c: com.typesafe.config.Config): Option = {
      Option(
        c.getString("bar")
      )
    }
  }
  case class String(
    bar : java.lang.String
  )

  object String {
    def apply(c: com.typesafe.config.Config): String = {
      String(
        c.getString("bar")
      )
    }
  }
  case class Int(
    bar : scala.Int
  )

  object Int {
    def apply(c: com.typesafe.config.Config): Int = {
      Int(
        c.getInt("bar")
      )
    }
  }
  def apply(c: com.typesafe.config.Config): ScalaIssue12Cfg = {
    ScalaIssue12Cfg(
      Boolean(c.getConfig("Boolean")),
      Option(c.getConfig("Option")),
      String(c.getConfig("String")),
      Int(c.getConfig("int"))
    )
  }
}
