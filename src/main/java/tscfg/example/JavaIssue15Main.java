package tscfg.example;

import com.typesafe.config.ConfigFactory;
    
public class JavaIssue15Main {
  public static void main(String[] args) {
    {
      JavaIssue15bCfg c = new JavaIssue15bCfg(
          ConfigFactory.parseString(
              "numbers: [[1, 2, 3], [4, 5]]"
          )
      );
      System.out.println("c.numbers = " + c.numbers);
    }
  
    {
      JavaIssue15cCfg c = new JavaIssue15cCfg(
          ConfigFactory.parseString(
              "positions: [{lat: 1, lon: 2}, {lat:3, lon:4}]"
          )
      );
      System.out.println("c.positions = " + c.positions);
    }
  
    {
      JavaIssue15dCfg c = new JavaIssue15dCfg(
          ConfigFactory.parseString(
              "baz: [ [ {dd: 1, aa: true}, {dd: 2} ] ]"
          )
      );
      System.out.println("c.baz = " + c.baz);
    }

  }
}