package tscfg.example

final case class ScalaIssue54bCfg(
    root: ScalaIssue54bCfg.Root
)
object ScalaIssue54bCfg {
  final case class Root(
      e: Root.Shared,
      f: scala.List[Root.Shared]
  )
  object Root {
    final case class Shared(
        b: java.lang.String,
        c: ScalaIssue54bCfg.Root.Shared.C
    )
    object Shared {
      final case class C(
          d: scala.Int
      )
      object C {
        def apply(
            c: com.typesafe.config.Config,
            parentPath: java.lang.String,
            $tsCfgValidator: $TsCfgValidator
        ): ScalaIssue54bCfg.Root.Shared.C = {
          ScalaIssue54bCfg.Root.Shared.C(
            d = $_reqInt(parentPath, c, "d", $tsCfgValidator)
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
      ): ScalaIssue54bCfg.Root.Shared = {
        ScalaIssue54bCfg.Root.Shared(
          b = $_reqStr(parentPath, c, "b", $tsCfgValidator),
          c = ScalaIssue54bCfg.Root.Shared.C(
            if (c.hasPathOrNull("c")) c.getConfig("c")
            else com.typesafe.config.ConfigFactory.parseString("c{}"),
            parentPath + "c.",
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

    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue54bCfg.Root = {
      ScalaIssue54bCfg.Root(
        e = Root.Shared(
          if (c.hasPathOrNull("e")) c.getConfig("e")
          else com.typesafe.config.ConfigFactory.parseString("e{}"),
          parentPath + "e.",
          $tsCfgValidator
        ),
        f = $_LRoot_Shared(c.getList("f"), parentPath, $tsCfgValidator)
      )
    }
    private def $_LRoot_Shared(
        cl: com.typesafe.config.ConfigList,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): scala.List[Root.Shared] = {
      import scala.jdk.CollectionConverters._
      cl.asScala
        .map(cv =>
          Root.Shared(
            cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig,
            parentPath,
            $tsCfgValidator
          )
        )
        .toList
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaIssue54bCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue54bCfg(
      root = ScalaIssue54bCfg.Root(
        if (c.hasPathOrNull("root")) c.getConfig("root")
        else com.typesafe.config.ConfigFactory.parseString("root{}"),
        parentPath + "root.",
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
