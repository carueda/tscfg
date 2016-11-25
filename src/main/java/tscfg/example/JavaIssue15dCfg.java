package tscfg.example;

public class JavaIssue15dCfg {
    public final java.util.List<java.util.List<_$0$Elm_>> baz;

    public static class _$0$Elm_ {
        public final java.lang.Boolean aa;
        public final double dd;

        public _$0$Elm_(com.typesafe.config.Config c) {
            this.aa = c != null && c.hasPath("aa") ? c.getBoolean("aa") : null;
            this.dd = c.getDouble("dd");
        }
    }


    public JavaIssue15dCfg(com.typesafe.config.Config c) {
        this.baz = $list$list_$0$Elm_(c.getList("baz"));
    }

    private static java.util.List<_$0$Elm_> $list_$0$Elm_(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<_$0$Elm_> al = new java.util.ArrayList<_$0$Elm_>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add(new _$0$Elm_(((com.typesafe.config.ConfigObject)cv).toConfig()));
      }
      return java.util.Collections.unmodifiableList(al);
    }

    private static java.util.List<java.util.List<_$0$Elm_>> $list$list_$0$Elm_(com.typesafe.config.ConfigList cl) {
      java.util.ArrayList<java.util.List<_$0$Elm_>> al = new java.util.ArrayList<java.util.List<_$0$Elm_>>();
      for (com.typesafe.config.ConfigValue cv: cl) {
        al.add($list_$0$Elm_((com.typesafe.config.ConfigList)cv));
      }
      return java.util.Collections.unmodifiableList(al);
    }
}

