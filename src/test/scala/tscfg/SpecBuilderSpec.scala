package tscfg

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification
import tscfg.generator.GenOpts
import tscfg.specs.ObjSpec

class SpecBuilderSpec extends Specification {

  def build(configString: String, showOutput: Boolean = true)
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
        |  bar = string
        |  baz = int
        |}
      """.stripMargin)

    "build expected spec" in {
      spec.key === Key(genOpts.className)
      spec.isOptional === false
      spec.children.keySet === Set("foo")
      val fooSpec = spec.children("foo")
      fooSpec must beAnInstanceOf[ObjSpec]
      fooSpec.asInstanceOf[ObjSpec].children.keySet === Set("bar", "baz")
    }
  }
}
