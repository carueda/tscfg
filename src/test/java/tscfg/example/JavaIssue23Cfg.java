package tscfg.example;

public class JavaIssue23Cfg {
  public final java.lang.Long sizeOpt;
  public final long sizeOptDef;
  public final long sizeReq;
  public final java.util.List<java.lang.Long> sizes;
  public final java.util.List<java.util.List<java.lang.Long>> sizes2;

  public JavaIssue23Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.sizeOpt = c.hasPathOrNull("sizeOpt") ? c.getBytes("sizeOpt") : null;
    this.sizeOptDef = c.hasPathOrNull("sizeOptDef") ? c.getBytes("sizeOptDef") : 1024L;
    this.sizeReq = $_reqSiz(parentPath, c, "sizeReq", $tsCfgValidator);
    this.sizes = $_L$_siz(c.getList("sizes"), parentPath, $tsCfgValidator);
    this.sizes2 = $_L$_L$_siz(c.getList("sizes2"), parentPath, $tsCfgValidator);
    $tsCfgValidator.validate();
  }

  private static long $_reqSiz(
      java.lang.String parentPath,
      com.typesafe.config.Config c,
      java.lang.String path,
      $TsCfgValidator $tsCfgValidator) {
    if (c == null) return 0;
    try {
      return c.getBytes(path);
    } catch (com.typesafe.config.ConfigException e) {
      $tsCfgValidator.addBadPath(parentPath + path, e);
      return 0;
    }
  }

  private static java.util.List<java.util.List<java.lang.Long>> $_L$_L$_siz(
      com.typesafe.config.ConfigList cl,
      java.lang.String parentPath,
      $TsCfgValidator $tsCfgValidator) {
    java.util.ArrayList<java.util.List<java.lang.Long>> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv : cl) {
      al.add($_L$_siz((com.typesafe.config.ConfigList) cv, parentPath, $tsCfgValidator));
    }
    return java.util.Collections.unmodifiableList(al);
  }

  private static java.util.List<java.lang.Long> $_L$_siz(
      com.typesafe.config.ConfigList cl,
      java.lang.String parentPath,
      $TsCfgValidator $tsCfgValidator) {
    java.util.ArrayList<java.lang.Long> al = new java.util.ArrayList<>();
    for (com.typesafe.config.ConfigValue cv : cl) {
      al.add($_siz(cv));
    }
    return java.util.Collections.unmodifiableList(al);
  }

  private static java.lang.RuntimeException $_expE(
      com.typesafe.config.ConfigValue cv, java.lang.String exp) {
    java.lang.Object u = cv.unwrapped();
    return new java.lang.RuntimeException(
        cv.origin().lineNumber()
            + ": expecting: "
            + exp
            + " got: "
            + (u instanceof java.lang.String ? "\"" + u + "\"" : u));
  }

  private static java.lang.Long $_siz(com.typesafe.config.ConfigValue cv) {
    java.lang.Object u = cv.unwrapped();
    if (cv.valueType() == com.typesafe.config.ConfigValueType.NUMBER
        || (u instanceof java.lang.Long)
        || (u instanceof java.lang.Integer)) return ((java.lang.Number) u).longValue();
    if (cv.valueType() == com.typesafe.config.ConfigValueType.STRING) {
      return com.typesafe.config.ConfigFactory.parseString("s = " + '"' + u + '"').getBytes("s");
    }
    throw $_expE(cv, "size");
  }

  private static final class $TsCfgValidator {
    private final java.util.List<java.lang.String> badPaths = new java.util.ArrayList<>();

    void addBadPath(java.lang.String path, com.typesafe.config.ConfigException e) {
      badPaths.add("'" + path + "': " + e.getClass().getName() + "(" + e.getMessage() + ")");
    }

    void validate() {
      if (!badPaths.isEmpty()) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("Invalid configuration:");
        for (java.lang.String path : badPaths) {
          sb.append("\n    ").append(path);
        }
        throw new com.typesafe.config.ConfigException(sb.toString()) {};
      }
    }
  }
}
