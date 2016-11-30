package tscfg

import org.specs2.mutable.Specification
import org.specs2.specification.core.Fragments
import tscfg.generators.java.javaUtil.{javaKeywords, javaIdentifier}
import scala.util.Random

object javaIdentifierSpec extends Specification {

  """javaIdentifier""" should {

    List("foo", "bar_3", "$baz").foldLeft(Fragments.empty) { (res, id) =>
      res.append(s"""keep valid identifier "$id"""" in {
        javaIdentifier(id) must_== id
      })
    }

    Random.shuffle(javaKeywords).take(3).foldLeft(Fragments.empty) { (res, kw) =>
      res.append(s"""convert java keyword "$kw" to "${kw}_"""" in {
        javaIdentifier(kw) must_== kw + "_"
      })
    }

    List("foo-bar", "foo:bar", "foo#bar").foldLeft(Fragments.empty) { (res, id) =>
      res.append(s"""replace non java id character with '_': "$id" -> "foo_bar"""" in {
        javaIdentifier(id) must_== "foo_bar"
      })
    }

    s"""prefix with '_' if first character is valid but not at first position: "21" -> "_21"""" in {
      javaIdentifier("21") must_== "_21"
    }
  }
}
