package tscfg

import tscfg.generator.GenOpts


class javaGeneratorSpec extends BaseGeneratorSpec {

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
