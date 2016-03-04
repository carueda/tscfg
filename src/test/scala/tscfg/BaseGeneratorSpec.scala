package tscfg

import java.io.{PrintWriter, StringWriter}

import com.typesafe.config.ConfigFactory
import org.specs2.mutable.Specification
import tscfg.generator.{GenOpts, GenResult}

abstract class BaseGeneratorSpec extends Specification {

  def generate(configString: String, showOutput: Boolean = false)
         (implicit genOpts: GenOpts): GenResult = {
    val config = ConfigFactory.parseString(configString).resolve()
    val root = nodes.createAllNodes(config)
    val sw = new StringWriter()
    val out = new PrintWriter(sw)

    val results = if (genOpts.language == "java")
      javaGenerator.generate(root, out)
    else if (genOpts.language == "scala")
      scalaGenerator.generate(root, out)
    else throw new AssertionError

    if (showOutput)
      println("Output:\n  " + sw.toString.replace("\n", "\n  "))

    results
  }
}
