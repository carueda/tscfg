package tscfg.example;

public class JavaIssue47Cfg {
  public final JavaIssue47Cfg.Service service;

  public static class Service {
    public final boolean debug;
    public final boolean doLog;
    public final double factor;
    public final int poolSize;
    public final java.lang.String url;

    public Service(
        com.typesafe.config.Config c,
        java.lang.String parentPath,
        $TsCfgValidator $tsCfgValidator) {
      this.debug = $_reqBln(parentPath, c, "debug", $tsCfgValidator);
      this.doLog = $_reqBln(parentPath, c, "doLog", $tsCfgValidator);
      this.factor = $_reqDbl(parentPath, c, "factor", $tsCfgValidator);
      this.poolSize = $_reqInt(parentPath, c, "poolSize", $tsCfgValidator);
      this.url = $_reqStr(parentPath, c, "url", $tsCfgValidator);
    }

    private static boolean $_reqBln(
        java.lang.String parentPath,
        com.typesafe.config.Config c,
        java.lang.String path,
        $TsCfgValidator $tsCfgValidator) {
      if (c == null) return false;
      try {
        return c.getBoolean(path);
      } catch (com.typesafe.config.ConfigException e) {
        $tsCfgValidator.addBadPath(parentPath + path, e);
        return false;
      }
    }

    private static double $_reqDbl(
        java.lang.String parentPath,
        com.typesafe.config.Config c,
        java.lang.String path,
        $TsCfgValidator $tsCfgValidator) {
      if (c == null) return 0;
      try {
        return c.getDouble(path);
      } catch (com.typesafe.config.ConfigException e) {
        $tsCfgValidator.addBadPath(parentPath + path, e);
        return 0;
      }
    }

    private static int $_reqInt(
        java.lang.String parentPath,
        com.typesafe.config.Config c,
        java.lang.String path,
        $TsCfgValidator $tsCfgValidator) {
      if (c == null) return 0;
      try {
        return c.getInt(path);
      } catch (com.typesafe.config.ConfigException e) {
        $tsCfgValidator.addBadPath(parentPath + path, e);
        return 0;
      }
    }

    private static java.lang.String $_reqStr(
        java.lang.String parentPath,
        com.typesafe.config.Config c,
        java.lang.String path,
        $TsCfgValidator $tsCfgValidator) {
      if (c == null) return null;
      try {
        return c.getString(path);
      } catch (com.typesafe.config.ConfigException e) {
        $tsCfgValidator.addBadPath(parentPath + path, e);
        return null;
      }
    }
  }

  public JavaIssue47Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.service =
        new JavaIssue47Cfg.Service(
            $_reqConfig(parentPath, c, "service", $tsCfgValidator),
            parentPath + "service.",
            $tsCfgValidator);
    $tsCfgValidator.validate();
  }

  private static com.typesafe.config.Config $_reqConfig(
      java.lang.String parentPath,
      com.typesafe.config.Config c,
      java.lang.String path,
      $TsCfgValidator $tsCfgValidator) {
    if (c == null) return null;
    try {
      return c.getConfig(path);
    } catch (com.typesafe.config.ConfigException e) {
      $tsCfgValidator.addBadPath(parentPath + path, e);
      return null;
    }
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
