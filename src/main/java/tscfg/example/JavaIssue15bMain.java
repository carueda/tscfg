package tscfg.example;

public class JavaIssue15bMain {
  public static void main(String[] args) {
    String src = "ages: [[1, 2, 3], [4, 5]]";
    com.typesafe.config.Config c = com.typesafe.config.ConfigFactory.parseString(src);
    JavaIssue15bCfg j = new JavaIssue15bCfg(c);
    System.out.println("j.ages = " + j.ages);
  }
  
}