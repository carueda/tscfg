package tscfg

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification
import tscfg.generators.GenOpts
import tscfg.specs.ObjSpec

class SpecBuilderSpec extends Specification {

  def build(configString: String, showOutput: Boolean = false)
              (implicit genOpts: GenOpts): ObjSpec = {

    val config = ConfigFactory.parseString(configString).resolve()

    val objSpec = new SpecBuilder(Key(genOpts.className)).fromConfig(config)

    if (showOutput)
      println("\nobjSpec:\n  |" + objSpec.format().replaceAll("\n", "\n  |"))

    objSpec
  }

  implicit val genOpts = GenOpts(className = "CFG")

  "with empty input" should {
    val spec = build("")
    "only build CFG spec" in {
      spec === ObjSpec(Key(genOpts.className), Map.empty)
    }
  }

  "with good input" should {
    val spec = build(
      """
        |foo {
        |  reqStr        = string
        |  reqInt        = 1
        |  reqLong       = 99999999999
        |  reqDouble     = 2.71
        |  reqBoolean    = true
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
        |  listBoolean   = [ boolean ]
        |  listDouble    = [ double ]
        |  listDuration  = [ duration ]
        |}
      """.stripMargin)

    "build expected spec" in {
      spec.key === Key(genOpts.className)
      spec.isOptional === false
      spec.children.keySet === Set("foo")
      val fooSpec = spec.children("foo")
      fooSpec must beAnInstanceOf[ObjSpec]
      fooSpec.asInstanceOf[ObjSpec].children.keySet === Set(
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
        "listDuration"
      )
    }
  }
}
