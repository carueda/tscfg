package tscfg

import org.specs2.mutable.Specification
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

  def verify(objType: ObjectType,
             memberName: String,
             t: Type,
             optional: Boolean = false,
             default: Option[String] = None,
             qualification: Option[String] = None,
             comments: Option[String] = None
            ) = {
    val at = objType.members(memberName)
    at.t === t
    at.isOptional === optional
    at.default === default
    at.qualification === qualification
    at.comments === comments
  }

  "with empty input" should {
    val spec = build("")
    "build empty ObjectType" in {
      spec === ObjectType()
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
        |  listDuration_se  = [ "duration : s" ]
        |}
      """.stripMargin, showOutput = true)

    "build expected objType" in {
      objType.members.keySet === Set("foo")
      val foo = objType.members("foo")
      foo.optional === false
      foo.default must beNone
      foo.qualification must beNone
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
      verify(fooObj, "reqDuration", DURATION)
      verify(fooObj, "optStr" ,     STRING,   optional = true)
      verify(fooObj, "optInt" ,     INTEGER,  optional = true)
      verify(fooObj, "optLong",     LONG,     optional = true)
      verify(fooObj, "optDouble",   DOUBLE,   optional = true)
      verify(fooObj, "optBoolean",  BOOLEAN,  optional = true)
      verify(fooObj, "optDuration", DURATION, optional = true)
      verify(fooObj, "dflStr" ,     STRING,   optional = true, default = Some("hi"))
      verify(fooObj, "dflInt" ,     INTEGER,  optional = true, default = Some("3"))
      verify(fooObj, "dflLong",     LONG,     optional = true, default = Some("999999999"))
      verify(fooObj, "dflDouble",   DOUBLE,   optional = true, default = Some("3.14"))
      verify(fooObj, "dflBoolean",  BOOLEAN,  optional = true, default = Some("false"))
      verify(fooObj, "dflDuration", DURATION, optional = true, default = Some("21d"))
      verify(fooObj, "listStr",      ListType(STRING))
      verify(fooObj, "listInt",      ListType(INTEGER))
      verify(fooObj, "listLong",     ListType(LONG))
      verify(fooObj, "listDouble",   ListType(DOUBLE))
      verify(fooObj, "listBoolean",  ListType(BOOLEAN))
      verify(fooObj, "listDuration", ListType(DURATION))
      verify(fooObj, "listDuration_se", ListType(DURATION))
    }
  }
}
