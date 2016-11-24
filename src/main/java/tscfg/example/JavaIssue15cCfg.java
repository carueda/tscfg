package tscfg.example;

public class JavaIssue15cCfg {
    public final java.util.List<Positions$Element> positions;

    public static class Positions$Element {
        public final double lat;
        public final double lon;

        public Positions$Element(com.typesafe.config.Config c) {
            this.lat = c.getDouble("lat");
            this.lon = c.getDouble("lon");
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


    public JavaIssue15cCfg(com.typesafe.config.Config c) {
        this.positions = $listPositions$Element(c.getList("positions"));
    }

    private static java.util.List<Positions$Element> $listPositions$Element(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<Positions$Element> al = new java.util.ArrayList<Positions$Element>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add(new Positions$Element(((com.typesafe.config.ConfigObject)cv).toConfig()));
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

