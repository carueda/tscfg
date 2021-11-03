package tscfg.example

final case class ScalaIssue41Cfg(
  a : scala.Option[scala.Int],
  b : scala.Int,
  c : ScalaIssue41Cfg.C,
  i : scala.Option[scala.List[scala.Double]]
)
object ScalaIssue41Cfg {
  final case class C(
    d : scala.Option[java.lang.String],
    e : java.lang.String,
    f : scala.Option[ScalaIssue41Cfg.C.F]
  )
  object C {
    final case class F(
      g : scala.Int,
      h : java.lang.String
    )
    object F {
      def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue41Cfg.C.F = {
        ScalaIssue41Cfg.C.F(
          g = $_reqInt(parentPath, c, "g", $tsCfgValidator),
          h = $_reqStr(parentPath, c, "h", $tsCfgValidator)
        )
      }
      private def $_reqInt(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.Int = {
        if (c == null) 0
        else try c.getInt(path)
        catch {
          case e:com.typesafe.config.ConfigException =>
            $tsCfgValidator.addBadPath(parentPath + path, e)
            0
        }
      }
    
      private def $_reqStr(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): java.lang.String = {
        if (c == null) null
        else try c.getString(path)
        catch {
          case e:com.typesafe.config.ConfigException =>
            $tsCfgValidator.addBadPath(parentPath + path, e)
            null
        }
      }
    
    }
          
    def apply(c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): ScalaIssue41Cfg.C = {
      ScalaIssue41Cfg.C(
        d = if(c.hasPathOrNull("d")) Some(c.getString("d")) else None,
        e = if(c.hasPathOrNull("e")) c.getString("e") else "hello",
        f = if(c.hasPathOrNull("f")) scala.Some(ScalaIssue41Cfg.C.F(c.getConfig("f"), parentPath + "f.", $tsCfgValidator)) else None
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): ScalaIssue41Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue41Cfg(
      a = if(c.hasPathOrNull("a")) Some(c.getInt("a")) else None,
      b = if(c.hasPathOrNull("b")) c.getInt("b") else 10,
      c = ScalaIssue41Cfg.C(if(c.hasPathOrNull("c")) c.getConfig("c") else com.typesafe.config.ConfigFactory.parseString("c{}"), parentPath + "c.", $tsCfgValidator),
      i = if(c.hasPathOrNull("i")) scala.Some($_L$_dbl(c.getList("i"), parentPath, $tsCfgValidator)) else None
    )
    $tsCfgValidator.validate()
    $result
  }

  private def $_L$_dbl(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[scala.Double] = {
    import scala.jdk.CollectionConverters._
    cl.asScala.map(cv => $_dbl(cv)).toList
  }
  private def $_dbl(cv:com.typesafe.config.ConfigValue): scala.Double = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
      !u.isInstanceOf[java.lang.Number]) throw $_expE(cv, "double")
    u.asInstanceOf[java.lang.Number].doubleValue()
  }

  private def $_expE(cv:com.typesafe.config.ConfigValue, exp:java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(s"${cv.origin.lineNumber}: " +
      "expecting: " + exp + " got: " +
      (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u))
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
