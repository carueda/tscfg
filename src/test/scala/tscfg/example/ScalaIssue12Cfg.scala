package tscfg.example

final case class ScalaIssue12Cfg(
  Boolean : ScalaIssue12Cfg.Boolean,
  Option  : ScalaIssue12Cfg.Option,
  String  : ScalaIssue12Cfg.String,
  int     : ScalaIssue12Cfg.Int
)
object ScalaIssue12Cfg {
  final case class Boolean(
    bar : java.lang.String
  )
  object Boolean {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue12Cfg.Boolean = {
      ScalaIssue12Cfg.Boolean(
        bar = if(c.hasPathOrNull("bar")) c.getString("bar") else "foo"
      )
    }
  }
        
  final case class Option(
    bar : java.lang.String
  )
  object Option {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue12Cfg.Option = {
      ScalaIssue12Cfg.Option(
        bar = if(c.hasPathOrNull("bar")) c.getString("bar") else "baz"
      )
    }
  }
        
  final case class String(
    bar : java.lang.String
  )
  object String {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue12Cfg.String = {
      ScalaIssue12Cfg.String(
        bar = if(c.hasPathOrNull("bar")) c.getString("bar") else "baz"
      )
    }
  }
        
  final case class Int(
    bar : scala.Int
  )
  object Int {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue12Cfg.Int = {
      ScalaIssue12Cfg.Int(
        bar = if(c.hasPathOrNull("bar")) c.getInt("bar") else 1
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue12Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue12Cfg(
      Boolean = ScalaIssue12Cfg.Boolean(if(c.hasPathOrNull("Boolean")) c.getConfig("Boolean") else com.typesafe.config.ConfigFactory.parseString("Boolean{}"), parentPath + "Boolean.", $tsCfgValidator),
      Option  = ScalaIssue12Cfg.Option(if(c.hasPathOrNull("Option")) c.getConfig("Option") else com.typesafe.config.ConfigFactory.parseString("Option{}"), parentPath + "Option.", $tsCfgValidator),
      String  = ScalaIssue12Cfg.String(if(c.hasPathOrNull("String")) c.getConfig("String") else com.typesafe.config.ConfigFactory.parseString("String{}"), parentPath + "String.", $tsCfgValidator),
      int     = ScalaIssue12Cfg.Int(if(c.hasPathOrNull("int")) c.getConfig("int") else com.typesafe.config.ConfigFactory.parseString("int{}"), parentPath + "int.", $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
  }
  final class $TsCfgValidator {
    private val badPaths = scala.collection.mutable.ArrayBuffer[java.lang.String]()

    def addBadPath(path: java.lang.String, e: com.typesafe.config.ConfigException): Unit = {
      badPaths += s"'$path': ${e.getClass.getName}(${e.getMessage})"
    }

    def addInvalidEnumValue(path: java.lang.String, value: java.lang.String, enumName: java.lang.String): Unit = {
      badPaths += s"'$path': invalid value $value for enumeration $enumName"
    }

    def validate(): Unit = {
      if (badPaths.nonEmpty) {
        throw new com.typesafe.config.ConfigException(
          badPaths.mkString("Invalid configuration:\n    ", "\n    ", "")
        ){}
      }
    }
  }
}
