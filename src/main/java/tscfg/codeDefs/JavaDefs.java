// $COVERAGE-OFF$
package tscfg.codeDefs;

import java.util.ArrayList;
import java.util.List;

/**
 * Captures various definitions to be included in the generated wrapper.
 * This is not used as compile code in the generator itself but the
 * text of this file is retrieved as a resource at runtime.
 * Capturing this as code helps with validation at compile time.
 */
public class JavaDefs {
  
  //<$TsCfgValidator>
  private static final class $TsCfgValidator  {
    private final java.util.List<java.lang.String> undefinedPaths = new java.util.ArrayList<>();
    
    void addUndefinedPath(java.lang.String path) {
      undefinedPaths.add(path);
    }
    
    void validate() {
      if (!undefinedPaths.isEmpty()) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("Undefined paths in given configuration: ");
        java.lang.String comma = "";
        for (java.lang.String path : undefinedPaths) {
          sb.append(comma).append("`").append(path).append("`");
          comma = ", ";
        }
        throw new java.lang.RuntimeException(sb.toString());
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
  private static com.typesafe.config.Config $_reqConfig(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
    if (c == null) return null;
    try {
      return c.getConfig(path);
    }
    catch(com.typesafe.config.ConfigException.Missing e) {
      $tsCfgValidator.addUndefinedPath(parentPath + path);
      return null;
    }
  }
  //</$_reqConfig>
  
  //<$_reqStr>
  private static java.lang.String $_reqStr(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
    if (c == null) return null;
    try {
      return c.getString(path);
    }
    catch(com.typesafe.config.ConfigException.Missing e) {
      $tsCfgValidator.addUndefinedPath(parentPath + path);
      return null;
    }
  }
  //</$_reqStr>
  
  //<$_reqInt>
  private static int $_reqInt(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
    if (c == null) return 0;
    try {
      return c.getInt(path);
    }
    catch(com.typesafe.config.ConfigException.Missing e) {
      $tsCfgValidator.addUndefinedPath(parentPath + path);
      return 0;
    }
  }
  //</$_reqInt>
  
  //<$_reqBln>
  private static boolean $_reqBln(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
    if (c == null) return false;
    try {
      return c.getBoolean(path);
    }
    catch(com.typesafe.config.ConfigException.Missing e) {
      $tsCfgValidator.addUndefinedPath(parentPath + path);
      return false;
    }
  }
  //</$_reqBln>
  
  //<$_reqDbl>
  private static double $_reqDbl(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
    if (c == null) return 0;
    try {
      return c.getDouble(path);
    }
    catch(com.typesafe.config.ConfigException.Missing e) {
      $tsCfgValidator.addUndefinedPath(parentPath + path);
      return 0;
    }
  }
  //</$_reqDbl>
  
  //<$_reqLng>
  private static long $_reqLng(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
    if (c == null) return 0;
    try {
      return c.getLong(path);
    }
    catch(com.typesafe.config.ConfigException.Missing e) {
      $tsCfgValidator.addUndefinedPath(parentPath + path);
      return 0;
    }
  }
  //</$_reqLng>
  
  //<$_reqSiz>
  private static long $_reqSiz(java.lang.String parentPath, com.typesafe.config.Config c, java.lang.String path, $TsCfgValidator $tsCfgValidator) {
    if (c == null) return 0;
    try {
      return c.getBytes(path);
    }
    catch(com.typesafe.config.ConfigException.Missing e) {
      $tsCfgValidator.addUndefinedPath(parentPath + path);
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
