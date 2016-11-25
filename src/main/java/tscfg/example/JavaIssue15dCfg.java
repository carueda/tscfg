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
    }


    public JavaIssue15dCfg(com.typesafe.config.Config c) {
        this.baz = $list$listBaz$Element(c.getList("baz"));
    }

    private static java.util.List<java.util.List<Baz$Element>> $list$listBaz$Element(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.util.List<Baz$Element>> al = new java.util.ArrayList<java.util.List<Baz$Element>>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($listBaz$Element((com.typesafe.config.ConfigList)cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }

    private static java.util.List<Baz$Element> $listBaz$Element(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<Baz$Element> al = new java.util.ArrayList<Baz$Element>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add(new Baz$Element(((com.typesafe.config.ConfigObject)cv).toConfig()));
      }
      return java.util.Collections.unmodifiableList(al);
    }
}

