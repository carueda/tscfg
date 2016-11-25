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

