package tscfg

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import tscfg.buildWarnings.{
  DefaultListElemWarning,
  MultElemListWarning,
  OptListElemWarning
}
import tscfg.exceptions.ObjectDefinitionException
import tscfg.model._
import tscfg.model.durations._
import tscfg.ns.NamespaceMan

class ModelBuilderSpec extends AnyWordSpec {

  def build(source: String, showOutput: Boolean = false): ModelBuildResult = {

    if (showOutput)
      println("\nsource:\n  |" + source.replaceAll("\n", "\n  |"))

    val result     = ModelBuilder(new NamespaceMan, source)
    val objectType = result.objectType

    if (showOutput) {
      println("\nobjectType: " + objectType)
      println(
        "\nobjectType:\n  |" + model.util
          .format(objectType)
          .replaceAll("\n", "\n  |")
      )
    }

    result
  }

  private def verify(
      objType: ObjectType,
      memberName: String,
      t: Type,
      optional: Boolean = false,
      default: Option[String] = None,
      comments: Option[String] = None
  ): Unit = {
    val at = objType.members(memberName)
    assert(at.t === t)
    assert(at.optional === optional)
    assert(at.default === default)
    assert(at.comments === comments)
  }

  "with empty input" should {
    val result = build("")
    "build empty ObjectType" in {
      assert(result.objectType === ObjectType())
    }
  }

  "with empty list" should {
    "throw" in {
      assertThrows[IllegalArgumentException] {
        build("""
            |my_list: [ ]
          """.stripMargin)
      }
    }
  }

  "with list with multiple elements" should {
    val result = build("my_list: [ true, false ]")

    "generate warning" in {
      val warns = result.warnings.filter(_.isInstanceOf[MultElemListWarning])
      assert(warns.map(_.source).contains("[true,false]"))
    }
  }

  "with list element indicating optional" should {
    val result = build("""my_list: [ "string?" ]""")

    "generate warning" in {
      val warns = result.warnings.filter(_.isInstanceOf[OptListElemWarning])
      assert(warns.map(_.source).contains("string?"))
    }
  }

  "with list element indicating a default value" should {
    val result = build("""
        |my_list: [ "double | 3.14" ]
      """.stripMargin)

    "generate warning" in {
      val warns = result.warnings
        .filter(_.isInstanceOf[DefaultListElemWarning])
        .asInstanceOf[List[DefaultListElemWarning]]
      assert(warns.map(_.default).contains("3.14"))
    }
  }

  "with list with literal int" should {
    val result = build("""
        |my_list: [ 99999999 ]
      """.stripMargin)

    "translate into ListType(INTEGER)" in {
      assert(result.objectType.members("my_list").t === ListType(INTEGER))
    }
  }

  "with list with literal long" should {
    val result = build("""
        |my_list: [ 99999999999 ]
      """.stripMargin)

    "translate into ListType(LONG)" in {
      assert(result.objectType.members("my_list").t === ListType(LONG))
    }
  }

  "with list with literal double" should {
    val result = build("""
        |my_list: [ 3.14 ]
      """.stripMargin)

    "translate into ListType(DOUBLE)" in {
      assert(result.objectType.members("my_list").t === ListType(DOUBLE))
    }
  }

  "with list with literal boolean" should {
    val result = build("""
        |my_list: [ false ]
      """.stripMargin)

    "translate into ListType(BOOLEAN)" in {
      assert(result.objectType.members("my_list").t === ListType(BOOLEAN))
    }
  }

  "with literal integer" should {
    val result = build("""
        |optInt: 21
      """.stripMargin)

    "translate into ListType(BOOLEAN)" in {
      val at = result.objectType.members("optInt")
      assert(at.t === INTEGER)
      assert(at.optional)
      assert(at.default.contains("21"))
    }
  }

  "with literal duration (issue 22)" should {
    val result = build("""
        |idleTimeout = 75 seconds
      """.stripMargin)

    "translate into DURATION(ms) with given default" in {
      val at = result.objectType.members("idleTimeout")
      assert(at.t === DURATION(ms))
      assert(at.optional)
      assert(at.default.contains("75 seconds"))
    }
  }

  "with good input" should {
    val result = build("""
        |foo {
        |  reqStr        = string
        |  reqInt        = integer
        |  reqLong       = long
        |  reqDouble     = double
        |  reqBoolean    = boolean
        |  reqDuration   = duration
        |  duration_ns   = "duration : ns"
        |  duration_µs   = "duration : us"
        |  duration_ms   = "duration : ms"
        |  duration_se   = "duration : s"
        |  duration_mi   = "duration : m"
        |  duration_hr   = "duration : h"
        |  duration_dy   = "duration : d"
        |  optStr        = "string?"
        |  optInt        = "int?"
        |  optLong       = "long?"
        |  optDouble     = "double?"
        |  optBoolean    = "boolean?"
        |  optDuration   = "duration?"
        |  dflStr        = "string   | hi"
        |  dflInt        = "int      | 3"
        |  dflLong       = "long     | 999999999"
        |  dflDouble     = "double   | 3.14"
        |  dflBoolean    = "boolean  | false"
        |  dflDuration   = "duration | 21d"
        |  listStr       = [ string ]
        |  listInt       = [ integer ]
        |  listLong      = [ long ]
        |  listDouble    = [ double ]
        |  listBoolean   = [ boolean ]
        |  listDuration  = [ duration ]
        |  listDuration_se  = [ "duration : second" ]
        |}
      """.stripMargin)

    val objType = result.objectType

    "build expected objType" in {
      assert(objType.members.keySet === Set("foo"))
      val foo = objType.members("foo")
      assert(foo.optional === false)
      assert(foo.default.isEmpty)
      assert(foo.comments.isEmpty)
      assert(foo.t.isInstanceOf[ObjectType])
      val fooObj = foo.t.asInstanceOf[ObjectType]
      assert(
        fooObj.members.keySet === Set(
          "reqStr",
          "reqInt",
          "reqLong",
          "reqDouble",
          "reqBoolean",
          "reqDuration",
          "duration_ns",
          "duration_µs",
          "duration_ms",
          "duration_se",
          "duration_mi",
          "duration_hr",
          "duration_dy",
          "optStr",
          "optInt",
          "optLong",
          "optDouble",
          "optBoolean",
          "optDuration",
          "dflStr",
          "dflInt",
          "dflLong",
          "dflDouble",
          "dflBoolean",
          "dflDuration",
          "listStr",
          "listInt",
          "listLong",
          "listBoolean",
          "listDouble",
          "listDuration",
          "listDuration_se"
        )
      )
      verify(fooObj, "reqStr", STRING)
      verify(fooObj, "reqInt", INTEGER)
      verify(fooObj, "reqLong", LONG)
      verify(fooObj, "reqDouble", DOUBLE)
      verify(fooObj, "reqBoolean", BOOLEAN)
      verify(fooObj, "reqDuration", DURATION(ms))
      verify(fooObj, "duration_ns", DURATION(ns))
      verify(fooObj, "duration_µs", DURATION(us))
      verify(fooObj, "duration_ms", DURATION(ms))
      verify(fooObj, "duration_se", DURATION(second))
      verify(fooObj, "duration_mi", DURATION(minute))
      verify(fooObj, "duration_hr", DURATION(hour))
      verify(fooObj, "duration_dy", DURATION(day))
      verify(fooObj, "optStr", STRING, optional = true)
      verify(fooObj, "optInt", INTEGER, optional = true)
      verify(fooObj, "optLong", LONG, optional = true)
      verify(fooObj, "optDouble", DOUBLE, optional = true)
      verify(fooObj, "optBoolean", BOOLEAN, optional = true)
      verify(fooObj, "optDuration", DURATION(ms), optional = true)
      verify(fooObj, "dflStr", STRING, optional = true, default = Some("hi"))
      verify(fooObj, "dflInt", INTEGER, optional = true, default = Some("3"))
      verify(
        fooObj,
        "dflLong",
        LONG,
        optional = true,
        default = Some("999999999")
      )
      verify(
        fooObj,
        "dflDouble",
        DOUBLE,
        optional = true,
        default = Some("3.14")
      )
      verify(
        fooObj,
        "dflBoolean",
        BOOLEAN,
        optional = true,
        default = Some("false")
      )
      verify(
        fooObj,
        "dflDuration",
        DURATION(ms),
        optional = true,
        default = Some("21d")
      )
      verify(fooObj, "listStr", ListType(STRING))
      verify(fooObj, "listInt", ListType(INTEGER))
      verify(fooObj, "listLong", ListType(LONG))
      verify(fooObj, "listDouble", ListType(DOUBLE))
      verify(fooObj, "listBoolean", ListType(BOOLEAN))
      verify(fooObj, "listDuration", ListType(DURATION(ms)))
      verify(fooObj, "listDuration_se", ListType(DURATION(second)))
    }
  }

  "invalid @defines" should {
    "check Missing name after `extends`" in {
      val e = intercept[ObjectDefinitionException] {
        build("""#@define extends
            |foo {x:int}
            |""".stripMargin)
      }
      assert(e.getMessage.contains("Missing name after `extends`"))
    }

    "check Unrecognized @define construct" in {
      val e = intercept[ObjectDefinitionException] {
        build("""#@define dummy
            |foo {x:int}
            |""".stripMargin)
      }
      assert(e.getMessage.contains("Unrecognized @define construct"))
    }
  }

  "#180 Wrapped Data Types" should {
    val source = """
        |#@define
        |TypeA {
        |    fizz: String
        |    buzz: String
        |}
        |
        |#@define
        |TypeB {
        |    foo: TypeA
        |    bar: TypeA
        |}
        |
        |cfg {
        |    typeB: TypeB
        |    additionalParam: String
        |}
        |""".stripMargin

    val result = build(source)
    // pprint.pprintln(result.objectType)

    "capture expected TypeA model" in {
      val TypeA = result.objectType.members("TypeA")
      TypeA.t shouldBe a[ObjectType]
      val ot = TypeA.t.asInstanceOf[ObjectType]
      ot.members.keySet shouldBe Set("fizz", "buzz")
      ot.members("fizz").t shouldBe STRING
    }

    "capture expected TypeB model" in {
      val TypeB = result.objectType.members("TypeB")
      TypeB.t shouldBe a[ObjectType]
      val TypeB_ot = TypeB.t.asInstanceOf[ObjectType]
      TypeB_ot.members.keySet shouldBe Set("foo", "bar")
      val foo = TypeB_ot.members("foo")
      // pprint.pprintln(foo)
      foo.t shouldBe a[ObjectRefType]
      val foo_ort = foo.t.asInstanceOf[ObjectRefType]
      foo_ort.simpleName shouldBe "TypeA"
    }

    "capture expected cfg model" in {
      val cfg = result.objectType.members("cfg")
      cfg.t shouldBe a[ObjectType]
      val cfg_ot = cfg.t.asInstanceOf[ObjectType]
      cfg_ot.members.keySet shouldBe Set("typeB", "additionalParam")
      val typeB = cfg_ot.members("typeB")
      typeB.t shouldBe a[ObjectRefType]
      val typeB_ort = typeB.t.asInstanceOf[ObjectRefType]
      typeB_ort.simpleName shouldBe "TypeB"
    }
  }
}
