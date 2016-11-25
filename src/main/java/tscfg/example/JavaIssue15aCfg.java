package tscfg.example;

public class JavaIssue15aCfg {
    public final double dd;
    public final Foo foo;
    public final int ii;

    public static class Foo {
        public final Aho aho;
        public final java.lang.String bar;

        public static class Aho {
            public final int val;
            public final java.lang.Boolean yes;

            public Aho(com.typesafe.config.Config c) {
                this.val = c != null && c.hasPath("val") ? c.getInt("val") : 31;
                this.yes = c != null && c.hasPath("yes") ? c.getBoolean("yes") : null;
            }
        }

        public Foo(com.typesafe.config.Config c) {
            this.aho = new Aho(_$config(c, "aho"));
            this.bar = c.getString("bar");
        }
    }

    public JavaIssue15aCfg(com.typesafe.config.Config c) {
        this.dd = c.getDouble("dd");
        this.foo = new Foo(_$config(c, "foo"));
        this.ii = c.getInt("ii");
    }

    private static com.typesafe.config.Config _$config(com.typesafe.config.Config c, java.lang.String path) {
      return c != null && c.hasPath(path) ? c.getConfig(path) : null;
    }
}

