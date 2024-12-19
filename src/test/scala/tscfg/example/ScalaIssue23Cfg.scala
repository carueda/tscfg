package tscfg.example

/** @param sizes2
  *   list of lists of sizes
  * @param sizeReq
  *   required size
  * @param sizes
  *   list of sizes
  * @param sizeOptDef
  *   optional size with default value 1024 bytes
  * @param sizeOpt
  *   optional size, no default
  */
final case class ScalaIssue23Cfg(
  sizeOpt    : scala.Option[scala.Long],
  sizeOptDef : scala.Long,
  sizeReq    : scala.Long,
  sizes      : scala.List[scala.Long],
  sizes2     : scala.List[scala.List[scala.Long]]
)
object ScalaIssue23Cfg {
  def apply(c: com.typesafe.config.Config): ScalaIssue23Cfg = {
    val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
    val parentPath: java.lang.String = ""
    val $result = ScalaIssue23Cfg(
      sizeOpt    = if(c.hasPathOrNull("sizeOpt")) Some(c.getBytes("sizeOpt").longValue()) else None,
      sizeOptDef = if(c.hasPathOrNull("sizeOptDef")) c.getBytes("sizeOptDef").longValue() else 1024L,
      sizeReq    = $_reqSiz(parentPath, c, "sizeReq", $tsCfgValidator),
      sizes      = $_L$_siz(c.getList("sizes"), parentPath, $tsCfgValidator),
      sizes2     = $_L$_L$_siz(c.getList("sizes2"), parentPath, $tsCfgValidator)
    )
    $tsCfgValidator.validate()
    $result
  }
  private def $_reqSiz(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.Long = {
    if (c == null) 0
    else try c.getBytes(path)
    catch {
      case e:com.typesafe.config.ConfigException =>
        $tsCfgValidator.addBadPath(parentPath + path, e)
        0
    }
  }


  private def $_L$_L$_siz(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[scala.List[scala.Long]] = {
    import scala.jdk.CollectionConverters._
    cl.asScala.map(cv => $_L$_siz(cv.asInstanceOf[com.typesafe.config.ConfigList], parentPath, $tsCfgValidator)).toList
  }
  private def $_L$_siz(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.List[scala.Long] = {
    import scala.jdk.CollectionConverters._
    cl.asScala.map(cv => $_siz(cv)).toList
  }
  private def $_expE(cv:com.typesafe.config.ConfigValue, exp:java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(s"${cv.origin.lineNumber}: " +
      "expecting: " + exp + " got: " +
      (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u))
  }

  private def $_siz(cv:com.typesafe.config.ConfigValue): scala.Long = {
    val u: Any = cv.unwrapped
    if (cv.valueType() == com.typesafe.config.ConfigValueType.NUMBER ||
       u.isInstanceOf[scala.Long] || u.isInstanceOf[scala.Int])
      u.asInstanceOf[java.lang.Number].longValue()
    else if (cv.valueType() == com.typesafe.config.ConfigValueType.STRING) {
      com.typesafe.config.ConfigFactory.parseString("s = " + '"' + u + '"').getBytes("s")
    }
    else throw $_expE(cv, "size")
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
