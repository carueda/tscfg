package tscfg.generators.java

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification
import tscfg._
import tscfg.generators.{GenOpts, GenResult, Generator}


class JavaGeneratorSpec extends Specification {

  def generate(configString: String, showOutput: Boolean = false)
              (implicit genOpts: GenOpts): GenResult = {

    val config = ConfigFactory.parseString(configString).resolve()

    val objSpec = new SpecBuilder(Key(genOpts.className)).fromConfig(config)
    //println("\nobjSpec:\n  |" + objSpec.format().replaceAll("\n", "\n  |"))

    val generator: Generator = new JavaGenerator(genOpts)

    val results = generator.generate(objSpec)

    if (showOutput)
      println("Output:\n  " + results.code.replace("\n", "\n  "))

    results
  }

  implicit val genOpts = GenOpts(
    language = "java",
    packageName = "pkg",
    className = "CFG")

  "with good input" should {
    val result = generate(
      """
        |foo {
        |  bar = string
        |  baz = int
        |}
      """.stripMargin)

    "generate expected classes " in {
      result.classNames must contain("CFG", "Foo")
      result.classNames.size must beEqualTo(2)
    }

    "generate expected fields" in {
      result.fieldNames must contain("foo", "bar", "baz")
      result.fieldNames.size must beEqualTo(3)
    }
  }

  "with empty input" should {
    val results = generate("")
    "only generate CFG class" in {
      results.classNames must contain("CFG")
      results.classNames.size must beEqualTo(1)
      results.fieldNames must beEmpty
    }
  }

  "handle issue14" should {
    val result = generate(
      """
        |0 = {
        |  1 = bar
        |}
        |2 = foo
      """.stripMargin)

    "generate expected class " in {
      result.classNames must contain("CFG", "_0")
      result.classNames.size must beEqualTo(2)
    }

    "generate expected fields" in {
      result.fieldNames must contain("_0", "_1", "_2")
      result.fieldNames.size must beEqualTo(3)
    }
  }
}
