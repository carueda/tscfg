package tscfg

import org.specs2.mutable.Specification
import model.durations._
import tscfg.buildWarnings.{DefaultListElemWarning, MultElemListWarning, OptListElemWarning}
import tscfg.model._


class ModelBuilderSpec extends Specification {

  def build(source: String, showOutput: Boolean = false): ModelBuildResult = {

    if (showOutput)
      println("\nsource:\n  |" + source.replaceAll("\n", "\n  |"))

    val result = ModelBuilder(source)
    val objectType = result.objectType

    if (showOutput) {
      println("\nobjectType: " + objectType)
      println("\nobjectType:\n  |" + model.util.format(objectType).replaceAll("\n", "\n  |"))
    }

    result
  }

  private def verify(objType: ObjectType,
                     memberName: String,
                     t: Type,
                     optional: Boolean = false,
                     default: Option[String] = None,
                     comments: Option[String] = None
                    ) = {
    val at = objType.members(memberName)
    at.t === t
    at.optional === optional
    at.default === default
    at.comments === comments
  }

  "with empty input" should {
    val result = build("")
    "build empty ObjectType" in {
      result.objectType === ObjectType()
    }
  }

  "with empty list" should {
    def a = build(
      """
        |my_list: [ ]
      """.stripMargin)

    "throw IllegalArgumentException" in {
      a must throwA[IllegalArgumentException]
    }
  }

  "with list with multiple elements" should {
    val result = build("my_list: [ true, false ]")

    "generate warning" in {
      val warns = result.warnings.filter(_.isInstanceOf[MultElemListWarning])
      warns.map(_.source) must contain("[true,false]")
    }
  }

  "with list element indicating optional" should {
    val result = build("""my_list: [ "string?" ]""")

    "generate warning" in {
      val warns = result.warnings.filter(_.isInstanceOf[OptListElemWarning])
      warns.map(_.source) must contain("string?")
    }
  }

  "with list element indicating a default value" should {
    val result = build(
      """
        |my_list: [ "double | 3.14" ]
      """.stripMargin)

    "generate warning" in {
      val warns = result.warnings.filter(_.isInstanceOf[DefaultListElemWarning]).
        asInstanceOf[List[DefaultListElemWarning]]
      warns.map(_.default) must contain("3.14")
    }
  }

  "with list with literal int" should {
    val result = build(
      """
        |my_list: [ 99999999 ]
      """.stripMargin)

    "translate into ListType(INTEGER)" in {
      result.objectType.members("my_list").t === ListType(INTEGER)
    }
  }

  "with list with literal long" should {
    val result = build(
      """
        |my_list: [ 99999999999 ]
      """.stripMargin)

    "translate into ListType(LONG)" in {
      result.objectType.members("my_list").t === ListType(LONG)
    }
  }

  "with list with literal double" should {
    val result = build(
      """
        |my_list: [ 3.14 ]
      """.stripMargin)

    "translate into ListType(DOUBLE)" in {
      result.objectType.members("my_list").t === ListType(DOUBLE)
    }
  }

  "with list with literal boolean" should {
    val result = build(
      """
        |my_list: [ false ]
      """.stripMargin)

    "translate into ListType(BOOLEAN)" in {
      result.objectType.members("my_list").t === ListType(BOOLEAN)
    }
  }

  "with literal integer" should {
    val result = build(
      """
        |optInt: 21
      """.stripMargin)

    "translate into ListType(BOOLEAN)" in {
      val at = result.objectType.members("optInt")
      at.t === INTEGER
      at.optional must beTrue
      at.default must beSome("21")
    }
  }

  // TODO
  "with enum" should {
    val result = build(
      """#@define enum
        |FruitType = [apple, banana, pineapple]
      """.stripMargin)

    "translate member into EnumObjectType" in {
      val at = result.objectType.members("FruitType")
      // println(s"FruitType = $at")
      true
    }
  }

  "with literal duration (issue 22)" should {
    val result = build(
      """
        |idleTimeout = 75 seconds
      """.stripMargin)

    "translate into DURATION(ms) with given default" in {
      val at = result.objectType.members("idleTimeout")
      at.t === DURATION(ms)
      at.optional must beTrue
      at.default must beSome("75 seconds")
    }
  }

  "with good input" should {
    val result = build(
      """
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
      objType.members.keySet === Set("foo")
      val foo = objType.members("foo")
      foo.optional === false
      foo.default must beNone
      foo.comments must beNone
      foo.t must beAnInstanceOf[ObjectType]
      val fooObj = foo.t.asInstanceOf[ObjectType]
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
      verify(fooObj, "reqStr",      STRING)
      verify(fooObj, "reqInt",      INTEGER)
      verify(fooObj, "reqLong",     LONG)
      verify(fooObj, "reqDouble",   DOUBLE)
      verify(fooObj, "reqBoolean",  BOOLEAN)
      verify(fooObj, "reqDuration", DURATION(ms))
      verify(fooObj, "duration_ns", DURATION(ns))
      verify(fooObj, "duration_µs", DURATION(us))
      verify(fooObj, "duration_ms", DURATION(ms))
      verify(fooObj, "duration_se", DURATION(second))
      verify(fooObj, "duration_mi", DURATION(minute))
      verify(fooObj, "duration_hr", DURATION(hour))
      verify(fooObj, "duration_dy", DURATION(day ))
      verify(fooObj, "optStr" ,     STRING,   optional = true)
      verify(fooObj, "optInt" ,     INTEGER,  optional = true)
      verify(fooObj, "optLong",     LONG,     optional = true)
      verify(fooObj, "optDouble",   DOUBLE,   optional = true)
      verify(fooObj, "optBoolean",  BOOLEAN,  optional = true)
      verify(fooObj, "optDuration", DURATION(ms), optional = true)
      verify(fooObj, "dflStr" ,     STRING,   optional = true, default = Some("hi"))
      verify(fooObj, "dflInt" ,     INTEGER,  optional = true, default = Some("3"))
      verify(fooObj, "dflLong",     LONG,     optional = true, default = Some("999999999"))
      verify(fooObj, "dflDouble",   DOUBLE,   optional = true, default = Some("3.14"))
      verify(fooObj, "dflBoolean",  BOOLEAN,  optional = true, default = Some("false"))
      verify(fooObj, "dflDuration", DURATION(ms), optional = true, default = Some("21d"))
      verify(fooObj, "listStr",      ListType(STRING))
      verify(fooObj, "listInt",      ListType(INTEGER))
      verify(fooObj, "listLong",     ListType(LONG))
      verify(fooObj, "listDouble",   ListType(DOUBLE))
      verify(fooObj, "listBoolean",  ListType(BOOLEAN))
      verify(fooObj, "listDuration", ListType(DURATION(ms)))
      verify(fooObj, "listDuration_se", ListType(DURATION(second)))
    }
  }
}
