package tscfg.generators.scala

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification
import tscfg.example.{ScalaDurationCfg, _}
import tscfg.generators.GenOpts
import tscfg.model
import tscfg.model._
import model.implicits._


class ScalaMainSpec extends Specification {

  "literal values as types" should {
    "generate primitive types with given values as defaults" in {
      val r = ScalaGen.generate("example/example0.spec.conf")
      r.classNames === Set("ScalaExample0Cfg", "Service")
      r.fields === Map(
        "service"  → "ScalaExample0Cfg.Service",
        "url"      → "java.lang.String",
        "debug"    → "scala.Boolean",
        "doLog"    → "scala.Boolean",
        "factor"   → "scala.Double",
        "poolSize" → "scala.Int"
      )
    }
    "example with missing entries should get their defaults" in {
      val c = ScalaExample0Cfg(ConfigFactory.parseString(
        """
          |service = {
          |}
        """.stripMargin
      ))
      c.service.url      === "http://example.net/rest"
      c.service.poolSize === 32
      c.service.debug    === true
      c.service.doLog    === false
      c.service.factor   === 0.75
    }
  }

  "issue5" should {
    "generate code" in {
      val r = ScalaGen.generate("example/issue5.spec.conf")
      r.classNames === Set("ScalaIssue5Cfg", "Foo", "Config")
      r.fields.keySet === Set("foo", "config", "bar")
    }
  }

  "issue10" should {
    "generate code" in {
      val r = ScalaGen.generate("example/issue10.spec.conf")
      r.classNames === Set("ScalaIssue10Cfg", "Main", "Email", "Reals$Elm")
      r.fields.keySet === Set("server", "email", "main", "reals", "password", "foo")
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
      val r = ScalaGen.generate("example/issue11.spec.conf")
      r.classNames === Set("ScalaIssue11Cfg", "Foo")
      r.fields.keySet === Set("notify_", "wait_", "getClass_", "clone_", "finalize_", "notifyAll_", "toString_", "foo")
    }
  }

  "issue12" should {
    "generate code" in {
      val r = ScalaGen.generate("example/issue12.spec.conf")
      r.classNames === Set("ScalaIssue12Cfg", "String", "Option", "Boolean", "Int")
      r.fields.keySet === Set("String", "Option", "Boolean", "int", "bar")
    }
  }

  "issue13" should {
    "generate code" in {
      val r = ScalaGen.generate("example/issue13.spec.conf")
      r.classNames === Set("ScalaIssue13Cfg", "Issue")
      r.fields.keySet === Set("issue", "optionalFoo")
    }
  }

  "issue14" should {
    "generate code" in {
      val r = ScalaGen.generate("example/issue14.spec.conf")
      r.classNames === Set("ScalaIssue14Cfg", "_0")
      r.fields.keySet === Set("_0", "_1", "_2")
    }
  }

  "issue15a" should {
    "generate code" in {
      val r = ScalaGen.generate("example/issue15a.spec.conf")
      r.classNames === Set("ScalaIssue15aCfg")
      r.fields.keySet === Set("ii")
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
      val r = ScalaGen.generate("example/issue15b.spec.conf")
      r.classNames === Set("ScalaIssue15bCfg")
      r.fields.keySet === Set("strings", "integers", "doubles", "longs", "booleans")
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
      val r = ScalaGen.generate("example/issue15c.spec.conf")
      r.classNames === Set("ScalaIssue15cCfg", "Qaz", "Aa", "Positions$Elm", "Bb$Elm", "Attrs$Elm")
      r.fields.keySet === Set("positions", "lat", "lon", "attrs", "foo", "qaz", "aa", "bb", "cc")
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
        ScalaIssue15cCfg.Positions$Elm(
          attrs = List(List(ScalaIssue15cCfg.Positions$Elm.Attrs$Elm(foo = 99))),
          lat = 1,
          lon = 2
        ),
        ScalaIssue15cCfg.Positions$Elm(
          attrs = List(List(
            ScalaIssue15cCfg.Positions$Elm.Attrs$Elm(foo = 3),
            ScalaIssue15cCfg.Positions$Elm.Attrs$Elm(foo = 0)
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
      val r = ScalaGen.generate("example/issue15d.spec.conf")
      r.classNames === Set("ScalaIssue15dCfg", "Baz$Elm")
      r.fields.keySet === Set("baz", "aa", "dd")
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
      val r = ScalaGen.generate("example/issue15.spec.conf")
      r.classNames === Set("ScalaIssue15Cfg", "Positions$Elm")
      r.fields.keySet === Set("positions", "numbers", "other", "stuff")
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
            ScalaIssue15Cfg.Positions$Elm.Positions$Elm(other = 33, stuff = "baz")
          ))
        )
      )
    }
  }

  "duration" should {
    "generate code" in {
      val r = ScalaGen.generate("example/duration.spec.conf")
      r.classNames === Set("ScalaDurationCfg", "Durations")
      r.fields.keySet === Set("durations", "days", "hours", "millis",
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
      val c = ScalaDurationCfg(ConfigFactory.parseString(
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
      c.durations === ScalaDurationCfg.Durations(
        days = Some(10),
        hours = 24,
        millis = 550000,
        duration_ns = 7,
        duration_µs = 7,
        duration_ms = 7,
        duration_se = 7,
        duration_mi = 7,
        duration_hr = 7,
        duration_dy = 7
      )
    }
  }

  "issue19" should {
    """put underscores for key having $""" in {
      val r = ScalaGen.generate("example/issue19.spec.conf")
      r.classNames === Set("ScalaIssue19Cfg")
      r.fields === Map(
        "do_log"  → "scala.Boolean",
        "_$_foo_" → "java.lang.String"
      )
    }

    "example" in {
      val c = ScalaIssue19Cfg(ConfigFactory.parseString(
        """
          |"do log" : true
          |"$_foo"  : some string
        """.stripMargin
      ))
      c.do_log  === true
      c._$_foo_ === "some string"
    }
  }

  "given class name starting with $_" should {
    "generate warning" in {
      val genOpts = GenOpts("tscfg.example", "Classy", j7 = true)
      val r = new ScalaGen(genOpts).generate(ObjectType())
      r.classNames === Set("Classy")
      r.fields === Map()
      // TODO actually verify generated warning
    }
  }

  "keys starting with $_" should {
    val objectType = ObjectType(
      "$_baz" := STRING | "some value",
      "other" := ObjectType(
        "$_foo" := DOUBLE
      )
    )

    "generate warnings" in {
      val genOpts = GenOpts("tscfg.example", "Classy", j7 = true)
      val r = new ScalaGen(genOpts).generate(objectType)

      r.classNames === Set("Classy", "Other")
      r.fields === Map(
        "$_baz" → "java.lang.String",
        "other" → "Classy.Other",
        "$_foo" → "scala.Double"
      )
      // TODO actually verify the generated warnings
    }
  }
  "issue22" should {
    "generate DURATION type" in {
      val r = ScalaGen.generate("example/issue22.spec.conf")
      r.classNames === Set("ScalaIssue22Cfg")
      r.fields === Map(
        "idleTimeout" → "scala.Long"
      )
    }

    "example with default value" in {
      val c = ScalaIssue22Cfg(ConfigFactory.parseString(
        """
          | idleTimeout = 1 hour
        """.stripMargin
      ))
      c.idleTimeout === 3600*1000
    }
  }

  "issue23" should {
    "generate SIZE type" in {
      val r = ScalaGen.generate("example/issue23.spec.conf")
      r.classNames === Set("ScalaIssue23Cfg")
      r.fields === Map(
        "sizeReq"    → "scala.Long",
        "sizeOpt"    → "scala.Option[scala.Long]",
        "sizeOptDef" → "scala.Long",
        "sizes"      → "scala.List[scala.Long]",
        "sizes2"     → "scala.List[scala.List[scala.Long]]"
      )
    }

    "example" in {
      val c = ScalaIssue23Cfg(ConfigFactory.parseString(
        """
          |sizeReq = "2048K"
          |sizeOpt = "1024000"
          |sizes   = [ 1000, "64G", "16kB" ]
          |sizes2  = [[ 1000, "64G" ], [ "16kB" ] ]
        """.stripMargin
      ))
      c.sizeReq === 2048*1024
      c.sizeOpt === Some(1024000)
      c.sizeOptDef === 1024
      c.sizes === List(1000, 64*1024*1024*1024L, 16*1000)
      c.sizes2 === List(
        List(1000, 64*1024*1024*1024L),
        List(16*1000))
    }
  }

  "issue30" should {
    "generate as indicated for useBackticks" in {
      val r = ScalaGen.generate("example/issue30.spec.conf",
                                useBackticks = true)

      r.classNames === Set("ScalaIssue30Cfg", "`Foo-object`")
      r.fields.size === 4
      r.fields("`foo-object`" ) === "ScalaIssue30Cfg.`Foo-object`"
      r.fields("`bar-baz`"    ) === "java.lang.String"
      r.fields("`0`"          ) === "java.lang.String"
      r.fields("`other#stuff`") === "scala.Int"
    }
  }
}
