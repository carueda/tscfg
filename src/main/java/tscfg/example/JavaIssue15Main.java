package tscfg.example;

import com.typesafe.config.ConfigFactory;
    
public class JavaIssue15Main {
  public static void main(String[] args) {
    JavaIssue15bCfg b = new JavaIssue15bCfg(
        ConfigFactory.parseString(
            "numbers: [[1, 2, 3], [4, 5]]"
        )
    );
    System.out.println("b.numbers = " + b.numbers);

    JavaIssue15cCfg c = new JavaIssue15cCfg(
        ConfigFactory.parseString(
            "positions: [{lat: 1, lon: 2}, {lat:3, lon:4}]"
        )
    );
    System.out.println("c.positions = " + c.positions);
  }
  
}