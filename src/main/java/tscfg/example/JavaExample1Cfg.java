package tscfg.example;

public class JavaExample1Cfg {
  public final java.lang.String bazOptionalWithDefault;
  public final java.lang.String bazOptionalWithNoDefault;
  public final java.lang.String fooRequired;

  public JavaExample1Cfg(com.typesafe.config.Config c) {
    this.bazOptionalWithDefault = c.getString("bazOptionalWithDefault");
    this.bazOptionalWithNoDefault = c.hasPathOrNull("bazOptionalWithNoDefault") ? c.getString("bazOptionalWithNoDefault") : null;
    this.fooRequired = c.getString("fooRequired");
  }
}

