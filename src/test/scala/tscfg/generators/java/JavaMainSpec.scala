package tscfg.generators.java

import java.io.File
import java.util.Optional
import java.time.Duration
import java.time.temporal.ChronoUnit.MICROS
import com.typesafe.config.ConfigFactory
import tscfg.example._
import tscfg.generators.GenOpts
import tscfg.model
import tscfg.model._
import model.implicits._
import org.scalatest.wordspec.AnyWordSpec
import tscfg.exceptions.ObjectDefinitionException

class JavaMainSpec extends AnyWordSpec {

  import scala.jdk.CollectionConverters._

  "(java) literal values as types" should {
    "generate primitive types with given values as defaults" in {
      val r = JavaGen.generate("example/example0.spec.conf")
      assert(r.classNames === Set("JavaExample0Cfg", "Service"))
      assert(
        r.fields === Map(
          "service"  -> "JavaExample0Cfg.Service",
          "url"      -> "java.lang.String",
          "debug"    -> "boolean",
          "doLog"    -> "boolean",
          "factor"   -> "double",
          "poolSize" -> "int"
        )
      )
    }

    "example with missing entries should get their defaults" in {
      val c = new JavaExample0Cfg(
        ConfigFactory.parseString(
          """
          |service = {
          |}
        """.stripMargin
        )
      )
      assert(c.service.url === "http://example.net/rest")
      assert(c.service.poolSize === 32)
      assert(c.service.debug === true)
      assert(c.service.doLog === false)
      assert(c.service.factor === 0.75)
    }
  }

  "(java) issue5" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue5.spec.conf")
      assert(r.classNames === Set("JavaIssue5Cfg", "Foo", "Config"))
      assert(r.fields.keySet === Set("foo", "config", "bar"))
    }
  }

  "(java) issue10" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue10.spec.conf")
      assert(
        r.classNames === Set("JavaIssue10Cfg", "Main", "Email", "Reals$Elm")
      )
      assert(
        r.fields.keySet === Set(
          "server",
          "email",
          "main",
          "reals",
          "password",
          "foo"
        )
      )
    }

    "example 1" in {
      val c = new JavaIssue10Cfg(
        ConfigFactory.parseString(
          """
          |main = {
          |  reals = [ { foo: 3.14 } ]
          |}
        """.stripMargin
        )
      )
      assert(c.main.email === null)
      assert(c.main.reals.size() === 1)
      assert(c.main.reals.get(0).foo === 3.14)
    }

    "example 2" in {
      val c = new JavaIssue10Cfg(
        ConfigFactory.parseString(
          """
          |main = {
          |  email = {
          |    server = "foo"
          |    password = "pw"
          |  }
          |}
          |""".stripMargin
        )
      )
      assert(c.main.email.password === "pw")
      assert(c.main.email.server === "foo")
      assert(c.main.reals === null)
    }
  }

  "(java) issue11" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue11.spec.conf")
      assert(r.classNames === Set("JavaIssue11Cfg", "Foo"))
      assert(
        r.fields.keySet === Set(
          "notify",
          "wait",
          "getClass",
          "clone",
          "finalize",
          "notifyAll",
          "toString",
          "foo"
        )
      )
    }
  }

  "(java) issue12" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue12.spec.conf")
      assert(
        r.classNames === Set(
          "JavaIssue12Cfg",
          "String",
          "Option",
          "Boolean",
          "Int_"
        )
      )
      assert(
        r.fields.keySet === Set("String", "Option", "Boolean", "int_", "bar")
      )
    }
  }

  "(java) issue13" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue13.spec.conf")
      assert(r.classNames === Set("JavaIssue13Cfg", "Issue"))
      assert(r.fields.keySet === Set("issue", "optionalFoo"))
    }
  }

  "(java) issue14" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue14.spec.conf")
      assert(r.classNames === Set("JavaIssue14Cfg", "_0"))
      assert(r.fields.keySet === Set("_0", "_1", "_2"))
    }
  }

  "(java) issue15a" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue15a.spec.conf")
      assert(r.classNames === Set("JavaIssue15aCfg"))
      assert(r.fields.keySet === Set("ii"))
    }

    "example 1" in {
      val c = new JavaIssue15aCfg(
        ConfigFactory.parseString(
          """
          |ii: [1,2 ,3 ]
        """.stripMargin
        )
      )
      assert(c.ii.asScala.toList === List(1, 2, 3))
    }

    "example 2" in {
      val c = new JavaIssue15aCfg(
        ConfigFactory.parseString(
          """
          |ii: []
        """.stripMargin
        )
      )
      assert(c.ii.asScala.toList === List.empty)
    }
  }

  "(java) issue15b" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue15b.spec.conf")
      assert(r.classNames === Set("JavaIssue15bCfg"))
      assert(
        r.fields.keySet === Set(
          "strings",
          "integers",
          "doubles",
          "longs",
          "booleans"
        )
      )
    }

    "example 1" in {
      val c = new JavaIssue15bCfg(
        ConfigFactory.parseString(
          """
          |strings:  [hello, world, true]
          |integers: [[1, 2, 3], [4, 5]]
          |doubles:  [3.14, 2.7182, 1.618]
          |longs:    [1, 9999999999]
          |booleans: [true, false]
          |""".stripMargin
        )
      )
      assert(c.strings.asScala.toList === List("hello", "world", "true"))
      assert(
        c.integers.asScala.toList.map(_.asScala.toList) === List(
          List(1, 2, 3),
          List(4, 5)
        )
      )
      assert(c.doubles.asScala.toList === List(3.14, 2.7182, 1.618))
      assert(c.longs.asScala.toList === List(1, 9999999999L))
      assert(c.booleans.asScala.toList === List(true, false))
    }
  }

  "(java) issue15c" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue15c.spec.conf")
      assert(
        r.classNames === Set(
          "JavaIssue15cCfg",
          "Qaz",
          "Aa",
          "Positions$Elm",
          "Bb$Elm",
          "Attrs$Elm"
        )
      )
      assert(
        r.fields.keySet === Set(
          "positions",
          "lat",
          "lon",
          "attrs",
          "foo",
          "qaz",
          "aa",
          "bb",
          "cc"
        )
      )
    }

    "example 1" in {
      val c = new JavaIssue15cCfg(
        ConfigFactory.parseString(
          """
          |positions: [
          |  { lat: 1, lon: 2, attrs = [ [ {foo: 99}          ] ] },
          |  { lat: 3, lon: 4, attrs = [ [ {foo: 3}, {foo: 0} ] ] }
          |]
          |qaz = {
          |  aa = { bb = [ { cc: hoho } ]  }
          |}
          |""".stripMargin
        )
      )
      assert(c.positions.size() === 2)
      assert(c.positions.get(0).lat === 1)
      assert(c.positions.get(0).lon === 2)
      assert(c.positions.get(0).attrs.size() === 1)
      assert(c.positions.get(0).attrs.get(0).size() === 1)
      assert(c.positions.get(0).attrs.get(0).get(0).foo === 99)
      assert(c.positions.get(1).lat === 3)
      assert(c.positions.get(1).lon === 4)
      assert(c.positions.get(1).attrs.size() === 1)
      assert(c.positions.get(1).attrs.get(0).size() === 2)
      assert(c.positions.get(1).attrs.get(0).get(0).foo === 3)
      assert(c.positions.get(1).attrs.get(0).get(1).foo === 0)
      assert(c.qaz.aa.bb.size() === 1)
      assert(c.qaz.aa.bb.get(0).cc === "hoho")
    }
  }

  "(java) issue15d" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue15d.spec.conf")
      assert(r.classNames === Set("JavaIssue15dCfg", "Baz$Elm"))
      assert(r.fields.keySet === Set("baz", "aa", "dd"))
    }

    "example 1" in {
      val c = new JavaIssue15dCfg(
        ConfigFactory.parseString(
          """
          |baz: [ [ {dd: 1, aa: true}, {dd: 2} ] ]
          |""".stripMargin
        )
      )
      assert(c.baz.size() === 1)
      assert(c.baz.get(0).size() === 2)
      assert(c.baz.get(0).get(0).aa === true)
      assert(c.baz.get(0).get(0).dd === 1)
      assert(c.baz.get(0).get(1).aa === null)
      assert(c.baz.get(0).get(1).dd === 2)
    }
  }

  "(java) issue15" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue15.spec.conf")
      assert(
        r.classNames === Set(
          "JavaIssue15Cfg",
          "Positions$Elm",
          "Positions$Elm2"
        )
      )
      assert(r.fields.keySet === Set("positions", "numbers", "other", "stuff"))
    }

    "example 1" in {
      val c = new JavaIssue15Cfg(
        ConfigFactory.parseString(
          """
          |positions: [
          |  {
          |    numbers: [ 1, 2, 3 ]
          |    positions: [ [ { other: 33, stuff: baz } ] ]
          |  }
          |]
          |""".stripMargin
        )
      )
      assert(c.positions.size() === 1)
      assert(c.positions.get(0).numbers.asScala.toList === List(1, 2, 3))
      assert(c.positions.get(0).positions.size() === 1)
      assert(c.positions.get(0).positions.get(0).size() === 1)
      assert(c.positions.get(0).positions.get(0).get(0).other === 33)
      assert(c.positions.get(0).positions.get(0).get(0).stuff === "baz")
    }
  }

  "(java) duration" should {
    "generate code" in {
      val r = JavaGen.generate("example/duration.spec.conf")
      assert(r.classNames === Set("JavaDurationCfg", "Durations"))
      assert(
        r.fields.keySet === Set(
          "durations",
          "days",
          "hours",
          "millis",
          "duration_ns",
          "duration_µs",
          "duration_ms",
          "duration_se",
          "duration_mi",
          "duration_hr",
          "duration_dy"
        )
      )
    }

    "example 1" in {
      val c = new JavaDurationCfg(
        ConfigFactory.parseString(
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
        )
      )
      assert(c.durations.days === 10)
      assert(c.durations.hours === 24)
      assert(c.durations.millis === 550000)
      assert(c.durations.duration_ns === 7)
      assert(c.durations.duration_µs === 7)
      assert(c.durations.duration_ms === 7)
      assert(c.durations.duration_se === 7)
      assert(c.durations.duration_mi === 7)
      assert(c.durations.duration_hr === 7)
      assert(c.durations.duration_dy === 7)
    }
  }

  "(java) duration2" should {
    "generate code" in {
      val r =
        JavaGen.generate("example/duration2.spec.conf", useDurations = true)
      assert(r.classNames === Set("JavaDuration2Cfg", "Durations"))
      assert(
        r.fields.keySet === Set(
          "durations",
          "days",
          "hours",
          "millis",
          "duration_ns",
          "duration_µs",
          "duration_ms",
          "duration_se",
          "duration_mi",
          "duration_hr",
          "duration_dy"
        )
      )
    }

    "example 1" in {
      val c = new JavaDuration2Cfg(
        ConfigFactory.parseString(
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
        )
      )
      assert(c.durations.days === Duration.ofDays(10))
      assert(c.durations.hours === Duration.ofHours(24))
      assert(c.durations.millis === Duration.ofMillis(550000))
      assert(c.durations.duration_ns === Duration.ofNanos(7))
      assert(c.durations.duration_µs === Duration.of(7, MICROS))
      assert(c.durations.duration_ms === Duration.ofMillis(7))
      assert(c.durations.duration_se === Duration.ofSeconds(7))
      assert(c.durations.duration_mi === Duration.ofMinutes(7))
      assert(c.durations.duration_hr === Duration.ofHours(7))
      assert(c.durations.duration_dy === Duration.ofDays(7))
    }
  }

  "(java) duration3" should {
    "generate code" in {
      val r = JavaGen.generate(
        "example/duration3.spec.conf",
        useDurations = true,
        genGetters = true
      )
      assert(r.classNames === Set("JavaDuration3Cfg", "Durations"))
      assert(
        r.fields.keySet === Set(
          "durations",
          "days",
          "hours",
          "millis",
          "duration_ns",
          "duration_µs",
          "duration_ms",
          "duration_se",
          "duration_mi",
          "duration_hr",
          "duration_dy"
        )
      )
    }

    "example 1" in {
      val c = new JavaDuration3Cfg(
        ConfigFactory.parseString(
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
        )
      )
      assert(c.durations.getDays === Duration.ofDays(10))
      assert(c.durations.getHours === Duration.ofHours(24))
      assert(c.durations.getMillis === Duration.ofMillis(550000))
      assert(c.durations.getDuration_ns === Duration.ofNanos(7))
      assert(c.durations.getDuration_µs === Duration.of(7, MICROS))
      assert(c.durations.getDuration_ms === Duration.ofMillis(7))
      assert(c.durations.getDuration_se === Duration.ofSeconds(7))
      assert(c.durations.getDuration_mi === Duration.ofMinutes(7))
      assert(c.durations.getDuration_hr === Duration.ofHours(7))
      assert(c.durations.getDuration_dy === Duration.ofDays(7))
    }
  }

  "(java) issue19" should {
    """put underscores for key having $""" in {
      val r = JavaGen.generate("example/issue19.spec.conf")
      assert(r.classNames === Set("JavaIssue19Cfg"))
      assert(
        r.fields === Map(
          "do_log"  -> "boolean",
          "_$_foo_" -> "java.lang.String"
        )
      )
    }

    "example" in {
      val c = new JavaIssue19Cfg(
        ConfigFactory.parseString(
          """
          |"do log" : true
          |"$_foo"  : some string
        """.stripMargin
        )
      )
      assert(c.do_log === true)
      assert(c._$_foo_ === "some string")
    }
  }

  "(java) given class name starting with $_" should {
    "generate warning" in {
      val genOpts = GenOpts("tscfg.example", "Classy", j7 = true)
      val r       = new JavaGen(genOpts).generate(ObjectType())
      assert(r.classNames === Set("Classy"))
      assert(r.fields === Map())
      // TODO actually verify generated warning
    }
  }

  "(java) keys starting with $_" should {
    val objectType = ObjectType(
      "$_baz" := STRING | "some value",
      "other" := ObjectType(
        "$_foo" := DOUBLE
      )
    )

    "generate warnings" in {
      val genOpts = GenOpts("tscfg.example", "Classy", j7 = true)
      val r       = new JavaGen(genOpts).generate(objectType)
      assert(r.classNames === Set("Classy", "Other"))
      assert(
        r.fields === Map(
          "$_baz" -> "java.lang.String",
          "other" -> "Classy.Other",
          "$_foo" -> "double"
        )
      )
      // TODO actually verify the generated warnings
    }
  }

  "(java) issue22" should {
    "generate DURATION type" in {
      val r = JavaGen.generate("example/issue22.spec.conf")
      assert(r.classNames === Set("JavaIssue22Cfg"))
      assert(
        r.fields === Map(
          "idleTimeout" -> "long"
        )
      )
    }

    "example with default value" in {
      val c = new JavaIssue22Cfg(
        ConfigFactory.parseString(
          """
          |  # empty
        """.stripMargin
        )
      )
      assert(c.idleTimeout === 75000)
    }
    "example with new value" in {
      val c = new JavaIssue22Cfg(
        ConfigFactory.parseString(
          """
          | idleTimeout = 1 hour
        """.stripMargin
        )
      )
      assert(c.idleTimeout === 3600 * 1000)
    }
  }

  "(java) issue23" should {
    "generate SIZE type" in {
      val r = JavaGen.generate("example/issue23.spec.conf")
      assert(r.classNames === Set("JavaIssue23Cfg"))
      assert(
        r.fields === Map(
          "sizeReq"    -> "long",
          "sizeOpt"    -> "java.lang.Long",
          "sizeOptDef" -> "long",
          "sizes"      -> "java.util.List<java.lang.Long>",
          "sizes2"     -> "java.util.List<java.util.List<java.lang.Long>>"
        )
      )
    }

    "example" in {
      val c = new JavaIssue23Cfg(
        ConfigFactory.parseString(
          """
          |sizeReq = "2048K"
          |sizeOpt = "1024000"
          |sizes   = [ 1000, "64G", "16kB" ]
          |sizes2  = [[ 1000, "64G" ], [ "16kB" ] ]
        """.stripMargin
        )
      )
      assert(c.sizeReq === 2048 * 1024)
      assert(c.sizeOpt === 1024000)
      assert(c.sizeOptDef === 1024)
      assert(
        c.sizes.asScala.toList === List(
          1000,
          64 * 1024 * 1024 * 1024L,
          16 * 1000
        )
      )
      assert(
        c.sizes2.asScala.toList.map(_.asScala.toList) === List(
          List(1000, 64 * 1024 * 1024 * 1024L),
          List(16 * 1000)
        )
      )
    }
  }

  "(java) issue31" should {
    "generate getters" in {
      val r = JavaGen.generate("example/issue31.spec.conf", genGetters = true)

      assert(r.classNames === Set("JavaIssue31Cfg", "B", "D"))
      assert(r.fields.size === 5)
      assert(r.fields("a") === "int")
      assert(r.fields("b") === "JavaIssue31Cfg.B")
      assert(r.fields("c") === "java.lang.String")
      assert(r.fields("d") === "B.D")
      assert(r.fields("e") === "boolean")

      assert(r.getters.size === 5)
      assert(r.getters("getA") === "int")
      assert(r.getters("getB") === "JavaIssue31Cfg.B")
      assert(r.getters("getC") === "java.lang.String")
      assert(r.getters("getD") === "B.D")
      assert(r.getters("getE") === "boolean")
    }

    "verify generated getters" in {
      val c = new JavaIssue31Cfg(
        ConfigFactory.parseString(
          """
          |a = 1
          |b.c = foo
        """.stripMargin
        )
      )
      assert(c.getA === 1)
      assert(c.getB.getC === "foo")
      assert(c.getB.getD.getE === false)
    }
  }

  "(java) issue33" should {
    "generate empty config for object level" in {
      val c = new JavaIssue33Cfg(ConfigFactory.parseString(""))
      assert(c.endpoint.url === "http://example.net")
    }

    "generate empty config for dot notated object level" in {
      val c = new JavaIssue33aCfg(ConfigFactory.parseString(""))
      assert(c.endpoint.more.url === "http://example.net")
    }

    "generate config for object first level" in {
      val c = new JavaIssue33bCfg(ConfigFactory.parseString(""))
      assert(c.endpoint.url === "http://example.net")
      assert(c.endpoint.foo === null)
      assert(c.endpoint.baz.key === "bar")
    }

    "generate config for object nested level" in {
      val c = new JavaIssue33bCfg(ConfigFactory.parseString("endpoint.foo = 1"))
      assert(c.endpoint.url === "http://example.net")
      assert(c.endpoint.foo === 1)
      assert(c.endpoint.baz.key === "bar")
    }

    "generate config for sub-object under required object" in {
      val c =
        new JavaIssue33cCfg(ConfigFactory.parseString("endpoint.req = foo"))
      assert(c.endpoint.req === "foo")
      assert(c.endpoint.optObj.key === "bar")
    }
  }

  "(java) issue40" should {
    "capture explicit memory size value in spec as a long literal" in {
      val c = new JavaIssue40Cfg(ConfigFactory.parseString(""))
      // well, the actual test is that the generated class compiles
      assert(c.memory === 53687091200L)
    }
  }

  "(java) issue41" should {
    "generate code" in {
      val r = JavaGen.generate("example/issue41.spec.conf", useOptionals = true)
      assert(r.classNames === Set("JavaIssue41Cfg", "C", "F"))
      assert(
        r.fields.keySet === Set("a", "b", "c", "d", "e", "f", "g", "h", "i")
      )
    }

    "example 1" in {
      val c = new JavaIssue41Cfg(
        ConfigFactory.parseString(
          """
          |{}
        """.stripMargin
        )
      )
      assert(c.a === Optional.empty())
      assert(c.b === 10)
      assert(c.c.d === Optional.empty())
      assert(c.c.e === "hello")
      assert(c.c.f === Optional.empty())
      assert(c.i === Optional.empty())
    }

    "example 2" in {
      val c = new JavaIssue41Cfg(
        ConfigFactory.parseString(
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
        )
      )
      assert(c.a === Optional.of(5))
      assert(c.b === 6)
      assert(c.c.d === Optional.of("c.d"))
      assert(c.c.e === "c.e")
      assert(c.c.f.get().g === 5)
      assert(c.c.f.get().h === "c.f.h")
      assert(c.i.get() === List(1.0, 2.0, 3.0).asJava)
    }
  }

  "(java) issue 49 (using issue47.spec.conf --all-required)" should {
    "fail with missing service entry" in {
      val e = intercept[com.typesafe.config.ConfigException] {
        new JavaIssue47Cfg(ConfigFactory.parseString(""))
      }
      assert(
        e.getMessage contains "'service': com.typesafe.config.ConfigException$Missing"
      )
    }
    "fail with missing url entry" in {
      val e = intercept[com.typesafe.config.ConfigException] {
        new JavaIssue47Cfg(ConfigFactory.parseString("""
            |service {
            |  # url = "http://example.net/rest"
            |  poolSize = 32
            |  debug = true
            |  doLog = false
            |  factor = 0.75
            |}""".stripMargin))
      }
      assert(
        e.getMessage contains "'service.url': com.typesafe.config.ConfigException$Missing"
      )
    }
    "fail with missing poolSize entry" in {
      val e = intercept[com.typesafe.config.ConfigException] {
        new JavaIssue47Cfg(ConfigFactory.parseString("""
            |service {
            |  url = "http://example.net/rest"
            |  # poolSize = 32
            |  debug = true
            |  doLog = false
            |  factor = 0.75
            |}""".stripMargin))
      }
      assert(
        e.getMessage contains "'service.poolSize': com.typesafe.config.ConfigException$Missing"
      )
    }
    "fail with all entries missing in service object" in {
      val e = intercept[com.typesafe.config.ConfigException] {
        new JavaIssue47Cfg(ConfigFactory.parseString("service {}"))
      }
      List("url", "poolSize", "debug", "doLog", "factor") foreach { k =>
        assert(
          e.getMessage contains s"'service.$k': com.typesafe.config.ConfigException$$Missing"
        )
      }
    }
    "fail with wrong types" in {
      val e = intercept[com.typesafe.config.ConfigException] {
        new JavaIssue47Cfg(ConfigFactory.parseString("""
            |service {
            |  url = 31  # anything can be a string, so not check on this one.
            |  poolSize = true
            |  debug = 3
            |  doLog = "str"
            |  factor = false
            |}""".stripMargin))
      }
      List("poolSize", "debug", "doLog", "factor") foreach { k =>
        assert(
          e.getMessage contains s"'service.$k': com.typesafe.config.ConfigException$$WrongType"
        )
      }
    }
    "fail with wrong type for object" in {
      val e = intercept[com.typesafe.config.ConfigException] {
        new JavaIssue47Cfg(ConfigFactory.parseString("""
            |service = 1
            |""".stripMargin))
      }
      assert(
        e.getMessage contains "'service': com.typesafe.config.ConfigException$WrongType"
      )
    }
  }

  "(java) issue 54 - shared config - example1" should {
    "be handled" in {
      val c = new JavaIssue54Cfg(ConfigFactory.parseString("""
          |example {
          |  a: {
          |    c = "C1"
          |    d = 1
          |  }
          |  b: [
          |    {
          |      c = "C2"
          |      d = 2
          |    },
          |    {
          |      c = "C3"
          |      d = 3
          |    }
          |  ]
          |}
          |
          |""".stripMargin))

      assert(c.example.a.c === "C1")
      assert(c.example.a.d === 1)
      assert(c.example.b.size() === 2)
      assert(c.example.b.get(0).c === "C2")
      assert(c.example.b.get(0).d === 2)
      assert(c.example.b.get(1).c === "C3")
      assert(c.example.b.get(1).d === 3)
    }
  }

  "(java) issue 54 - shared config - exampleD" should {
    "be handled" in {
      val c = new JavaIssue54exampleDCfg(ConfigFactory.parseString("""
          |exampleD {
          |  test {
          |    a = 1
          |  }
          |}
          |""".stripMargin))

      assert(c.exampleD.test.a === 1)
    }
  }

  "(java) issue 54 - shared config - exampleE" should {
    "be handled" in {
      val c = new JavaIssue54exampleECfg(ConfigFactory.parseString("""
          |exampleE {
          |  test {
          |    a = 1
          |  }
          |}
          |""".stripMargin))

      assert(c.exampleE.test.a === 1)
    }
  }

  "(java) issue 54 - shared config - example2" should {
    "be handled" in {
      val c = new JavaIssue54bCfg(ConfigFactory.parseString("""root {
          |  e: {
          |    b = "B1"
          |    c.d = 1
          |  }
          |  f: [
          |    {
          |      b = "B2"
          |      c.d = 2
          |    },
          |  ]
          |}
          |""".stripMargin))

      assert(c.root.e.b === "B1")
      assert(c.root.e.c.d === 1)
      assert(c.root.f.size() === 1)
      assert(c.root.f.get(0).b === "B2")
      assert(c.root.f.get(0).c.d === 2)
    }
  }

  "(java) issue 55 - valid regexes" should {
    "be properly reflected" in {
      val c = new JavaIssue55Cfg(ConfigFactory.parseString(""))
      assert(
        c.regex === ">(RUS00),(\\d{12})(.\\d{7})(.\\d{8})(\\d{3})(\\d{3}),(\\d{1,10})((\\.)(\\d{3}))?"
      )
      assert(c.regex2 === "foo bar: ([\\d]+)")

      java.util.regex.Pattern.compile(c.regex)
      java.util.regex.Pattern.compile(c.regex2)
    }
  }

  "(java) multiline strings" should {
    "be properly reflected" in {
      val c = new JavaMultilinesCfg(ConfigFactory.parseString(""))
      assert(c.a === "some\nlines")
      assert(c.b === "other\n\"quoted\"\nlines")
      assert(c.c === "'simply quoted' string")
      assert(c.d === "some \b control \t \\ chars \r\f")
    }
  }

  "(java) issue 62 - shared enumeration" when {
    "62a basic" should {
      "be handled with correct input" in {
        val c = new JavaIssue62aCfg(ConfigFactory.parseString("""foo {
            |  fruit = pineapple
            |}
            |""".stripMargin))

        import JavaIssue62aCfg.FruitType._
        assert(c.foo.fruit === pineapple)
      }

      "be handled with invalid enum value" in {
        val e = intercept[IllegalArgumentException] {
          new JavaIssue62aCfg(ConfigFactory.parseString("""foo {
              |  fruit = invalidFruit
              |}
              |""".stripMargin))
        }
        assert(e.getMessage matches "No enum constant .*FruitType.invalidFruit")
      }
    }

    "62b more complete" should {
      "be handled with correct input" in {
        val c = new JavaIssue62bCfg(ConfigFactory.parseString("""foo {
            |  fruit = pineapple
            |  someFruits = [banana, apple]
            |  other {
            |    aFruit = apple
            |  }
            |}
            |""".stripMargin))

        import JavaIssue62bCfg.FruitType._
        assert(c.foo.fruit === pineapple)
        assert(c.foo.someFruits.asScala === List(banana, apple))
        assert(c.foo.other.aFruit === apple)
      }
    }

    "62 enum used at first level " should {
      "be handled with correct input" in {
        val c = new JavaIssue62Cfg(ConfigFactory.parseString("""
            | fruit = apple
            | fruits = [banana, pineapple]
            |""".stripMargin))

        import JavaIssue62Cfg.FruitType._
        assert(c.fruit === apple)
        assert(c.fruits.asScala === List(banana, pineapple))
      }
    }
  }

  "(java) issue 64 - template with defined abstract class" should {
    val configFromFile = new JavaIssue64Cfg(
      ConfigFactory
        .parseFile(new File("src/main/tscfg/example/issue64.example.conf"))
        .resolve()
    )

    "result in a valid config for java" in {
      val r = JavaGen.generate("example/issue64.spec.conf")
      assert(
        r.classNames === Set(
          "JavaIssue64Cfg",
          "BaseModelConfig",
          "LoadModelConfig",
          "Test"
        )
      )
    }

    "be able to process a corresponding configuration correctly" in {

      // be instance of abstract super class
      configFromFile.test.loadModelConfig match {
        case _: JavaIssue64Cfg.BaseModelConfig => assert(true)
        case null                              => assert(false)
      }

      // have the correct values
      assert(
        configFromFile.test.loadModelConfig.modelBehaviour === "testBehaviour"
      )
      assert(
        configFromFile.test.loadModelConfig.uuids === List("default").asJava
      )
      assert(configFromFile.test.loadModelConfig.scaling === 1.0)
      assert(configFromFile.test.loadModelConfig.reference === "testReference")
    }
  }

  /*
  "(java) issue 64b - template with invalid case class extension" should {
    "throw an ObjectDefinitionException" in {
      JavaGen.generate("example/issue64b.spec.conf") must throwA[ObjectDefinitionException].like {
        case e: ObjectDefinitionException =>
          e.getMessage must startWith(
            "Cannot build from Config(SimpleConfigObject({\"BaseModelConfig\":{\"scaling\":\"double\",\"uuids\":" +
              "[\"string\"]},\"LoadModelConfig\":{\"modelBehaviour\":\"string\",\"reference\":\"string\"},\"test\":" +
              "{\"loadModelConfig\":\"LoadModelConfig\"}})), as linearization failed"
          )
      }
    }
  }
   */

  "(java) issue 67 - template with unintuitive order of shared objects" should {
    val configFromFile = new JavaIssue67Cfg(
      ConfigFactory
        .parseFile(new File("src/main/tscfg/example/issue67.example.conf"))
        .resolve()
    )

    "result in a valid config for scala" in {
      val r = JavaGen.generate("example/issue67.spec.conf")
      assert(
        r.classNames === Set("JavaIssue67Cfg", "AbstractA", "ImplA", "Test")
      )
    }

    "be able to process a corresponding configuration correctly" in {
      // be instance of abstract super class
      configFromFile.test.impl match {
        case _: JavaIssue67Cfg.AbstractA => assert(true)
        case null                        => assert(false)
      }

      // have the correct values
      assert(configFromFile.test.impl.a === "hello")
      assert(configFromFile.test.impl.b === "world")
    }
  }

  "(java) issue 67a - template with second inheritance level" should {
    val configFromFile = new JavaIssue67aCfg(
      ConfigFactory
        .parseFile(new File("src/main/tscfg/example/issue67a.example.conf"))
        .resolve()
    )

    "result in a valid config for scala" in {
      val r = JavaGen.generate("example/issue67a.spec.conf")
      assert(
        r.classNames === Set(
          "JavaIssue67aCfg",
          "AbstractA",
          "AbstractB",
          "ImplB",
          "Test"
        )
      )
    }

    "be able to process a corresponding configuration correctly" in {
      // be instance of abstract super class (one level above)
      configFromFile.test.impl match {
        case _: JavaIssue67aCfg.AbstractB => assert(true)
        case null                         => assert(false)
      }
      // be instance of abstract super class (second level above)
      configFromFile.test.impl match {
        case _: JavaIssue67aCfg.AbstractA => assert(true)
        case null                         => assert(false)
      }

      // have the correct values
      assert(configFromFile.test.impl.a === "hello")
      assert(configFromFile.test.impl.b === "world")
      assert(configFromFile.test.impl.c === "!")
    }
  }

  "(java) issue 67b - template with third inheritance level" should {
    val configFromFile = new JavaIssue67bCfg(
      ConfigFactory
        .parseFile(new File("src/main/tscfg/example/issue67b.example.conf"))
        .resolve()
    )

    "result in a valid config for scala" in {
      val r = JavaGen.generate("example/issue67b.spec.conf")
      assert(
        r.classNames === Set(
          "JavaIssue67bCfg",
          "AbstractA",
          "AbstractB",
          "AbstractC",
          "ImplC",
          "Test"
        )
      )
    }

    "be able to process a corresponding configuration correctly" in {
      // be instance of abstract super class (one level above)
      configFromFile.test.impl match {
        case _: JavaIssue67bCfg.AbstractB => assert(true)
        case null                         => assert(false)
      }
      // be instance of abstract super class (second level above)
      configFromFile.test.impl match {
        case _: JavaIssue67bCfg.AbstractA => assert(true)
        case null                         => assert(false)
      }
      // be instance of abstract super class (third level above)
      configFromFile.test.impl match {
        case _: JavaIssue67bCfg.AbstractC => assert(true)
        case null                         => assert(false)
      }

      // have the correct values
      assert(configFromFile.test.impl.a === "hello")
      assert(configFromFile.test.impl.b === "world")
      assert(configFromFile.test.impl.c === "!")
      assert(configFromFile.test.impl.d === "?")
    }
  }

  "(java) issue 67c - template with circular inheritance hierarchy" should {
    "be refused" in {
      val e = intercept[ObjectDefinitionException] {
        JavaGen.generate("example/issue67c.spec.conf")
      }
      assert(
        e.getMessage contains "extension of struct 'AbstractC' involves circular reference"
      )
    }
  }

  "(java) issue 71 - shared object leading to string conversion" should {
    "71a simplified handled ok" in {
      val c = new JavaIssue71aCfg(ConfigFactory.parseString("""example {
          |  a = {
          |    c = "eac"
          |    d = {
          |      e = 10
          |    }
          |  }
          |  b = [
          |    {
          |      c = "eb0c"
          |      d = {
          |        e = 20
          |      }
          |    }
          |  ]
          |}
          |""".stripMargin))

      assert(c.example.a.c === "eac")
      assert(c.example.a.d.e === 10)
      assert(c.example.b.get(0).c === "eb0c")
      assert(c.example.b.get(0).d.e === 20)
    }

    "71 handled ok" in {
      val c = new JavaIssue71Cfg(ConfigFactory.parseString("""example {
          |  a = {
          |    c = "eac"
          |    d = {
          |      e = 10
          |    }
          |  }
          |  b = [
          |    {
          |      c = "eb0c"
          |      d = {
          |        e = 20
          |      }
          |    }
          |  ]
          |  c = [
          |    {
          |      dd = "ec0dd"
          |      dddd = {
          |        eeee = 30
          |      }
          |    }
          |  ]
          |}
          |""".stripMargin))

      assert(c.example.a.c === "eac")
      assert(c.example.a.d.e === 10)
      assert(c.example.b.get(0).c === "eb0c")
      assert(c.example.b.get(0).d.e === 20)
      assert(c.example.c.get(0).dd === "ec0dd")
      assert(c.example.c.get(0).dddd.eeee === 30)
    }
  }

  "(java) issue 73 - Ability to extend or implement external type" when {
    "73a @define abstract extends java.lang.Object" should {
      "generate AbstractA implements java.lang.Object" in {
        val r = JavaGen.generate("example/issue73a.spec.conf")
        assert(r.code contains "extends java.lang.Object")
      }

      "do usual parsing" in {
        val c = new JavaIssue73aCfg(ConfigFactory.parseString("""test.impl {
            |  a = "aa"
            |  b = "bb"
            |}
            |""".stripMargin))
        assert(c.test.impl.a === "aa")
        assert(c.test.impl.b === "bb")
      }
    }

    "73b @define abstract implements java.io.Serializable" should {
      "generate AbstractA implements java.io.Serializable" in {
        val r = JavaGen.generate("example/issue73b.spec.conf")
        assert(r.code contains "implements java.io.Serializable")
      }

      "usual parsing" in {
        val c = new JavaIssue73bCfg(ConfigFactory.parseString("""test.impl {
            |  a = "aa"
            |  b = "bb"
            |}
            |""".stripMargin))
        assert(c.test.impl.a === "aa")
        assert(c.test.impl.b === "bb")
      }
    }
  }

  "(java) issue 75 - java:records" when {
    "with simple spec" should {
      "work " in {
        val c0 = ConfigFactory.parseString("simple.int = 9")
        val c  = new JavaIssue75Cfg(c0)
        assert(c.simple.foo === "simple")
        assert(c.simple.int_ === 9)
      }
    }

    "with simple @define" should {
      "work" in {
        val c0 = ConfigFactory.parseString("simple.int = 91")
        val c  = new JavaIssue75bCfg(c0)
        assert(c.simple.foo === "simple")
        assert(c.simple.int_ === 91)
      }
    }

    "with @define abstract" should {
      "generate error because record cannot be abstract" in {
        val e = intercept[ObjectDefinitionException] {
          JavaGen.generate("example/issue75c.spec.conf", genRecords = true)
        }
        assert(e.getMessage startsWith "record cannot be abstract: Simple")
      }
    }

    "with @define extends" should {
      "generate error because record cannot extend classes" in {
        val e = intercept[ObjectDefinitionException] {
          JavaGen.generate("example/issue75d.spec.conf", genRecords = true)
        }
        assert(e.getMessage startsWith "record cannot extend classes: Simple")
      }
    }

    "with @define implements" should {
      "work" in {
        val c0 = ConfigFactory.parseString("simple.int = 9")
        val c  = new JavaIssue75eCfg(c0)
        assert(c.simple.foo === "simple")
        assert(c.simple.int_ === 9)
      }
    }
  }

  "(java) issue 124b - Optional shared objects" should {
    "generate optional shared objects" in {
      val r =
        JavaGen.generate("example/issue124b.spec.conf", useOptionals = true)
      assert(r.code contains "java.util.Optional<Shared> a")
      assert(
        r.code contains "java.util.Optional<java.util.List<Shared>> b"
      )
    }

    "parse example 1 with single shared object" in {
      val c = new JavaIssue124bCfg(ConfigFactory.parseString("""example {
         |  a: {
         |    c = "C1"
         |    d = 1
         |  }
         |}
         |""".stripMargin))

      assert(c.example.a.isPresent)
      assert(c.example.a.get().c === "C1")
      assert(c.example.a.get().d === 1)

      assert(c.example.b.isEmpty)
    }

    "parse example 2 with list of shared objects" in {
      val c = new JavaIssue124bCfg(ConfigFactory.parseString("""example {
         |  b: [
         |    {
         |      c = "Apple"
         |      d = 4
         |    },
         |    {
         |      c = "Banana"
         |      d = 5
         |    }
         |  ]
         |}
         |""".stripMargin))

      assert(c.example.a.isEmpty)

      assert(c.example.b.isPresent)
      assert(c.example.b.get().size() === 2)
      assert(c.example.b.get().get(0).c === "Apple")
      assert(c.example.b.get().get(0).d === 4)
      assert(c.example.b.get().get(1).c === "Banana")
      assert(c.example.b.get().get(1).d === 5)
    }
  }

  "(java) issue 125: --java:getters" should {
    val r =
      JavaGen.generate("example/issue125.spec.conf", genGetters = true)

    "not generate getter for @define annotation" in {
      assert(r.code contains "public static class Shared")
      assert(!(r.code contains "Shared getShared() { return Shared; }"))
    }

    "get getters for members" in {
      assert(r.code contains "Example getExample() { return example; }")
      assert(r.code contains "java.lang.String getC() { return c; }")
      assert(r.code contains "int getD() { return d; }")
    }
  }

  "(java) issue 127 - @define annotation" should {
    "only generate the class, not the member" in {
      val r =
        JavaGen.generate("example/issue127.spec.conf")
      assert(r.code contains "public static class Shared")
      assert(!(r.code contains "public final JavaIssue127Cfg.Shared Shared"))
    }
  }
}
