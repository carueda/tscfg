package tscfg.example

final case class ScalaIssue71Cfg(
    example: ScalaIssue71Cfg.Example
)
object ScalaIssue71Cfg {
  final case class Shared(
      c: java.lang.String,
      d: ScalaIssue71Cfg.Shared.D
  )
  object Shared {
    final case class D(
        e: scala.Int
    )
    object D {
      def apply(
          c: com.typesafe.config.Config,
          parentPath: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): ScalaIssue71Cfg.Shared.D = {
        ScalaIssue71Cfg.Shared.D(
          e = $_reqInt(parentPath, c, "e", $tsCfgValidator)
        )
      }
      private def $_reqInt(
          parentPath: java.lang.String,
          c: com.typesafe.config.Config,
          path: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): scala.Int = {
        if (c == null) 0
        else
          try c.getInt(path)
          catch {
            case e: com.typesafe.config.ConfigException =>
              $tsCfgValidator.addBadPath(parentPath + path, e)
              0
          }
      }

    }

    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue71Cfg.Shared = {
      ScalaIssue71Cfg.Shared(
        c = $_reqStr(parentPath, c, "c", $tsCfgValidator),
        d = ScalaIssue71Cfg.Shared.D(
          if (c.hasPathOrNull("d")) c.getConfig("d")
          else com.typesafe.config.ConfigFactory.parseString("d{}"),
          parentPath + "d.",
          $tsCfgValidator
        )
      )
    }
    private def $_reqStr(
        parentPath: java.lang.String,
        c: com.typesafe.config.Config,
        path: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): java.lang.String = {
      if (c == null) null
      else
        try c.getString(path)
        catch {
          case e: com.typesafe.config.ConfigException =>
            $tsCfgValidator.addBadPath(parentPath + path, e)
            null
        }
    }

  }

  final case class Shared2(
      dd: java.lang.String,
      dddd: ScalaIssue71Cfg.Shared2.Dddd
  )
  object Shared2 {
    final case class Dddd(
        eeee: scala.Int
    )
    object Dddd {
      def apply(
          c: com.typesafe.config.Config,
          parentPath: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): ScalaIssue71Cfg.Shared2.Dddd = {
        ScalaIssue71Cfg.Shared2.Dddd(
          eeee = $_reqInt(parentPath, c, "eeee", $tsCfgValidator)
        )
      }
      private def $_reqInt(
          parentPath: java.lang.String,
          c: com.typesafe.config.Config,
          path: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): scala.Int = {
        if (c == null) 0
        else
          try c.getInt(path)
          catch {
            case e: com.typesafe.config.ConfigException =>
              $tsCfgValidator.addBadPath(parentPath + path, e)
              0
          }
      }

    }

    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue71Cfg.Shared2 = {
      ScalaIssue71Cfg.Shared2(
        dd = $_reqStr(parentPath, c, "dd", $tsCfgValidator),
        dddd = ScalaIssue71Cfg.Shared2.Dddd(
          if (c.hasPathOrNull("dddd")) c.getConfig("dddd")
          else com.typesafe.config.ConfigFactory.parseString("dddd{}"),
          parentPath + "dddd.",
          $tsCfgValidator
        )
      )
    }
    private def $_reqStr(
        parentPath: java.lang.String,
        c: com.typesafe.config.Config,
        path: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): java.lang.String = {
      if (c == null) null
      else
        try c.getString(path)
        catch {
          case e: com.typesafe.config.ConfigException =>
            $tsCfgValidator.addBadPath(parentPath + path, e)
            null
        }
    }

  }

  final case class Example(
      a: ScalaIssue71Cfg.Shared,
      b: scala.List[ScalaIssue71Cfg.Shared],
      c: scala.List[ScalaIssue71Cfg.Shared2]
  )
  object Example {
    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue71Cfg.Example = {
      ScalaIssue71Cfg.Example(
        a = ScalaIssue71Cfg.Shared(
          if (c.hasPathOrNull("a")) c.getConfig("a")
          else com.typesafe.config.ConfigFactory.parseString("a{}"),
          parentPath + "a.",
          $tsCfgValidator
        ),
        b = $_LScalaIssue71Cfg_Shared(c.getList("b"), parentPath, $tsCfgValidator),
        c = $_LScalaIssue71Cfg_Shared2(c.getList("c"), parentPath, $tsCfgValidator)
      )
    }
    private def $_LScalaIssue71Cfg_Shared(
        cl: com.typesafe.config.ConfigList,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): scala.List[ScalaIssue71Cfg.Shared] = {
      import scala.jdk.CollectionConverters._
      cl.asScala
        .map(cv =>
          ScalaIssue71Cfg.Shared(
            cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig,
            parentPath,
            $tsCfgValidator
          )
        )
        .toList
    }
    private def $_LScalaIssue71Cfg_Shared2(
        cl: com.typesafe.config.ConfigList,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): scala.List[ScalaIssue71Cfg.Shared2] = {
      import scala.jdk.CollectionConverters._
      cl.asScala
        .map(cv =>
          ScalaIssue71Cfg.Shared2(
            cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig,
            parentPath,
            $tsCfgValidator
          )
        )
        .toList
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaIssue71Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue71Cfg(
      example = ScalaIssue71Cfg.Example(
        if (c.hasPathOrNull("example")) c.getConfig("example")
        else com.typesafe.config.ConfigFactory.parseString("example{}"),
        parentPath + "example.",
        $tsCfgValidator
      )
    )
    $tsCfgValidator.validate()
    $result
  }
  private final class $TsCfgValidator {
    private val badPaths = scala.collection.mutable.ArrayBuffer[java.lang.String]()

    def addBadPath(path: java.lang.String, e: com.typesafe.config.ConfigException): Unit = {
      badPaths += s"'$path': ${e.getClass.getName}(${e.getMessage})"
    }

    def addInvalidEnumValue(
        path: java.lang.String,
        value: java.lang.String,
        enumName: java.lang.String
    ): Unit = {
      badPaths += s"'$path': invalid value $value for enumeration $enumName"
    }

    def validate(): Unit = {
      if (badPaths.nonEmpty) {
        throw new com.typesafe.config.ConfigException(
          badPaths.mkString("Invalid configuration:\n    ", "\n    ", "")
        ) {}
      }
    }
  }
}
