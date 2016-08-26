// generated by tscfg 0.3.3 on Fri Aug 26 00:45:29 PDT 2016
// source: example/example1.spec.conf

package tscfg.example;

public class JavaExample1Cfg {
  public final java.lang.String bazOptionalWithDefault;
  public final java.lang.String bazOptionalWithNoDefault;
  public final java.lang.String fooRequired;
  public JavaExample1Cfg(com.typesafe.config.Config c) {
    this.bazOptionalWithDefault = c != null && c.hasPathOrNull("bazOptionalWithDefault") ? c.getString("bazOptionalWithDefault") : "hello";
    this.bazOptionalWithNoDefault = c != null && c.hasPathOrNull("bazOptionalWithNoDefault") ? c.getString("bazOptionalWithNoDefault") : null;
    this.fooRequired = c.getString("fooRequired");
  }
  public java.lang.String toString() { return toString(""); }
  public java.lang.String toString(java.lang.String i) {
    return i+ "bazOptionalWithDefault = " + this.bazOptionalWithDefault + "\n"
        +i+ "bazOptionalWithNoDefault = " + this.bazOptionalWithNoDefault + "\n"
        +i+ "fooRequired = " + this.fooRequired + "\n";
  }
  public java.lang.String toPropString() { return toPropString(""); }
  public java.lang.String toPropString(java.lang.String p) {
    return p+ "bazOptionalWithDefault = " + this.bazOptionalWithDefault + "\n"
        +p+ "bazOptionalWithNoDefault = " + this.bazOptionalWithNoDefault + "\n"
        +p+ "fooRequired = " + this.fooRequired + "\n";
  }
}
