package tscfg.example;

public class JavaIssue15Cfg {
    public final java.util.List<Positions$Element> positions;

    public static class Positions$Element {
        public final java.util.List<java.lang.Integer> numbers;
        public final java.util.List<PositionsXX$Element> positionsXX;

        public static class PositionsXX$Element {
            public final int other;
            public final java.lang.String stuff;

            public PositionsXX$Element(com.typesafe.config.Config c) {
                this.other = c.getInt("other");
                this.stuff = c.getString("stuff");
            }
        }


        public Positions$Element(com.typesafe.config.Config c) {
            this.numbers = $list$int(c.getList("numbers"));
            this.positionsXX = $listPositionsXX$Element(c.getList("positionsXX"));
        }

        private static java.util.List<PositionsXX$Element> $listPositionsXX$Element(com.typesafe.config.ConfigList cl) {
          java.util.ArrayList<PositionsXX$Element> al = new java.util.ArrayList<PositionsXX$Element>();
          for (com.typesafe.config.ConfigValue cv: cl) {
            al.add(new PositionsXX$Element(((com.typesafe.config.ConfigObject)cv).toConfig()));
          }
          return java.util.Collections.unmodifiableList(al);
        }
    }


    public JavaIssue15Cfg(com.typesafe.config.Config c) {
        this.positions = $listPositions$Element(c.getList("positions"));
    }

    private static java.util.List<Positions$Element> $listPositions$Element(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<Positions$Element> al = new java.util.ArrayList<Positions$Element>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add(new Positions$Element(((com.typesafe.config.ConfigObject)cv).toConfig()));
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
}

