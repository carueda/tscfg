package tscfg

import org.scalatest.wordspec.AnyWordSpec

class templaterSpec extends AnyWordSpec {
  import tscfg.generators.{TemplateGenerator, TemplateOpts}
  import tscfg.model.ObjectType

  def build(source: String): ObjectType = {
    val result = ModelBuilder(source)
    result.objectType
  }

  "basic template generation" should {
    val templateOpts = TemplateOpts()
    val templater = new TemplateGenerator(templateOpts)

    "work" in {
      val objectType = build(
        s"""endpoint {
           |  # The path associated with the endpoint.
           |  # For example, "/home/foo/bar"
           |  #@envvar ENDPOINT_PATH
           |  path = string
           |}
           |
           |#@define enum
           |FruitType = [apple, banana, pineapple]
           |
           |fruit: FruitType
           |""".stripMargin
      )
      val template = templater.generate(objectType)
      assert(template contains "## 'endpoint': required section")
      assert(template contains "path = ?")
      assert(template contains "path = ${?ENDPOINT_PATH}")
      assert(template contains "## 'FruitType': Enumeration")
      assert(template contains "FruitType = [apple, banana, pineapple")
      assert(template contains "## 'fruit': required FruitType")
    }
  }
}
