package tscfg.schema.parsing

import org.scalatest.Inside.inside

class LxTests extends CommonForTests {
  "basic tests" should {
    "succeed with letters" in {
      val source = "foo"
      val res    = parseAll(lx.letters, source)
      inside(res) { case Right(s) if s == source => }
    }
  }

  "parse identifier" should {
    List("x", "ab", "_ab", "foo_123_baz") foreach { source =>
      s"succeed with: `$source`" in {
        val res = parseAll(lx.identifier, source)
        inside(res) { case Right(t) if t == source => }
      }
    }

    List("define", "enum") foreach { keyword =>
      s"fail with keyword: `$keyword`" in {
        val res = parseAll(lx.identifier, keyword)
        inside(res) { case Left(p: ParseError) =>
          // pprint.pprintln(p)
          assert(p.msg contains s"unexpected: $keyword")
        }
      }
    }
  }

}
