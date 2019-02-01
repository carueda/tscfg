package tscfg.generators.java

import java.util.Optional
import java.time.Duration
import java.time.temporal.ChronoUnit.MICROS

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification
import tscfg.example._
import tscfg.generators.GenOpts
import tscfg.model
import tscfg.model._
import model.implicits._


class JavaMainSpec extends Specification {
  import scala.collection.JavaConverters._

  "literal values as types" should {
    "generate primitive types with given values as defaults" in {
      val r = JavaGen.generate("example/example0.spec.conf")
      r.classNames === Set("JavaExample0Cfg", "Service")
      r.fields === Map(
        "service"  → "JavaExample0Cfg.Service",
        "url"      → "java.lang.String",
        "debug"    → "boolean",
        "doLog"    → "boolean",
        "factor"   → "double",
        "poolSize" → "int"
      )
    }

    "example with missing entries should get their defaults" in {
      val c = new JavaExample0Cfg(ConfigFactory.parseString(
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
      val r = JavaGen.generate("example/issue5.spec.conf")
      r.classNames === Set("JavaIssue5Cfg", "Foo", "Config")
      r.fields.keySet === Set("foo", "config", "bar")
    }
  }

  "issue10" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue10.spec.conf")
      r.classNames === Set("JavaIssue10Cfg", "Main", "Email", "Reals$Elm")
      r.fields.keySet === Set("server", "email", "main", "reals", "password", "foo")
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
      val r = JavaGen.generate("example/issue11.spec.conf")
      r.classNames === Set("JavaIssue11Cfg", "Foo")
      r.fields.keySet === Set("notify", "wait", "getClass", "clone", "finalize", "notifyAll", "toString", "foo")
    }
  }

  "issue12" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue12.spec.conf")
      r.classNames === Set("JavaIssue12Cfg", "String", "Option", "Boolean", "Int_")
      r.fields.keySet === Set("String", "Option", "Boolean", "int_", "bar")
    }
  }

  "issue13" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue13.spec.conf")
      r.classNames === Set("JavaIssue13Cfg", "Issue")
      r.fields.keySet === Set("issue", "optionalFoo")
    }
  }

  "issue14" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue14.spec.conf")
      r.classNames === Set("JavaIssue14Cfg", "_0")
      r.fields.keySet === Set("_0", "_1", "_2")
    }
  }

  "issue15a" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue15a.spec.conf")
      r.classNames === Set("JavaIssue15aCfg")
      r.fields.keySet === Set("ii")
    }

    "example 1" in {
      val c = new JavaIssue15aCfg(ConfigFactory.parseString(
        """
          |ii: [1,2 ,3 ]
        """.stripMargin
      ))
      c.ii.asScala.toList === List(1, 2, 3)
    }

    "example 2" in {
      val c = new JavaIssue15aCfg(ConfigFactory.parseString(
        """
          |ii: []
        """.stripMargin
      ))
      c.ii.asScala.toList === List.empty
    }
  }

  "issue15b" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue15b.spec.conf")
      r.classNames === Set("JavaIssue15bCfg")
      r.fields.keySet === Set("strings", "integers", "doubles", "longs", "booleans")
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
      c.strings .asScala.toList === List("hello", "world", "true")
      c.integers.asScala.toList.map(_.asScala.toList) === List(List(1, 2, 3), List(4, 5))
      c.doubles .asScala.toList === List(3.14, 2.7182, 1.618)
      c.longs   .asScala.toList === List(1, 9999999999L)
      c.booleans.asScala.toList === List(true, false)
    }
  }

  "issue15c" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue15c.spec.conf")
      r.classNames === Set("JavaIssue15cCfg", "Qaz", "Aa", "Positions$Elm", "Bb$Elm", "Attrs$Elm")
      r.fields.keySet === Set("positions", "lat", "lon", "attrs", "foo", "qaz", "aa", "bb", "cc")
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
      val r = JavaGen.generate("example/issue15d.spec.conf")
      r.classNames === Set("JavaIssue15dCfg", "Baz$Elm")
      r.fields.keySet === Set("baz", "aa", "dd")
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
      val r = JavaGen.generate("example/issue15.spec.conf")
      r.classNames === Set("JavaIssue15Cfg", "Positions$Elm", "Positions$Elm2")
      r.fields.keySet === Set("positions", "numbers", "other", "stuff")
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
      c.positions.get(0).numbers.asScala.toList === List(1, 2, 3)
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

  "duration2" should {
    "generate code" in {
      val r = JavaGen.generate("example/duration2.spec.conf", useDurations = true)
      r.classNames === Set("JavaDuration2Cfg", "Durations")
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
      val c = new JavaDuration2Cfg(ConfigFactory.parseString(
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
      c.durations.days === Duration.ofDays(10)
      c.durations.hours === Duration.ofHours(24)
      c.durations.millis === Duration.ofMillis(550000)
      c.durations.duration_ns === Duration.ofNanos(7)
      c.durations.duration_µs === Duration.of(7, MICROS)
      c.durations.duration_ms === Duration.ofMillis(7)
      c.durations.duration_se === Duration.ofSeconds(7)
      c.durations.duration_mi === Duration.ofMinutes(7)
      c.durations.duration_hr === Duration.ofHours(7)
      c.durations.duration_dy === Duration.ofDays(7)
    }
  }

  "duration3" should {
    "generate code" in {
      val r = JavaGen.generate("example/duration3.spec.conf", useDurations = true, genGetters = true)
      r.classNames === Set("JavaDuration3Cfg", "Durations")
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
      val c = new JavaDuration3Cfg(ConfigFactory.parseString(
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
      c.durations.getDays === Duration.ofDays(10)
      c.durations.getHours === Duration.ofHours(24)
      c.durations.getMillis === Duration.ofMillis(550000)
      c.durations.getDuration_ns === Duration.ofNanos(7)
      c.durations.getDuration_µs === Duration.of(7, MICROS)
      c.durations.getDuration_ms === Duration.ofMillis(7)
      c.durations.getDuration_se === Duration.ofSeconds(7)
      c.durations.getDuration_mi === Duration.ofMinutes(7)
      c.durations.getDuration_hr === Duration.ofHours(7)
      c.durations.getDuration_dy === Duration.ofDays(7)
    }
  }

  "issue19" should {
    """put underscores for key having $""" in {
      val r = JavaGen.generate("example/issue19.spec.conf")
      r.classNames === Set("JavaIssue19Cfg")
      r.fields === Map(
        "do_log"  → "boolean",
        "_$_foo_" → "java.lang.String"
      )
    }

    "example" in {
      val c = new JavaIssue19Cfg(ConfigFactory.parseString(
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
      val r = new JavaGen(genOpts).generate(ObjectType())
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
      val r = new JavaGen(genOpts).generate(objectType)
      r.classNames === Set("Classy", "Other")
      r.fields === Map(
        "$_baz" → "java.lang.String",
        "other" → "Classy.Other",
        "$_foo" → "double"
      )
      // TODO actually verify the generated warnings
    }
  }

  "issue22" should {
    "generate DURATION type" in {
      val r = JavaGen.generate("example/issue22.spec.conf")
      r.classNames === Set("JavaIssue22Cfg")
      r.fields === Map(
        "idleTimeout" → "long"
      )
    }

    "example with default value" in {
      val c = new JavaIssue22Cfg(ConfigFactory.parseString(
        """
          |  # empty
        """.stripMargin
      ))
      c.idleTimeout  === 75000
    }
    "example with new value" in {
      val c = new JavaIssue22Cfg(ConfigFactory.parseString(
        """
          | idleTimeout = 1 hour
        """.stripMargin
      ))
      c.idleTimeout === 3600*1000
    }
  }

  "issue23" should {
    "generate SIZE type" in {
      val r = JavaGen.generate("example/issue23.spec.conf")
      r.classNames === Set("JavaIssue23Cfg")
      r.fields === Map(
        "sizeReq"    → "long",
        "sizeOpt"    → "java.lang.Long",
        "sizeOptDef" → "long",
        "sizes"      → "java.util.List<java.lang.Long>",
        "sizes2"     → "java.util.List<java.util.List<java.lang.Long>>"
      )
    }

    "example" in {
      val c = new JavaIssue23Cfg(ConfigFactory.parseString(
        """
          |sizeReq = "2048K"
          |sizeOpt = "1024000"
          |sizes   = [ 1000, "64G", "16kB" ]
          |sizes2  = [[ 1000, "64G" ], [ "16kB" ] ]
        """.stripMargin
      ))
      c.sizeReq === 2048*1024
      c.sizeOpt === 1024000
      c.sizeOptDef === 1024
      c.sizes.asScala.toList === List(1000, 64*1024*1024*1024L, 16*1000)
      c.sizes2.asScala.toList.map(_.asScala.toList) === List(
        List(1000, 64*1024*1024*1024L),
        List(16*1000))
    }
  }

  "issue31" should {
    "generate getters" in {
      val r = JavaGen.generate("example/issue31.spec.conf",
                               genGetters = true)

      r.classNames === Set("JavaIssue31Cfg", "B", "D")
      r.fields.size === 5
      r.fields("a" ) === "int"
      r.fields("b" ) === "JavaIssue31Cfg.B"
      r.fields("c" ) === "java.lang.String"
      r.fields("d" ) === "B.D"
      r.fields("e" ) === "boolean"

      r.getters.size === 5
      r.getters("getA" ) === "int"
      r.getters("getB" ) === "JavaIssue31Cfg.B"
      r.getters("getC" ) === "java.lang.String"
      r.getters("getD" ) === "B.D"
      r.getters("getE" ) === "boolean"
    }

    "verify generated getters" in {
      val c = new JavaIssue31Cfg(ConfigFactory.parseString(
        """
          |a = 1
          |b.c = foo
        """.stripMargin
      ))
      c.getA === 1
      c.getB.getC === "foo"
      c.getB.getD.getE === false
    }
  }

  "issue33" should {
    "generate empty config for object level" in {
      val c = new JavaIssue33Cfg(ConfigFactory.parseString(""))
      c.endpoint.url === "http://example.net"
    }

    "generate empty config for dot notated object level" in {
      val c = new JavaIssue33aCfg(ConfigFactory.parseString(""))
      c.endpoint.more.url === "http://example.net"
    }

    "generate config for object first level" in {
      val c = new JavaIssue33bCfg(ConfigFactory.parseString(""))
      c.endpoint.url === "http://example.net"
      c.endpoint.foo === null
      c.endpoint.baz.key === "bar"
    }

    "generate config for object nested level" in {
      val c = new JavaIssue33bCfg(ConfigFactory.parseString("endpoint.foo = 1"))
      c.endpoint.url === "http://example.net"
      c.endpoint.foo === 1
      c.endpoint.baz.key === "bar"
    }

    "generate config for sub-object under required object" in {
      val c = new JavaIssue33cCfg(ConfigFactory.parseString("endpoint.req = foo"))
      c.endpoint.req === "foo"
      c.endpoint.optObj.key === "bar"
    }
  }

  "issue40" should {
    "capture explicit memory size value in spec as a long literal" in {
      val c = new JavaIssue40Cfg(ConfigFactory.parseString(""))
      // well, the actual test is that the generated class compiles
      c.memory === 53687091200L
    }
  }

  "issue41" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue41.spec.conf", useOptionals = true)
      r.classNames === Set("JavaIssue41Cfg", "C", "F")
      r.fields.keySet === Set("a", "b", "c", "d", "e", "f", "g", "h", "i")
    }

    "example 1" in {
      val c = new JavaIssue41Cfg(ConfigFactory.parseString(
        """
          |{}
        """.stripMargin
      ))
      c.a === Optional.empty()
      c.b === 10
      c.c.d === Optional.empty()
      c.c.e === "hello"
      c.c.f === Optional.empty()
      c.i === Optional.empty()
    }

    "example 2" in {
      val c = new JavaIssue41Cfg(ConfigFactory.parseString(
        """
          |a = 5
          |b = 6
          |c = {
          |  d = "c.d"
          |  e = "c.e"
          |  f = {
          |    g = 5
          |    h = "c.f.h"
          |  }
          |}
          |i = [1,  2, 3]
        """.stripMargin
      ))
      c.a === Optional.of(5)
      c.b === 6
      c.c.d === Optional.of("c.d")
      c.c.e === "c.e"
      c.c.f.get().g === 5
      c.c.f.get().h === "c.f.h"
      c.i.get() === List(1.0,2.0,3.0).asJava
    }
  }
}
