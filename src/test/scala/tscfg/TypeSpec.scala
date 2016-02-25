package tscfg

import org.specs2.specification.core.Fragments
import tscfg.generator.GenOpts

class TypeSpec extends BaseAccessorSpec {

  implicit val genOpts = GenOpts()

  """explicit types""" should {
    List(
      ("string", "string", true, None),
      ("string?", "string", false, None),
      ("string | hello-world", "string", false, Some(""""hello-world"""")),
      ("hello-world", "string", false, Some(""""hello-world"""")),

      ("int", "int", true, None),
      ("int?", "int", false, None),
      ("int|2", "int", false, Some("2"))
    ).foldLeft(Fragments.empty) { (res, ex) =>
      val (spec, base, req, value) = ex
      val t = Type(spec)
      res.append(Seq(
        s""""$spec" have base "$base"""" in {
          t.base must_== base
        },
        s""""$spec" have required $req""" in {
          t.required must_== req
        },
        s""""$spec" have value $value""" in {
          t.value must_== value
        }
      ))
    }
  }

  """inferred types""" should {
    List(
      (2,    "int",     false, Some("2")),
      (3.14, "double",  false, Some("3.14")),
      (true, "boolean", false, Some("true")),
      (2147483648L, "long", false, Some("2147483648"))
    ).foldLeft(Fragments.empty) { (res, ex) =>
      val (spec, base, req, value) = ex
      val t = Type(spec)
      res.append(Seq(
        s"""$spec have base "$base"""" in {
          t.base must_== base
        },
        s"""$spec have required $req""" in {
          t.required must_== req
        },
        s"""$spec have value $value""" in {
          t.value must_== value
        }
      ))
    }
  }
}
