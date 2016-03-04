package tscfg

import tscfg.generator.GenOpts

class templateGeneratorSpec extends BaseGeneratorSpec {

  def opts(what: templateGenerator.What) = GenOpts(templateWhat = Some(what))

  "genAll with good input" should {
    implicit val genOpts = opts(templateGenerator.genAll)
    val result = generate(
      """
        |foo {
        |  bar = "string?"
        |  baz = "int | 1"
        |}
      """.stripMargin)

    "generate expected fields" in {
      result.fieldNames must contain("foo", "bar", "baz")
      result.fieldNames.size must beEqualTo(3)
    }
  }

  "genAll with empty input" should {
    val results = generate("")(opts(templateGenerator.genAll))
    "no symbols generated" in {
      results.fieldNames must beEmpty
    }
  }

  "genBase with good input" should {
    implicit val genOpts = opts(templateGenerator.genBase)
    val result = generate(
      """
        |foo {
        |  bar = "string?"
        |  baz = "int | 1"
        |}
      """.stripMargin, true)

    "generate expected fields" in {
      result.fieldNames must contain("foo", "baz")
      result.fieldNames.size must beEqualTo(2)
    }
  }

  "genLocal with good input" should {
    implicit val genOpts = opts(templateGenerator.genLocal)
    val result = generate(
      """
        |foo {
        |  bar = "string?"
        |  baz = "int | 1"
        |}
      """.stripMargin, true)

    "generate expected fields" in {
      result.fieldNames must contain("foo", "bar")
      result.fieldNames.size must beEqualTo(2)
    }
  }
}
