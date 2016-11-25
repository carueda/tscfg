package tscfg.example;

public class JavaIssue15bCfg {
    public final java.util.List<java.lang.Boolean> booleans;
    public final java.util.List<java.lang.Double> doubles;
    public final java.util.List<java.util.List<java.lang.Integer>> integers;
    public final java.util.List<java.lang.Long> longs;
    public final java.util.List<java.lang.String> strings;

    public JavaIssue15bCfg(com.typesafe.config.Config c) {
        this.booleans = $list$bln(c.getList("booleans"));
        this.doubles = $list$dbl(c.getList("doubles"));
        this.integers = $list$list$int(c.getList("integers"));
        this.longs = $list$lng(c.getList("longs"));
        this.strings = $list$str(c.getList("strings"));
    }

    private static java.lang.Boolean $bln(com.typesafe.config.ConfigValue cv) {
      java.lang.Object u = cv.unwrapped();
      if (cv.valueType() != com.typesafe.config.ConfigValueType.BOOLEAN ||
        !(u instanceof java.lang.Boolean)) throw $expE(cv, "boolean");
      return (java.lang.Boolean) u;
    }
    private static java.lang.Double $dbl(com.typesafe.config.ConfigValue cv) {
      java.lang.Object u = cv.unwrapped();
      if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
        !(u instanceof java.lang.Number)) throw $expE(cv, "double");
      return ((java.lang.Number) u).doubleValue();
    }
    private static java.lang.RuntimeException $expE(com.typesafe.config.ConfigValue cv, java.lang.String exp) {
      java.lang.Object u = cv.unwrapped();
      return new java.lang.RuntimeException(cv.origin().lineNumber()
        + ": expecting: " +exp + " got: " + (u instanceof java.lang.String ? "\"" +u+ "\"" : u));
    }
    private static java.lang.Integer $int(com.typesafe.config.ConfigValue cv) {
      java.lang.Object u = cv.unwrapped();
      if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
        !(u instanceof java.lang.Integer)) throw $expE(cv, "integer");
      return (java.lang.Integer) u;
    }
    private static java.util.List<java.lang.Boolean> $list$bln(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.lang.Boolean> al = new java.util.ArrayList<java.lang.Boolean>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($bln(cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.util.List<java.lang.Double> $list$dbl(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.lang.Double> al = new java.util.ArrayList<java.lang.Double>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($dbl(cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.util.List<java.lang.Integer> $list$int(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.lang.Integer> al = new java.util.ArrayList<java.lang.Integer>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($int(cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.util.List<java.util.List<java.lang.Integer>> $list$list$int(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.util.List<java.lang.Integer>> al = new java.util.ArrayList<java.util.List<java.lang.Integer>>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($list$int((com.typesafe.config.ConfigList)cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.util.List<java.lang.Long> $list$lng(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.lang.Long> al = new java.util.ArrayList<java.lang.Long>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($lng(cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.util.List<java.lang.String> $list$str(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.lang.String> al = new java.util.ArrayList<java.lang.String>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($str(cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.lang.Long $lng(com.typesafe.config.ConfigValue cv) {
      java.lang.Object u = cv.unwrapped();
      if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
        !(u instanceof java.lang.Long) && !(u instanceof java.lang.Integer)) throw $expE(cv, "long");
      return ((java.lang.Number) u).longValue();
    }
    private static java.lang.String $str(com.typesafe.config.ConfigValue cv) {
      return java.lang.String.valueOf(cv.unwrapped());
    }
}

