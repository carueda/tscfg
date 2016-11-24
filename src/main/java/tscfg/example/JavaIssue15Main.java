package tscfg.example;

public class JavaIssue15Main {
  public static void main(String[] args) {
    String src = "numbers: [[1, 2, 3], [4, 5]]";
    com.typesafe.config.Config c = com.typesafe.config.ConfigFactory.parseString(src);
    JavaIssue15bCfg j = new JavaIssue15bCfg(c);
    System.out.println("j.numbers = " + j.numbers);
  }
  
}