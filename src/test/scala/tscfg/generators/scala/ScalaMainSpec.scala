package tscfg.generators.scala

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification
import tscfg.example.{ScalaDurationCfg, _}

class ScalaMainSpec extends Specification {

  "issue5" should {
    "generate code" in {
      val r = ScalaGenerator.generate("example/issue5.conf")
      r.classNames === Set("ScalaIssue5Cfg", "Foo", "Config")
      r.fieldNames === Set("foo", "config", "bar")
    }
  }

  "issue10" should {
    "generate code" in {
      val r = ScalaGenerator.generate("example/issue10.conf")
      r.classNames === Set("ScalaIssue10Cfg", "Main", "Email", "Reals$Elm")
      r.fieldNames === Set("server", "email", "main", "reals", "password", "foo")
    }

    "example 1" in {
      val c = ScalaIssue10Cfg(ConfigFactory.parseString(
        """
          |main = {
          |  reals = [ { foo: 3.14 } ]
          |}
        """.stripMargin
      ))
      c.main.email must beNone
      c.main.reals must beSome(List(ScalaIssue10Cfg.Main.Reals$Elm(foo = 3.14)))
    }

    "example 2" in {
      val c = ScalaIssue10Cfg(ConfigFactory.parseString(
        """
          |main = {
          |  email = {
          |    server = "foo"
          |    password = "pw"
          |  }
          |}
          |""".stripMargin
      ))
      c.main.email must beSome(ScalaIssue10Cfg.Main.Email(password = "pw", server = "foo"))
      c.main.reals must beNone
    }
  }

  "issue11" should {
    "generate code" in {
      val r = ScalaGenerator.generate("example/issue11.conf")
      r.classNames === Set("ScalaIssue11Cfg", "Foo")
      r.fieldNames === Set("notify_", "wait_", "getClass_", "clone_", "finalize_", "notifyAll_", "toString_", "foo")
    }
  }

  "issue12" should {
    "generate code" in {
      val r = ScalaGenerator.generate("example/issue12.conf")
      r.classNames === Set("ScalaIssue12Cfg", "String", "Option", "Boolean", "Int")
      r.fieldNames === Set("String", "Option", "Boolean", "int", "bar")
    }
  }

  "issue13" should {
    "generate code" in {
      val r = ScalaGenerator.generate("example/issue13.conf")
      r.classNames === Set("ScalaIssue13Cfg", "Issue")
      r.fieldNames === Set("issue", "optionalFoo")
    }
  }

  "issue14" should {
    "generate code" in {
      val r = ScalaGenerator.generate("example/issue14.conf")
      r.classNames === Set("ScalaIssue14Cfg", "_0")
      r.fieldNames === Set("_0", "_1", "_2")
    }
  }

  "issue15a" should {
    "generate code" in {
      val r = ScalaGenerator.generate("example/issue15a.conf")
      r.classNames === Set("ScalaIssue15aCfg")
      r.fieldNames === Set("ii")
    }

    "example 1" in {
      val c = ScalaIssue15aCfg(ConfigFactory.parseString(
        """
          |ii: [1,2 ,3 ]
        """.stripMargin
      ))
      c.ii === List(1, 2, 3)
    }

    "example 2" in {
      val c = ScalaIssue15aCfg(ConfigFactory.parseString(
        """
          |ii: []
        """.stripMargin
      ))
      c.ii === List.empty
    }
  }

  "issue15b" should {
    "generate code" in {
      val r = ScalaGenerator.generate("example/issue15b.conf")
      r.classNames === Set("ScalaIssue15bCfg")
      r.fieldNames === Set("strings", "integers", "doubles", "longs", "booleans")
    }

    "example 1" in {
      val c = ScalaIssue15bCfg(ConfigFactory.parseString(
        """
          |strings:  [hello, world, true]
          |integers: [[1, 2, 3], [4, 5]]
          |doubles:  [3.14, 2.7182, 1.618]
          |longs:    [1, 9999999999]
          |booleans: [true, false]
          |""".stripMargin
      ))
      c.strings  === List("hello", "world", "true")
      c.integers === List(List(1, 2, 3), List(4, 5))
      c.doubles  === List(3.14, 2.7182, 1.618)
      c.longs    === List(1, 9999999999L)
      c.booleans === List(true, false)
    }
  }

  "issue15c" should {
    "generate code" in {
      val r = ScalaGenerator.generate("example/issue15c.conf")
      r.classNames === Set("ScalaIssue15cCfg", "Qaz", "Aa", "Positions$1$Elm", "Bb$Elm", "Attrs$2$Elm")
      r.fieldNames === Set("positions", "lat", "lon", "attrs", "foo", "qaz", "aa", "bb", "cc")
    }

    "example 1" in {
      val c = ScalaIssue15cCfg(ConfigFactory.parseString(
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
      c.positions  === List(
        ScalaIssue15cCfg.Positions$1$Elm(
          attrs = List(List(ScalaIssue15cCfg.Positions$1$Elm.Attrs$2$Elm(foo = 99))),
          lat = 1,
          lon = 2
        ),
        ScalaIssue15cCfg.Positions$1$Elm(
          attrs = List(List(
            ScalaIssue15cCfg.Positions$1$Elm.Attrs$2$Elm(foo = 3),
            ScalaIssue15cCfg.Positions$1$Elm.Attrs$2$Elm(foo = 0)
          )),
          lat = 3,
          lon = 4
        )
      )
      c.qaz === ScalaIssue15cCfg.Qaz(
        aa = ScalaIssue15cCfg.Qaz.Aa(
          bb = List(ScalaIssue15cCfg.Qaz.Aa.Bb$Elm(cc = "hoho"))
        )
      )
    }
  }

  "issue15d" should {
    "generate code" in {
      val r = ScalaGenerator.generate("example/issue15d.conf")
      r.classNames === Set("ScalaIssue15dCfg", "Baz$Elm")
      r.fieldNames === Set("baz", "aa", "dd")
    }

    "example 1" in {
      val c = ScalaIssue15dCfg(ConfigFactory.parseString(
        """
          |baz: [ [ {dd: 1, aa: true}, {dd: 2} ] ]
          |""".stripMargin
      ))
      c.baz === List(List(
        ScalaIssue15dCfg.Baz$Elm(aa = Some(true), dd = 1),
        ScalaIssue15dCfg.Baz$Elm(aa = None, dd = 2)
      ))
    }
  }

  "issue15" should {
    "generate code" in {
      val r = ScalaGenerator.generate("example/issue15.conf")
      r.classNames === Set("ScalaIssue15Cfg", "Positions$Elm", "Positions$2$Elm")
      r.fieldNames === Set("positions", "numbers", "other", "stuff")
    }

    "example 1" in {
      val c = ScalaIssue15Cfg(ConfigFactory.parseString(
        """
          |positions: [
          |  {
          |    numbers: [ 1, 2, 3 ]
          |    positions: [ [ { other: 33, stuff: baz } ] ]
          |  }
          |]
          |""".stripMargin
      ))
      c.positions === List(
        ScalaIssue15Cfg.Positions$Elm(
          numbers = List(1, 2, 3),
          positions = List(List(
            ScalaIssue15Cfg.Positions$Elm.Positions$2$Elm(other = 33, stuff = "baz")
          ))
        )
      )
    }
  }

  "duration" should {
    "generate code" in {
      val r = ScalaGenerator.generate("example/duration.spec.conf")
      r.classNames === Set("ScalaDurationCfg", "Durations")
      r.fieldNames === Set("durations", "days", "hours", "millis")
    }

    "example 1" in {
      val c = ScalaDurationCfg(ConfigFactory.parseString(
        """
          |durations {
          |  days  = "10d"
          |  hours = "24h"
          |}
          |""".stripMargin
      ))
      c.durations === ScalaDurationCfg.Durations(
        days = Some(10),
        hours = 24,
        millis = 550000
      )
    }
  }

}
