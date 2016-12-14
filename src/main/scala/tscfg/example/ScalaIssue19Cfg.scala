package tscfg.example

case class ScalaIssue19Cfg(
  _$_foo_  : java.lang.String,
  _do_log_ : scala.Boolean
)
object ScalaIssue19Cfg {
  def apply(c: com.typesafe.config.Config): ScalaIssue19Cfg = {
    ScalaIssue19Cfg(
      _$_foo_  = if(c.hasPathOrNull("\"$_foo\"")) c.getString("\"$_foo\"") else "baz",
      _do_log_ = c.getBoolean("\"do log\"")
    )
  }
}
