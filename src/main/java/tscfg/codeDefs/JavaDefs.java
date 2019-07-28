// $COVERAGE-OFF$
package tscfg.codeDefs;

/**
 * Captures various definitions to be included in the generated wrapper.
 * This is not used as compile code in the generator itself but the
 * text of this file is retrieved as a resource at runtime.
 * Capturing this as code helps with validation at compile time.
 */
public class JavaDefs {

  ///////////////////////////////////////////////////////////////////////
  // definition of methods used to access required paths:
  
  //<$_reqStr>
  private static java.lang.String $_reqStr(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, java.lang.StringBuilder errors) {
    try {
      return c.getString(path);
    }
    catch(com.typesafe.config.ConfigException.Missing e) {
      errors.append("Undefined entry for path: '").append(parentPath).append(path).append("'\n");
      return null;
    }
  }
  //</$_reqStr>
  
  //<$_reqInt>
  private static int $_reqInt(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, java.lang.StringBuilder errors) {
    try {
      return c.getInt(path);
    }
    catch(com.typesafe.config.ConfigException.Missing e) {
      errors.append("Undefined entry for path: '").append(parentPath).append(path).append("'\n");
      return 0;
    }
  }
  //</$_reqInt>
  
  //<$_reqBln>
  private static boolean $_reqBln(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, java.lang.StringBuilder errors) {
    try {
      return c.getBoolean(path);
    }
    catch(com.typesafe.config.ConfigException.Missing e) {
      errors.append("Undefined entry for path: '").append(parentPath).append(path).append("'\n");
      return false;
    }
  }
  //</$_reqBln>
  
  //<$_reqDbl>
  private static double $_reqDbl(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, java.lang.StringBuilder errors) {
    try {
      return c.getDouble(path);
    }
    catch(com.typesafe.config.ConfigException.Missing e) {
      errors.append("Undefined entry for path: '").append(parentPath).append(path).append("'\n");
      return 0;
    }
  }
  //</$_reqDbl>
  
  //<$_reqLng>
  private static long $_reqLng(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, java.lang.StringBuilder errors) {
    try {
      return c.getLong(path);
    }
    catch(com.typesafe.config.ConfigException.Missing e) {
      errors.append("Undefined entry for path: '").append(parentPath).append(path).append("'\n");
      return 0;
    }
  }
  //</$_reqLng>
  
  //<$_reqSiz>
  private static long $_reqSiz(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, java.lang.StringBuilder errors) {
    try {
      return c.getBytes(path);
    }
    catch(com.typesafe.config.ConfigException.Missing e) {
      errors.append("Undefined entry for path: '").append(parentPath).append(path).append("'\n");
      return 0;
    }
  }
  //</$_reqSiz>
  
  ///////////////////////////////////////////////////////////////////////
  // definition of methods used to access list's elements of basic type:
  
  //<$_bln>
  private static java.lang.Boolean $_bln(com.typesafe.config.ConfigValue cv) {
    java.lang.Object u = cv.unwrapped();
    if (cv.valueType() != com.typesafe.config.ConfigValueType.BOOLEAN ||
      !(u instanceof java.lang.Boolean)) throw $_expE(cv, "boolean");
    return (java.lang.Boolean) u;
  }
  //</$_bln>
  
  //<$_dbl>
  private static java.lang.Double $_dbl(com.typesafe.config.ConfigValue cv) {
    java.lang.Object u = cv.unwrapped();
    if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
      !(u instanceof java.lang.Number)) throw $_expE(cv, "double");
    return ((java.lang.Number) u).doubleValue();
  }
  //</$_dbl>

  //<$_int>
  private static java.lang.Integer $_int(com.typesafe.config.ConfigValue cv) {
    java.lang.Object u = cv.unwrapped();
    if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
      !(u instanceof java.lang.Integer)) throw $_expE(cv, "integer");
    return (java.lang.Integer) u;
  }
  //</$_int>
  
  //<$_lng>
  private static java.lang.Long $_lng(com.typesafe.config.ConfigValue cv) {
    java.lang.Object u = cv.unwrapped();
    if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
      !(u instanceof java.lang.Long) && !(u instanceof java.lang.Integer)) throw $_expE(cv, "long");
    return ((java.lang.Number) u).longValue();
  }
  //</$_lng>
  
  //<$_str>
  private static java.lang.String $_str(com.typesafe.config.ConfigValue cv) {
    return java.lang.String.valueOf(cv.unwrapped());
  }
  //</$_str>
  
  // $_siz: since there's no something like cv.getBytes() nor is SimpleConfig.parseBytes visible,
  // use ConfigFactory.parseString:
  //<$_siz>
  private static java.lang.Long $_siz(com.typesafe.config.ConfigValue cv) {
    java.lang.Object u = cv.unwrapped();
    if (cv.valueType() == com.typesafe.config.ConfigValueType.NUMBER ||
       (u instanceof java.lang.Long) || (u instanceof java.lang.Integer))
      return ((java.lang.Number) u).longValue();
    if (cv.valueType() == com.typesafe.config.ConfigValueType.STRING) {
      return com.typesafe.config.ConfigFactory.parseString("s = " + '"' + u + '"').getBytes("s");
    }
    throw $_expE(cv, "size");
  }
  //</$_siz>
  
  //<$_expE>
  private static java.lang.RuntimeException $_expE(com.typesafe.config.ConfigValue cv, java.lang.String exp) {
    java.lang.Object u = cv.unwrapped();
    return new java.lang.RuntimeException(cv.origin().lineNumber()
      + ": expecting: " +exp + " got: " + (u instanceof java.lang.String ? "\"" +u+ "\"" : u));
  }
  //</$_expE>
  
}
// $COVERAGE-ON$
