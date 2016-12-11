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
    def apply(c: com.typesafe.config.Config): ScalaIssue12Cfg.Boolean = {
      ScalaIssue12Cfg.Boolean(
        bar = if(c.hasPathOrNull("bar")) c.getString("bar") else "foo"
      )
    }
  }
        
  case class Option(
    bar : java.lang.String
  )
  object Option {
    def apply(c: com.typesafe.config.Config): ScalaIssue12Cfg.Option = {
      ScalaIssue12Cfg.Option(
        bar = if(c.hasPathOrNull("bar")) c.getString("bar") else "baz"
      )
    }
  }
        
  case class String(
    bar : java.lang.String
  )
  object String {
    def apply(c: com.typesafe.config.Config): ScalaIssue12Cfg.String = {
      ScalaIssue12Cfg.String(
        bar = if(c.hasPathOrNull("bar")) c.getString("bar") else "baz"
      )
    }
  }
        
  case class Int(
    bar : scala.Int
  )
  object Int {
    def apply(c: com.typesafe.config.Config): ScalaIssue12Cfg.Int = {
      ScalaIssue12Cfg.Int(
        bar = if(c.hasPathOrNull("bar")) c.getInt("bar") else 1
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue12Cfg = {
    ScalaIssue12Cfg(
      Boolean = ScalaIssue12Cfg.Boolean(c.getConfig("Boolean")),
      Option  = ScalaIssue12Cfg.Option(c.getConfig("Option")),
      String  = ScalaIssue12Cfg.String(c.getConfig("String")),
      int     = ScalaIssue12Cfg.Int(c.getConfig("int"))
    )
  }
}
