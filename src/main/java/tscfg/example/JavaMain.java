package tscfg.example;

import com.typesafe.config.ConfigFactory;
    
public class JavaMain {
  public static void main(String[] args) {
    {
      System.out.println("\nissue10:");
      JavaIssue10Cfg c0 = new JavaIssue10Cfg(
          ConfigFactory.parseString(
              "main = {              \n" +
              "  reals = [ { foo: 3.14 } ]   \n" +
              "}"
          )
      );
      System.out.println("c0.main.email  = " + c0.main.email);
      System.out.println("c0.main.reals  = " + c0.main.reals);

      JavaIssue10Cfg c1 = new JavaIssue10Cfg(
          ConfigFactory.parseString(
              "main = {               \n" +
              "  email = {            \n" +
              "    server = \"foo\"   \n" +
              "    password = \"pw\"  \n" +
              "  }\n" +
              "}"
          )
      );
      System.out.println("c1.main.email  = " + c1.main.email);
      System.out.println("c1.main.reals  = " + c1.main.reals);
    }
  
    {
      System.out.println("\nJavaIssue15aCfg:");
      JavaIssue15aCfg c = new JavaIssue15aCfg(
          ConfigFactory.parseString(
              "ii: [1,2 ,3]"
          )
      );
      System.out.println("c.ii  = " + c.ii);
    }
  
    {
      System.out.println("\nJavaIssue15bCfg:");
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
      System.out.println("\nJavaIssue15cCfg:");
      JavaIssue15cCfg c = new JavaIssue15cCfg(
          ConfigFactory.parseString(
              "positions: [\n" +
                  "{ lat: 1, lon: 2, attrs = [ [ {foo: 99}             ] ] },\n" +
                  "{ lat: 3, lon: 4, attrs = [ [ {foo: 3.14}, {foo: 0} ] ] }\n" +
              "]\n" +
              "qaz = {\n" +
              "  aa = { bb = [ { cc: hoho } ]  }\n" +
              "}"
          )
      );
      System.out.println("c.positions = " + c.positions);
      System.out.println("c.positions.get(1).attrs = " + c.positions.get(1).attrs);
    }
  
    {
      System.out.println("\nJavaIssue15dCfg:");
      JavaIssue15dCfg c = new JavaIssue15dCfg(
          ConfigFactory.parseString(
              "baz: [ [ {dd: 1, aa: true}, {dd: 2} ] ]"
          )
      );
      System.out.println("c.baz = " + c.baz);
    }

    {
      System.out.println("\nJavaIssue15Cfg:");
      JavaIssue15Cfg c = new JavaIssue15Cfg(
          ConfigFactory.parseString(
              "positions: [\n" +
              "  {\n" +
              "    numbers: [ 1, 2, 3 ] \n" +
              "    positions: [ [ { other: 33, stuff: baz } ] ] \n" +
              "  }\n" +
              "]"
          )
      );
      System.out.println("c.positions = " + c.positions);
      System.out.println("c.positions.get(0).positions.get(0).get(0).stuff = " +
                          c.positions.get(0).positions.get(0).get(0).stuff);
    }

    {
      System.out.println("\nduration:");
      JavaDurationCfg c = new JavaDurationCfg(
          ConfigFactory.parseString(
              "durations {            \n" +
              "  days = \"48hour\"    \n" +
              "  hours = \"1d\"       \n" +
              "}"
          )
      );
      System.out.println("c.durations.days   = " + c.durations.days);
      System.out.println("c.durations.hours  = " + c.durations.hours);
      System.out.println("c.durations.millis = " + c.durations.millis);
    }

  }
}