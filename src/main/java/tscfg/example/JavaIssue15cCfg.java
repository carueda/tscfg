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
    private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
      return c != null && c.hasPath(path) ? c.getConfig(path) : null;
    }
}

