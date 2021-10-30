package tscfg

import org.scalatest.wordspec.AnyWordSpec
import tscfg.generators.scala.ScalaUtil
import tscfg.generators.scala.ScalaUtil.scalaReservedWords

import scala.util.Random

class scalaIdentifierSpec extends AnyWordSpec {

  """scalaIdentifier""" should {
    val scalaUtil: ScalaUtil = new ScalaUtil()
    import scalaUtil.scalaIdentifier


    List("foo", "bar_3", "$baz") foreach { id =>
      s"""keep valid identifier "$id"""" in {
        scalaIdentifier(id) === id
      }
    }

    Random.shuffle(scalaReservedWords).take(3) foreach { w =>
      val e = "`" +w + "`"
      s"""convert scala reserved word "$w" to "$e"""" in {
        scalaIdentifier(w) === e
      }
    }

    List("foo-bar", "foo:bar", "foo#bar") foreach { id =>
      s"""replace non scala id character with '_': "$id" -> "foo_bar"""" in {
        scalaIdentifier(id) === "foo_bar"
      }
    }

    s"""prefix with '_' if first character is valid but not at first position: "21" -> "_21"""" in {
      scalaIdentifier("21") === "_21"
    }
  }

  """scalaIdentifier with useBackticks=true""" should {
    val scalaUtil: ScalaUtil = new ScalaUtil(useBackticks = true)
    import scalaUtil.scalaIdentifier

    List("foo-bar", "foo:bar", "foo#bar") foreach { id =>
      s"""put non scala id with backticks: "$id" -> "`$id`"""" in {
        scalaIdentifier(id) === s"`$id`"
      }
    }

    List("0", "1", "3") foreach { id =>
      s"""put literal number with backticks: "$id" -> "`$id`"""" in {
        scalaIdentifier(id) === s"`$id`"
      }
    }
  }
}
