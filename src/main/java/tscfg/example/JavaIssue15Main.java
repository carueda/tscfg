package tscfg.example;

import com.typesafe.config.ConfigFactory;
    
public class JavaIssue15Main {
  public static void main(String[] args) {
    {
      JavaIssue15bCfg c = new JavaIssue15bCfg(
          ConfigFactory.parseString(
              "strings:  [hello, world, true]        \n" +
              "integers: [[1, 2, 3], [4, 5]]   \n" +
              "doubles:  [3.14, 2.7182, 1.618] \n" +
              "longs:    [1, 9999999999]       \n" +
              "booleans: [true, false]"
          )
      );
      System.out.println("c.strings  = " + c.strings);
      System.out.println("c.integers = " + c.integers);
      System.out.println("c.doubles  = " + c.doubles);
      System.out.println("c.longs    = " + c.longs);
      System.out.println("c.booleans = " + c.booleans);
    }
  
    {
      System.out.println();
      JavaIssue15cCfg c = new JavaIssue15cCfg(
          ConfigFactory.parseString(
              "positions: [{lat: 1, lon: 2}, {lat:3, lon:4}]"
          )
      );
      System.out.println("c.positions = " + c.positions);
    }
  
    {
      System.out.println();
      JavaIssue15dCfg c = new JavaIssue15dCfg(
          ConfigFactory.parseString(
              "baz: [ [ {dd: 1, aa: true}, {dd: 2} ] ]"
          )
      );
      System.out.println("c.baz = " + c.baz);
    }

    {
      System.out.println();
      JavaIssue15Cfg c = new JavaIssue15Cfg(
          ConfigFactory.parseString(
              "names: [ hi there ]\n" +
              "positions: [\n" +
              "  {\n" +
              "    lon: -121.0\n" +
              "    lat: 34.0\n" +
              "    foo: {\n" +
              "      bar: hello world\n" +
              "    }\n" +
              "    numbers: [ 1, 2, 3 ]\n" +
              "  }\n" +
              "]"
          )
      );
      System.out.println("c.names = " + c.names);
      System.out.println("c.positions = " + c.positions);
      System.out.println("c.positions.get(0).foo.bar = " + c.positions.get(0).foo.bar);
    }

  }
}