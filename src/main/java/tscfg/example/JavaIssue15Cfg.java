package tscfg.example;

public class JavaIssue15Cfg {
    public final java.util.List<java.lang.String> names;
    public final java.util.List<Positions$Element> positions;

    public static class Positions$Element {
        public final Foo foo;
        public final double lat;
        public final double lon;
        public final java.util.List<java.lang.Integer> numbers;

        public static class Foo {
            public final java.lang.String bar;

            public Foo(com.typesafe.config.Config c) {
                this.bar = c.getString("bar");
            }
        }

        public Positions$Element(com.typesafe.config.Config c) {
            this.foo = new Foo(_$config(c, "foo"));
            this.lat = c.getDouble("lat");
            this.lon = c.getDouble("lon");
            this.numbers = $list$int(c.getList("numbers"));
        }
    }


    public JavaIssue15Cfg(com.typesafe.config.Config c) {
        this.names = $list$str(c.getList("names"));
        this.positions = $listPositions$Element(c.getList("positions"));
    }

    private static java.util.List<java.lang.String> $list$str(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.lang.String> al = new java.util.ArrayList<java.lang.String>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($str(cv));
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

    private static java.util.List<Positions$Element> $listPositions$Element(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<Positions$Element> al = new java.util.ArrayList<Positions$Element>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add(new Positions$Element(((com.typesafe.config.ConfigObject)cv).toConfig()));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.lang.String $str(com.typesafe.config.ConfigValue cv) {
      return java.lang.String.valueOf(cv.unwrapped());
    }
    private static java.lang.Integer $int(com.typesafe.config.ConfigValue cv) {
      java.lang.Object u = cv.unwrapped();
      if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
        !(u instanceof java.lang.Integer)) throw $exc(cv, "integer");
      return (java.lang.Integer) u;
    }
    private static java.lang.RuntimeException $exc(com.typesafe.config.ConfigValue cv, java.lang.String exp) {
      java.lang.Object u = cv.unwrapped();
      return new java.lang.RuntimeException(cv.origin().lineNumber()
        + ": expecting: " +exp + " got: " + (u instanceof java.lang.String ? "\"" +u+ "\"" : u));
    }
    private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
      return c != null && c.hasPath(path) ? c.getConfig(path) : null;
    }
}

