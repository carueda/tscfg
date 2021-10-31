package tscfg.example

final case class ScalaIssue15cCfg(
    positions: scala.List[ScalaIssue15cCfg.Positions$Elm],
    qaz: ScalaIssue15cCfg.Qaz
)
object ScalaIssue15cCfg {
  final case class Positions$Elm(
      attrs: scala.List[scala.List[ScalaIssue15cCfg.Positions$Elm.Attrs$Elm]],
      lat: scala.Double,
      lon: scala.Double
  )
  object Positions$Elm {
    final case class Attrs$Elm(
        foo: scala.Long
    )
    object Attrs$Elm {
      def apply(
          c: com.typesafe.config.Config,
          parentPath: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): ScalaIssue15cCfg.Positions$Elm.Attrs$Elm = {
        ScalaIssue15cCfg.Positions$Elm.Attrs$Elm(
          foo = $_reqLng(parentPath, c, "foo", $tsCfgValidator)
        )
      }
      private def $_reqLng(
          parentPath: java.lang.String,
          c: com.typesafe.config.Config,
          path: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): scala.Long = {
        if (c == null) 0
        else
          try c.getLong(path)
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
    ): ScalaIssue15cCfg.Positions$Elm = {
      ScalaIssue15cCfg.Positions$Elm(
        attrs = $_L$_LScalaIssue15cCfg_Positions$Elm_Attrs$Elm(
          c.getList("attrs"),
          parentPath,
          $tsCfgValidator
        ),
        lat = $_reqDbl(parentPath, c, "lat", $tsCfgValidator),
        lon = $_reqDbl(parentPath, c, "lon", $tsCfgValidator)
      )
    }
    private def $_L$_LScalaIssue15cCfg_Positions$Elm_Attrs$Elm(
        cl: com.typesafe.config.ConfigList,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): scala.List[scala.List[ScalaIssue15cCfg.Positions$Elm.Attrs$Elm]] = {
      import scala.jdk.CollectionConverters._
      cl.asScala
        .map(cv =>
          $_LScalaIssue15cCfg_Positions$Elm_Attrs$Elm(
            cv.asInstanceOf[com.typesafe.config.ConfigList],
            parentPath,
            $tsCfgValidator
          )
        )
        .toList
    }
    private def $_LScalaIssue15cCfg_Positions$Elm_Attrs$Elm(
        cl: com.typesafe.config.ConfigList,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): scala.List[ScalaIssue15cCfg.Positions$Elm.Attrs$Elm] = {
      import scala.jdk.CollectionConverters._
      cl.asScala
        .map(cv =>
          ScalaIssue15cCfg.Positions$Elm.Attrs$Elm(
            cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig,
            parentPath,
            $tsCfgValidator
          )
        )
        .toList
    }
    private def $_reqDbl(
        parentPath: java.lang.String,
        c: com.typesafe.config.Config,
        path: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): scala.Double = {
      if (c == null) 0
      else
        try c.getDouble(path)
        catch {
          case e: com.typesafe.config.ConfigException =>
            $tsCfgValidator.addBadPath(parentPath + path, e)
            0
        }
    }

  }

  final case class Qaz(
      aa: ScalaIssue15cCfg.Qaz.Aa
  )
  object Qaz {
    final case class Aa(
        bb: scala.List[ScalaIssue15cCfg.Qaz.Aa.Bb$Elm]
    )
    object Aa {
      final case class Bb$Elm(
          cc: java.lang.String
      )
      object Bb$Elm {
        def apply(
            c: com.typesafe.config.Config,
            parentPath: java.lang.String,
            $tsCfgValidator: $TsCfgValidator
        ): ScalaIssue15cCfg.Qaz.Aa.Bb$Elm = {
          ScalaIssue15cCfg.Qaz.Aa.Bb$Elm(
            cc = $_reqStr(parentPath, c, "cc", $tsCfgValidator)
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
      ): ScalaIssue15cCfg.Qaz.Aa = {
        ScalaIssue15cCfg.Qaz.Aa(
          bb = $_LScalaIssue15cCfg_Qaz_Aa_Bb$Elm(c.getList("bb"), parentPath, $tsCfgValidator)
        )
      }
      private def $_LScalaIssue15cCfg_Qaz_Aa_Bb$Elm(
          cl: com.typesafe.config.ConfigList,
          parentPath: java.lang.String,
          $tsCfgValidator: $TsCfgValidator
      ): scala.List[ScalaIssue15cCfg.Qaz.Aa.Bb$Elm] = {
        import scala.jdk.CollectionConverters._
        cl.asScala
          .map(cv =>
            ScalaIssue15cCfg.Qaz.Aa.Bb$Elm(
              cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig,
              parentPath,
              $tsCfgValidator
            )
          )
          .toList
      }
    }

    def apply(
        c: com.typesafe.config.Config,
        parentPath: java.lang.String,
        $tsCfgValidator: $TsCfgValidator
    ): ScalaIssue15cCfg.Qaz = {
      ScalaIssue15cCfg.Qaz(
        aa = ScalaIssue15cCfg.Qaz.Aa(
          if (c.hasPathOrNull("aa")) c.getConfig("aa")
          else com.typesafe.config.ConfigFactory.parseString("aa{}"),
          parentPath + "aa.",
          $tsCfgValidator
        )
      )
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaIssue15cCfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String     = ""
    val $result = ScalaIssue15cCfg(
      positions =
        $_LScalaIssue15cCfg_Positions$Elm(c.getList("positions"), parentPath, $tsCfgValidator),
      qaz = ScalaIssue15cCfg.Qaz(
        if (c.hasPathOrNull("qaz")) c.getConfig("qaz")
        else com.typesafe.config.ConfigFactory.parseString("qaz{}"),
        parentPath + "qaz.",
        $tsCfgValidator
      )
    )
    $tsCfgValidator.validate()
    $result
  }
  private def $_LScalaIssue15cCfg_Positions$Elm(
      cl: com.typesafe.config.ConfigList,
      parentPath: java.lang.String,
      $tsCfgValidator: $TsCfgValidator
  ): scala.List[ScalaIssue15cCfg.Positions$Elm] = {
    import scala.jdk.CollectionConverters._
    cl.asScala
      .map(cv =>
        ScalaIssue15cCfg.Positions$Elm(
          cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig,
          parentPath,
          $tsCfgValidator
        )
      )
      .toList
  }
  final class $TsCfgValidator {
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
