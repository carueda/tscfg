package tscfg.example;

public class JavaIssue15cCfg {
    public final java.util.List<Positions$Element> positions;

    public static class Positions$Element {
        public final java.util.List<java.lang.Double> attrs;
        public final double lat;
        public final double lon;

        public Positions$Element(com.typesafe.config.Config c) {
            this.attrs = $list$dbl(c.getList("attrs"));
            this.lat = c.getDouble("lat");
            this.lon = c.getDouble("lon");
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

    private static java.util.List<java.lang.Double> $list$dbl(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.lang.Double> al = new java.util.ArrayList<java.lang.Double>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($dbl(cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.lang.Double $dbl(com.typesafe.config.ConfigValue cv) {
      java.lang.Object u = cv.unwrapped();
      if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
        !(u instanceof java.lang.Number)) throw $exc(cv, "double");
      return ((java.lang.Number) u).doubleValue();
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

