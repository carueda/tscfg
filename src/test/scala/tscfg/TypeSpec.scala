package tscfg

import org.specs2.specification.core.Fragments

class TypeSpec extends BaseAccessorSpec {

  val stringBaseType = BaseType("string")
  val intBaseType = BaseType("int")
  val durationBaseType = BaseType("duration")

  // (spec-as-String, base-type, required, value)
  type StringExample = (String, BaseType, Boolean, Option[String])

  def doString(res: Fragments, ex: StringExample) = {
    val (spec, baseType, req, value) = ex
    val t = Type(spec)
    res.append(Seq(
      s""""$spec" have baseType "$baseType"""" in {
        t.baseType must_== baseType
      },
      s""""$spec" have required $req""" in {
        t.required must_== req
      },
      s""""$spec" have value $value""" in {
        t.value must_== value
      }
    ))
  }

  """explicit types""" should {
    List(
      ("string",  stringBaseType, true, None),
      ("string?", stringBaseType, false, None),
      ("string | hello-world", stringBaseType, false, Some(""""hello-world"""")),

      ("int",   intBaseType, true, None),
      ("int?",  intBaseType, false, None),
      ("int|2", intBaseType, false, Some("2")),

      ("duration",   durationBaseType, true, None),
      ("duration?",  durationBaseType, false, None),
      ("duration|2", durationBaseType, false, Some("2"))
    ).foldLeft(Fragments.empty) { (res, ex) => doString(res, ex) }
  }

  """explicit types (qualified)""" should {
    List(
      ("duration:ms",     BaseType("duration", "millisecond"), true, None),
      ("duration:h?",     BaseType("duration", "hour"),  false, None),
      ("duration:s | 1m", BaseType("duration", "second"),  false, Some("1m"))
    ).foldLeft(Fragments.empty) { (res, ex) => doString(res, ex) }
  }

  """inferred string type""" should {
    List(
      ("hello world", stringBaseType, false, Some(""""hello world"""")),
      ("2",           stringBaseType, false, Some(""""2""""))
    ).foldLeft(Fragments.empty) { (res, ex) => doString(res, ex) }
  }

  // (spec-as-AnyVal, base-type, required, value)
  type AnyValExample = (AnyVal, BaseType, Boolean, Option[String])

  def doAnyVal(res: Fragments, ex: AnyValExample) = {
    val (spec, baseType, req, value) = ex
    val t = Type(spec)
    res.append(Seq(
      s"""$spec have baseType "$baseType"""" in {
        t.baseType must_== baseType
      },
      s"""$spec have required $req""" in {
        t.required must_== req
      },
      s"""$spec have value $value""" in {
        t.value must_== value
      }
    ))
  }

  """inferred types with other values""" should {
    List(
      (2,           intBaseType,         false, Some("2")),
      (3.14,        BaseType("double"),  false, Some("3.14")),
      (true,        BaseType("boolean"), false, Some("true")),
      (2147483648L, BaseType("long"),    false, Some("2147483648"))
    ).foldLeft(Fragments.empty) { (res, ex) => doAnyVal(res, ex) }
  }
}
