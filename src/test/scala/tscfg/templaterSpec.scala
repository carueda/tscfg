package tscfg

import org.specs2.mutable.Specification

class templaterSpec extends Specification {
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
      template must contain("## 'endpoint': required section")
      template must contain("path = ?")
      template must contain("path = ${?ENDPOINT_PATH}")

      template must contain("## 'FruitType': Enumeration")
      template must contain("FruitType = [apple, banana, pineapple")
      template must contain("## 'fruit': required FruitType")
    }
  }
}
