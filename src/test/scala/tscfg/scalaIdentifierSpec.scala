package tscfg

import org.specs2.mutable.Specification
import org.specs2.specification.core.Fragments
import tscfg.generators.scala.ScalaUtil
import tscfg.generators.scala.ScalaUtil.scalaReservedWords

import scala.util.Random

object scalaIdentifierSpec extends Specification {

  """scalaIdentifier""" should {
    val scalaUtil: ScalaUtil = new ScalaUtil()
    import scalaUtil.scalaIdentifier


    List("foo", "bar_3", "$baz").foldLeft(Fragments.empty) { (res, id) =>
      res.append(s"""keep valid identifier "$id"""" in {
        scalaIdentifier(id) must_== id
      })
    }

    Random.shuffle(scalaReservedWords).take(3).foldLeft(Fragments.empty) { (res, w) =>
      val e = "`" +w + "`"
      res.append(s"""convert scala reserved word "$w" to "$e"""" in {
        scalaIdentifier(w) must_== e
      })
    }

    List("foo-bar", "foo:bar", "foo#bar").foldLeft(Fragments.empty) { (res, id) =>
      res.append(s"""replace non scala id character with '_': "$id" -> "foo_bar"""" in {
        scalaIdentifier(id) must_== "foo_bar"
      })
    }

    s"""prefix with '_' if first character is valid but not at first position: "21" -> "_21"""" in {
      scalaIdentifier("21") must_== "_21"
    }
  }

  """scalaIdentifier with useBackticks=true""" should {
    val scalaUtil: ScalaUtil = new ScalaUtil(useBackticks = true)
    import scalaUtil.scalaIdentifier

    List("foo-bar", "foo:bar", "foo#bar").foldLeft(Fragments.empty) { (res, id) =>
      res.append(s"""put non scala id with backticks: "$id" -> "`$id`"""" in {
        scalaIdentifier(id) must_== s"`$id`"
      })
    }

    List("0", "1", "3").foldLeft(Fragments.empty) { (res, id) =>
      res.append(s"""put literal number with backticks: "$id" -> "`$id`"""" in {
        scalaIdentifier(id) must_== s"`$id`"
      })
    }
  }
}
