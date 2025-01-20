package tscfg.example

final case class ScalaIssue328Cfg(
  Some : ScalaIssue328Cfg.Some
)
object ScalaIssue328Cfg {
  final case class Some(
    intOpt : scala.Option[scala.Int]
  )
  object Some {
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue328Cfg.Some = {
      ScalaIssue328Cfg.Some(
        intOpt = if(c.hasPathOrNull("intOpt")) scala.Some(c.getInt("intOpt")) else scala.None
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue328Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue328Cfg(
      Some = ScalaIssue328Cfg.Some(if(c.hasPathOrNull("Some")) c.getConfig("Some") else com.typesafe.config.ConfigFactory.parseString("Some{}"), parentPath + "Some.", $tsCfgValidator)
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
