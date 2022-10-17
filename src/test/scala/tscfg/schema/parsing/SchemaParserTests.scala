package tscfg.schema.parsing

import cats.data.NonEmptyList
import org.scalatest.Inside.inside
import tscfg.schema.ast._

class SchemaParserTests extends CommonForTests {
  "target language spec" should {
    List("scala", "java") foreach { lang =>
      val code = TargetLanguageCode.valueOf(lang.toUpperCase)
      s"succeed with lang: `$lang`" in {
        val source = s"target $lang"
        val res    = parseAll(SchemaParser.targetLanguage, source)
        inside(res) { case Right(tl: TargetLanguage) =>
          // pprint.pprintln(tl)
          assert(tl.code === code)
        }
      }
    }
  }

  "basic member" should {
    "parse with int" in {
      val source = "foo: int"
      val res    = parseAll(SchemaParser.member, source)
      inside(res) { case Right(m: Member) =>
        assert(m.name.lexeme === "foo")
        assert(m.typeTerm.type_ === BasicType.INT)
      }
    }
    "parse with string" in {
      val source = "foo: string"
      val res    = parseAll(SchemaParser.member, source)
      inside(res) { case Right(m: Member) =>
        assert(m.name.lexeme === "foo")
        assert(m.typeTerm.type_ === BasicType.STRING)
      }
    }
  }

  "basic member with name" should {
    "parse with some ident" in {
      val source = "foo: someIdent"
      val res    = parseAll(SchemaParser.member, source)
      inside(res) { case Right(m: Member) =>
        assert(m.name.lexeme === "foo")
        assert(m.typeTerm.type_ === TypeName("someIdent"))
      }
    }
  }

  "member with struct" should {
    "parse with simple struct" in {
      val source =
        """foo: {
          |  bar: int
          |  baz: long
          |}""".stripMargin
      val res = parseAll(SchemaParser.member, source)
      inside(res) { case Right(m: Member) =>
        assert(m.name.lexeme === "foo")
        inside(m.typeTerm.type_) {
          case TypeStruct(NonEmptyList(m0, m1 :: Nil)) =>
            assert(m0.name.lexeme === "bar")
            assert(m0.typeTerm.type_ === BasicType.INT)
            assert(m1.name.lexeme === "baz")
            assert(m1.typeTerm.type_ === BasicType.LONG)
        }
      }
    }
  }

  "topLevel" should {
    "parse with no package" in {
      val source =
        """target java
          | foo : int
          |""".stripMargin

      val res = parseAll(SchemaParser.topLevel, source)
      // pprint.pprintln(res)
      inside(res) { case Right(tl: TopLevel) =>
        assert(tl.meta.language.code === TargetLanguageCode.JAVA)
        assert(tl.meta.package_ === None)
        inside(tl.members) { case NonEmptyList(m0, Nil) =>
          assert(m0.name.lexeme === "foo")
        }
      }
    }

    "parse with package" in {
      val source =
        """target scala
          |package com.example
          |foo : string
          |""".stripMargin

      val res = parseAll(SchemaParser.topLevel, source)
      // pprint.pprintln(res)
      inside(res) { case Right(tl: TopLevel) =>
        assert(tl.meta.language.code === TargetLanguageCode.SCALA)
        assert(tl.meta.package_.map(_.name.lexeme) === Some("com.example"))
        inside(tl.members) { case NonEmptyList(m0, Nil) =>
          assert(m0.name.lexeme === "foo")
        }
      }
    }

    "parse with struct member" in {
      val source =
        """target scala
          |package com.example
          |foo: {
          |  bar: boolean
          |}""".stripMargin

      val res = parseAll(SchemaParser.topLevel, source)
      // pprint.pprintln(res)
      inside(res) { case Right(tl: TopLevel) =>
        assert(tl.meta.language.code === TargetLanguageCode.SCALA)
        assert(tl.meta.package_.map(_.name.lexeme) === Some("com.example"))
        inside(tl.members) { case NonEmptyList(m, Nil) =>
          assert(m.name.lexeme === "foo")
          inside(m.typeTerm.type_) { case TypeStruct(NonEmptyList(m0, Nil)) =>
            assert(m0.name.lexeme === "bar")
            assert(m0.typeTerm.type_ === BasicType.BOOLEAN)
          }
        }
      }
    }
  }
}
