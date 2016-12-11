package tscfg.generators.java

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification
import tscfg.example._

class JavaMainSpec extends Specification {
  import scala.collection.JavaConversions._

  "issue5" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue5.conf")
      r.classNames === Set("JavaIssue5Cfg", "Foo", "Config")
      r.fieldNames === Set("foo", "config", "bar")
    }
  }

  "issue10" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue10.conf")
      r.classNames === Set("JavaIssue10Cfg", "Main", "Email", "Reals$Elm")
      r.fieldNames === Set("server", "email", "main", "reals", "password", "foo")
    }

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

  "issue11" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue11.conf")
      r.classNames === Set("JavaIssue11Cfg", "Foo")
      r.fieldNames === Set("notify", "wait", "getClass", "clone", "finalize", "notifyAll", "toString", "foo")
    }
  }

  "issue12" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue12.conf")
      r.classNames === Set("JavaIssue12Cfg", "String", "Option", "Boolean", "Int_")
      r.fieldNames === Set("String", "Option", "Boolean", "int_", "bar")
    }
  }

  "issue13" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue13.conf")
      r.classNames === Set("JavaIssue13Cfg", "Issue")
      r.fieldNames === Set("issue", "optionalFoo")
    }
  }

  "issue14" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue14.conf")
      r.classNames === Set("JavaIssue14Cfg", "_0")
      r.fieldNames === Set("_0", "_1", "_2")
    }
  }

  "issue15a" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue15a.conf")
      r.classNames === Set("JavaIssue15aCfg")
      r.fieldNames === Set("ii")
    }

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
    "generate code" in {
      val r = JavaGen.generate("example/issue15b.conf")
      r.classNames === Set("JavaIssue15bCfg")
      r.fieldNames === Set("strings", "integers", "doubles", "longs", "booleans")
    }

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
    "generate code" in {
      val r = JavaGen.generate("example/issue15c.conf")
      r.classNames === Set("JavaIssue15cCfg", "Qaz", "Aa", "Positions$Elm", "Bb$Elm", "Attrs$Elm")
      r.fieldNames === Set("positions", "lat", "lon", "attrs", "foo", "qaz", "aa", "bb", "cc")
    }

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
    "generate code" in {
      val r = JavaGen.generate("example/issue15d.conf")
      r.classNames === Set("JavaIssue15dCfg", "Baz$Elm")
      r.fieldNames === Set("baz", "aa", "dd")
    }

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
    "generate code" in {
      val r = JavaGen.generate("example/issue15.conf")
      r.classNames === Set("JavaIssue15Cfg", "Positions$Elm", "Positions$Elm2")
      r.fieldNames === Set("positions", "numbers", "other", "stuff")
    }

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
    "generate code" in {
      val r = JavaGen.generate("example/duration.spec.conf")
      r.classNames === Set("JavaDurationCfg", "Durations")
      r.fieldNames === Set("durations", "days", "hours", "millis",
        "duration_ns",
        "duration_µs",
        "duration_ms",
        "duration_se",
        "duration_mi",
        "duration_hr",
        "duration_dy"
      )
    }

    "example 1" in {
      val c = new JavaDurationCfg(ConfigFactory.parseString(
        """
          |durations {
          |  days  = "10d"
          |  hours = "24h"
          |  duration_ns = "7ns"
          |  duration_µs = "7us"
          |  duration_ms = "7ms"
          |  duration_se = "7s"
          |  duration_mi = "7m"
          |  duration_hr = "7h"
          |  duration_dy = "7d"
          |}
          |""".stripMargin
      ))
      c.durations.days === 10
      c.durations.hours === 24
      c.durations.millis === 550000
      c.durations.duration_ns === 7
      c.durations.duration_µs === 7
      c.durations.duration_ms === 7
      c.durations.duration_se === 7
      c.durations.duration_mi === 7
      c.durations.duration_hr === 7
      c.durations.duration_dy === 7
    }
  }

}
