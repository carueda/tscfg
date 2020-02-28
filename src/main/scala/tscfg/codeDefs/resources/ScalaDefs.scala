// $COVERAGE-OFF$
package tscfg.codeDefs.resources

/**
  * Captures various definitions to be included in the generated wrapper.
  * This is not used as compile code in the generator itself but the
  * text of this file is retrieved as a resource at runtime.
  * Capturing this as code helps with validation at compile time.
  */
object ScalaDefs {

  //<$TsCfgValidator>
  private final class $TsCfgValidator {
    private val badPaths = scala.collection.mutable.ArrayBuffer[java.lang.String]()

    def addBadPath(path: java.lang.String, e: com.typesafe.config.ConfigException): Unit = {
      badPaths += s"'$path': ${e.getClass.getName}(${e.getMessage})"
    }

    def validate(): Unit = {
      if (badPaths.nonEmpty) {
        throw new com.typesafe.config.ConfigException(
          badPaths.mkString("Invalid configuration:\n    ", "\n    ", "")
        ){}
      }
    }
  }
  //</$TsCfgValidator>

  ///////////////////////////////////////////////////////////////////////
  // Definition of methods used to access required paths:
  //
  // Note: the various `$_req` methods first do `if (c == null) return <null_value>`
  // as a way to simplify the logic when "traversing" an undefined config object.
  //

  //<$_reqConfig>
  private def $_reqConfig(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): com.typesafe.config.Config = {
    if (c == null) null
    else try c.getConfig(path)
    catch {
      case e:com.typesafe.config.ConfigException =>
        $tsCfgValidator.addBadPath(parentPath + path, e)
        null
    }
  }
  //</$_reqConfig>

  //<$_reqStr>
  private def $_reqStr(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): java.lang.String = {
    if (c == null) null
    else try c.getString(path)
    catch {
      case e:com.typesafe.config.ConfigException =>
        $tsCfgValidator.addBadPath(parentPath + path, e)
        null
    }
  }
  //</$_reqStr>

  //<$_reqInt>
  private def $_reqInt(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.Int = {
    if (c == null) 0
    else try c.getInt(path)
    catch {
      case e:com.typesafe.config.ConfigException =>
        $tsCfgValidator.addBadPath(parentPath + path, e)
        0
    }
  }
  //</$_reqInt>

  //<$_reqBln>
  private def $_reqBln(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.Boolean = {
    if (c == null) false
    else try c.getBoolean(path)
    catch {
      case e:com.typesafe.config.ConfigException =>
        $tsCfgValidator.addBadPath(parentPath + path, e)
        false
    }
  }
  //</$_reqBln>

  //<$_reqDbl>
  private def $_reqDbl(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.Double = {
    if (c == null) 0
    else try c.getDouble(path)
    catch {
      case e:com.typesafe.config.ConfigException =>
        $tsCfgValidator.addBadPath(parentPath + path, e)
        0
    }
  }
  //</$_reqDbl>

  //<$_reqLng>
  private def $_reqLng(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.Long = {
    if (c == null) 0
    else try c.getLong(path)
    catch {
      case e:com.typesafe.config.ConfigException =>
        $tsCfgValidator.addBadPath(parentPath + path, e)
        0
    }
  }
  //</$_reqLng>

  //<$_reqSiz>
  private def $_reqSiz(parentPath: java.lang.String, c: com.typesafe.config.Config, path: java.lang.String, $tsCfgValidator: $TsCfgValidator): scala.Long = {
    if (c == null) 0
    else try c.getBytes(path)
    catch {
      case e:com.typesafe.config.ConfigException =>
        $tsCfgValidator.addBadPath(parentPath + path, e)
        0
    }
  }
  //</$_reqSiz>

  ///////////////////////////////////////////////////////////////////////
  // definition of methods used to access list's elements of basic type:

  //<$_bln>
  private def $_bln(cv:com.typesafe.config.ConfigValue): scala.Boolean = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.BOOLEAN) ||
      !u.isInstanceOf[java.lang.Boolean]) throw $_expE(cv, "boolean")
    u.asInstanceOf[java.lang.Boolean].booleanValue()
  }
  //</$_bln>

  //<$_dbl>
  private def $_dbl(cv:com.typesafe.config.ConfigValue): scala.Double = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
      !u.isInstanceOf[java.lang.Number]) throw $_expE(cv, "double")
    u.asInstanceOf[java.lang.Number].doubleValue()
  }
  //</$_dbl>

  //<$_int>
  private def $_int(cv:com.typesafe.config.ConfigValue): scala.Int = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
      !u.isInstanceOf[Integer]) throw $_expE(cv, "integer")
    u.asInstanceOf[Integer]
  }
  //</$_int>

  //<$_lng>
  private def $_lng(cv:com.typesafe.config.ConfigValue): scala.Long = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
      !u.isInstanceOf[java.lang.Integer] && !u.isInstanceOf[java.lang.Long]) throw $_expE(cv, "long")
    u.asInstanceOf[java.lang.Number].longValue()
  }
  //</$_lng>

  //<$_str>
  private def $_str(cv:com.typesafe.config.ConfigValue): java.lang.String = {
    java.lang.String.valueOf(cv.unwrapped())
  }
  //</$_str>

  //<$_siz>
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
  //</$_siz>

  //<$_expE>
  private def $_expE(cv:com.typesafe.config.ConfigValue, exp:java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(s"${cv.origin.lineNumber}: " +
      "expecting: " + exp + " got: " +
      (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u))
  }
  //</$_expE>

  //<$_require>
  def $_require[T](parentPath: java.lang.String, path: java.lang.String)(get: => T): T = {
    import com.typesafe.config.ConfigException.Missing
    try get
    catch {
      case e: Missing => throw if (parentPath.isEmpty) e else new Missing(parentPath + path, e)
    }
  }
  //</$_require>

}
// $COVERAGE-ON$
