package tscfg.example;

public class JavaIssue55Cfg {
  public final java.lang.String regex;
  public final java.lang.String regex2;

  public JavaIssue55Cfg(com.typesafe.config.Config c) {
    final $TsCfgValidator $tsCfgValidator = new $TsCfgValidator();
    final java.lang.String parentPath = "";
    this.regex =
        c.hasPathOrNull("regex")
            ? c.getString("regex")
            : ">(RUS00),(\\d{12})(.\\d{7})(.\\d{8})(\\d{3})(\\d{3}),(\\d{1,10})((\\.)(\\d{3}))?";
    this.regex2 = c.hasPathOrNull("regex2") ? c.getString("regex2") : "foo bar: ([\\d]+)";
    $tsCfgValidator.validate();
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
