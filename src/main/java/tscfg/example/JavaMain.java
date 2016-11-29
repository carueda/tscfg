package tscfg.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.typesafe.config.ConfigFactory;
    
public class JavaMain {
  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  
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
      System.out.println("c0 = " + gson.toJson(c0));

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
      System.out.println("c1 = " + gson.toJson(c1));
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
      System.out.println("c = " + gson.toJson(c));
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
      System.out.println("c = " + gson.toJson(c));
    }
  
    {
      System.out.println("\nJavaIssue15dCfg:");
      JavaIssue15dCfg c = new JavaIssue15dCfg(
          ConfigFactory.parseString(
              "baz: [ [ {dd: 1, aa: true}, {dd: 2} ] ]"
          )
      );
      System.out.println("c = " + gson.toJson(c));
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
      System.out.println("c = " + gson.toJson(c));
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
      System.out.println("c = " + gson.toJson(c));
    }
  }
}
