package tscfg.example;

public class JavaIssue15Cfg {
    public final java.util.List<_$0$Elm_> positions;

    public static class _$0$Elm_ {
        public final java.util.List<java.lang.Integer> numbers;
        public final java.util.List<java.util.List<_$1$Elm_>> positions;

        public static class _$1$Elm_ {
            public final int other;
            public final java.lang.String stuff;

            public _$1$Elm_(com.typesafe.config.Config c) {
                this.other = c.getInt("other");
                this.stuff = c.getString("stuff");
            }
        }


        public _$0$Elm_(com.typesafe.config.Config c) {
            this.numbers = $list$int(c.getList("numbers"));
            this.positions = $list$list_$1$Elm_(c.getList("positions"));
        }

        private static java.util.List<java.util.List<_$1$Elm_>> $list$list_$1$Elm_(com.typesafe.config.ConfigList cl) {
          java.util.ArrayList<java.util.List<_$1$Elm_>> al = new java.util.ArrayList<java.util.List<_$1$Elm_>>();
          for (com.typesafe.config.ConfigValue cv: cl) {
            al.add($list_$1$Elm_((com.typesafe.config.ConfigList)cv));
          }
          return java.util.Collections.unmodifiableList(al);
        }
        private static java.util.List<_$1$Elm_> $list_$1$Elm_(com.typesafe.config.ConfigList cl) {
          java.util.ArrayList<_$1$Elm_> al = new java.util.ArrayList<_$1$Elm_>();
          for (com.typesafe.config.ConfigValue cv: cl) {
            al.add(new _$1$Elm_(((com.typesafe.config.ConfigObject)cv).toConfig()));
          }
          return java.util.Collections.unmodifiableList(al);
        }
    }


    public JavaIssue15Cfg(com.typesafe.config.Config c) {
        this.positions = $list_$0$Elm_(c.getList("positions"));
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
    private static java.util.List<java.lang.Integer> $list$int(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.lang.Integer> al = new java.util.ArrayList<java.lang.Integer>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($int(cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }
    private static java.util.List<_$0$Elm_> $list_$0$Elm_(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<_$0$Elm_> al = new java.util.ArrayList<_$0$Elm_>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add(new _$0$Elm_(((com.typesafe.config.ConfigObject)cv).toConfig()));
      }
      return java.util.Collections.unmodifiableList(al);
    }
}

