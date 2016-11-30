package tscfg.generators.java

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification
import tscfg.example._

class JavaMainSpec extends Specification {
  import scala.collection.JavaConversions._

  "issue10" should {
    "example 1" in {
      val c = new JavaIssue10Cfg(ConfigFactory.parseString(
        """
          |main = {
          |  reals = [ { foo: 3.14 } ]
          |}
        """.stripMargin
      ))
      c.main.email must beNull
      c.main.reals.size() === 1
      c.main.reals.get(0).foo === 3.14
    }

    "example 2" in {
      val c = new JavaIssue10Cfg(ConfigFactory.parseString(
        """
          |main = {
          |  email = {
          |    server = "foo"
          |    password = "pw"
          |  }
          |}
          |""".stripMargin
      ))
      c.main.email.password === "pw"
      c.main.email.server === "foo"
      c.main.reals must beNull
    }
  }

  "issue15a" should {
    "example 1" in {
      val c = new JavaIssue15aCfg(ConfigFactory.parseString(
        """
          |ii: [1,2 ,3 ]
        """.stripMargin
      ))
      c.ii.toList === List(1, 2, 3)
    }

    "example 2" in {
      val c = new JavaIssue15aCfg(ConfigFactory.parseString(
        """
          |ii: []
        """.stripMargin
      ))
      c.ii.toList === List.empty
    }
  }

  "issue15b" should {
    "example 1" in {
      val c = new JavaIssue15bCfg(ConfigFactory.parseString(
        """
          |strings:  [hello, world, true]
          |integers: [[1, 2, 3], [4, 5]]
          |doubles:  [3.14, 2.7182, 1.618]
          |longs:    [1, 9999999999]
          |booleans: [true, false]
          |""".stripMargin
      ))
      c.strings .toList === List("hello", "world", "true")
      c.integers.toList.map(_.toList) === List(List(1, 2, 3), List(4, 5))
      c.doubles .toList === List(3.14, 2.7182, 1.618)
      c.longs   .toList === List(1, 9999999999L)
      c.booleans.toList === List(true, false)
    }
  }

  "issue15c" should {
    "example 1" in {
      val c = new JavaIssue15cCfg(ConfigFactory.parseString(
        """
          |positions: [
          |  { lat: 1, lon: 2, attrs = [ [ {foo: 99}          ] ] },
          |  { lat: 3, lon: 4, attrs = [ [ {foo: 3}, {foo: 0} ] ] }
          |]
          |qaz = {
          |  aa = { bb = [ { cc: hoho } ]  }
          |}
          |""".stripMargin
      ))
      c.positions.size() === 2
      c.positions.get(0).lat === 1
      c.positions.get(0).lon === 2
      c.positions.get(0).attrs.size() === 1
      c.positions.get(0).attrs.get(0).size() === 1
      c.positions.get(0).attrs.get(0).get(0).foo === 99
      c.positions.get(1).lat === 3
      c.positions.get(1).lon === 4
      c.positions.get(1).attrs.size() === 1
      c.positions.get(1).attrs.get(0).size() === 2
      c.positions.get(1).attrs.get(0).get(0).foo === 3
      c.positions.get(1).attrs.get(0).get(1).foo === 0
      c.qaz.aa.bb.size() === 1
      c.qaz.aa.bb.get(0).cc === "hoho"
    }
  }

  "issue15d" should {
    "example 1" in {
      val c = new JavaIssue15dCfg(ConfigFactory.parseString(
        """
          |baz: [ [ {dd: 1, aa: true}, {dd: 2} ] ]
          |""".stripMargin
      ))
      c.baz.size() === 1
      c.baz.get(0).size() === 2
      c.baz.get(0).get(0).aa === true
      c.baz.get(0).get(0).dd === 1
      c.baz.get(0).get(1).aa === null
      c.baz.get(0).get(1).dd === 2
    }
  }

  "issue15" should {
    "example 1" in {
      val c = new JavaIssue15Cfg(ConfigFactory.parseString(
        """
          |positions: [
          |  {
          |    numbers: [ 1, 2, 3 ]
          |    positions: [ [ { other: 33, stuff: baz } ] ]
          |  }
          |]
          |""".stripMargin
      ))
      c.positions.size() === 1
      c.positions.get(0).numbers.toList === List(1, 2, 3)
      c.positions.get(0).positions.size() === 1
      c.positions.get(0).positions.get(0).size() === 1
      c.positions.get(0).positions.get(0).get(0).other === 33
      c.positions.get(0).positions.get(0).get(0).stuff === "baz"
    }
  }

  "duration" should {
    "example 1" in {
      val c = new JavaDurationCfg(ConfigFactory.parseString(
        """
          |durations {
          |  days  = "10d"
          |  hours = "24h"
          |}
          |""".stripMargin
      ))
      c.durations.days === 10
      c.durations.hours === 24
    }
  }

}
