package tscfg.example;

public class JavaIssue15dCfg {
    public final java.util.List<java.util.List<Baz$Element>> baz;

    public static class Baz$Element {
        public final java.lang.Boolean aa;
        public final double dd;

        public Baz$Element(com.typesafe.config.Config c) {
            this.aa = c != null && c.hasPath("aa") ? c.getBoolean("aa") : null;
            this.dd = c.getDouble("dd");
        }

        private static java.lang.Integer $int(com.typesafe.config.ConfigValue cv) {
          java.lang.Object u = cv.unwrapped();
          if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER
              || !(u instanceof java.lang.Integer)) {
            _$exc(u, "integer");
          }
          return (java.lang.Integer) u;
        }

        private static void _$exc(java.lang.Object u, java.lang.String expected) {
          throw new java.lang.RuntimeException(
              "expecting: " +expected + " got: " +
                  (u instanceof java.lang.String ? "\"" +u+ "\"" : u));
        }
    }


    public JavaIssue15dCfg(com.typesafe.config.Config c) {
        this.baz = $list$listBaz$Element(c.getList("baz"));
    }

    private static java.util.List<Baz$Element> $listBaz$Element(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<Baz$Element> al = new java.util.ArrayList<Baz$Element>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add(new Baz$Element(((com.typesafe.config.ConfigObject)cv).toConfig()));
      }
      return java.util.Collections.unmodifiableList(al);
    }

    private static java.util.List<java.util.List<Baz$Element>> $list$listBaz$Element(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.util.List<Baz$Element>> al = new java.util.ArrayList<java.util.List<Baz$Element>>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($listBaz$Element((com.typesafe.config.ConfigList)cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }

    private static java.lang.Integer $int(com.typesafe.config.ConfigValue cv) {
      java.lang.Object u = cv.unwrapped();
      if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER
          || !(u instanceof java.lang.Integer)) {
        _$exc(u, "integer");
      }
      return (java.lang.Integer) u;
    }

    private static void _$exc(java.lang.Object u, java.lang.String expected) {
      throw new java.lang.RuntimeException(
          "expecting: " +expected + " got: " +
              (u instanceof java.lang.String ? "\"" +u+ "\"" : u));
    }

    private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
      return c != null && c.hasPath(path) ? c.getConfig(path) : null;
    }
}

