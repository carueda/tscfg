package tscfg.generators.java

import org.specs2.mutable.Specification
import tscfg.generators.{GenOpts, GenResult}
import tscfg.specs.types.{INTEGER, STRING}
import tscfg.specs.{AtomicSpec, ObjSpec}

class JavaGeneratorSpec extends Specification {

  private val genOpts = GenOpts(
    packageName = "pkg",
    className = "CFG")

  private val showOutput = false

  private def generate(objSpec: ObjSpec): GenResult = {
    val results = new JavaGenerator(genOpts).generate(objSpec)
    if (showOutput) println("Output:\n  " + results.code.replace("\n", "\n  "))
    results
  }

  "with direct ObjSpec" should {
    val objSpec = ObjSpec("foo",
      "bar" → AtomicSpec(STRING),
      "baz" → AtomicSpec(INTEGER)
    )
    val result = generate(objSpec)
    //println(result.code)

    "generate expected classes " in {
      result.classNames === Set("Foo")
      result.fieldNames === Set("bar", "baz")
    }
  }

  "with direct empty ObjSpec" should {
    val objSpec = ObjSpec("foo")
    val result = generate(objSpec)
    //println(result.code)

    "generate expected classes " in {
      result.classNames === Set("Foo")
      result.fieldNames === Set.empty
    }
  }
}
