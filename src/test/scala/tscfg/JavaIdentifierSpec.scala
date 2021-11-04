package tscfg

import org.scalatest.wordspec.AnyWordSpec
import tscfg.generators.java.javaUtil.{javaIdentifier, javaKeywords}

import scala.util.Random

class JavaIdentifierSpec extends AnyWordSpec {

  """javaIdentifier""" should {

    List("foo", "bar_3", "$baz") foreach { id =>
      s"""keep valid identifier "$id"""" in {
        assert(javaIdentifier(id) === id)
      }
    }

    Random.shuffle(javaKeywords).take(3) foreach { kw =>
      s"""convert java keyword "$kw" to "${kw}_"""" in {
        assert(javaIdentifier(kw) === kw + "_")
      }
    }

    List("foo-bar", "foo:bar", "foo#bar") foreach { id =>
      s"""replace non java id character with '_': "$id" -> "foo_bar"""" in {
        assert(javaIdentifier(id) === "foo_bar")
      }
    }

    s"""prefix with '_' if first character is valid but not at first position: "21" -> "_21"""" in {
      assert(javaIdentifier("21") === "_21")
    }
  }
}
