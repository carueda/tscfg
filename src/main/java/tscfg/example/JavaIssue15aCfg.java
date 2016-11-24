package tscfg.example;

public class JavaIssue15aCfg {
    public final double dd;
    public final Foo foo;
    public final int ii;

    public static class Foo {
        public final java.lang.String bar;

        public Foo(com.typesafe.config.Config c) {
            this.bar = c.getString("bar");
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

    public JavaIssue15aCfg(com.typesafe.config.Config c) {
        this.dd = c.getDouble("dd");
        this.foo = new Foo(_$config(c, "foo"));
        this.ii = c.getInt("ii");
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

