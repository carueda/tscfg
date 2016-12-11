package tscfg

import org.specs2.mutable.Specification
import model.durations._
import tscfg.model._


class ModelBuilderSpec extends Specification {

  def build(source: String, showOutput: Boolean = false): ObjectType = {

    if (showOutput)
      println("\nsource:\n  |" + source.replaceAll("\n", "\n  |"))

    val objSpec = ModelBuilder(source)

    if (showOutput)
      println("\nobjSpec:\n  |" + model.util.format(objSpec).replaceAll("\n", "\n  |"))

    objSpec
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
    at.isOptional === optional
    at.default === default
    at.comments === comments
  }

  "with empty input" should {
    val spec = build("")
    "build empty ObjectType" in {
      spec === ObjectType()
    }
  }

  "with empty list" should {
    def a = build(
      """
        |list: [ ]
      """.stripMargin)

    "throw IllegalArgumentException" in {
      a must throwA[IllegalArgumentException]
    }
  }

  "with list with multiple elements" should {
    build(
      """
        |list: [ true, false ]
      """.stripMargin)

    "generate warning" in {
      1===1 // not actually verified here
    }
  }

  "with list element indicating optional" should {
    build(
      """
        |list: [ "string?" ]
      """.stripMargin)

    "generate warning" in {
      1===1 // not actually verified here
    }
  }

  "with list element indicating a default value" should {
    build(
      """
        |list: [ "double | 3.14" ]
      """.stripMargin)

    "generate warning" in {
      1===1 // not actually verified here
    }
  }

  "with list with literal int" should {
    val ot = build(
      """
        |list: [ 99999999 ]
      """.stripMargin)

    "translate into ListType(INTEGER)" in {
      ot.members("list").t === ListType(INTEGER)
    }
  }

  "with list with literal long" should {
    val ot = build(
      """
        |list: [ 99999999999 ]
      """.stripMargin)

    "translate into ListType(LONG)" in {
      ot.members("list").t === ListType(LONG)
    }
  }

  "with list with literal double" should {
    val ot = build(
      """
        |list: [ 3.14 ]
      """.stripMargin)

    "translate into ListType(DOUBLE)" in {
      ot.members("list").t === ListType(DOUBLE)
    }
  }

  "with list with literal boolean" should {
    val ot = build(
      """
        |list: [ false ]
      """.stripMargin)

    "translate into ListType(BOOLEAN)" in {
      ot.members("list").t === ListType(BOOLEAN)
    }
  }

  "with good input" should {
    val objType = build(
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
