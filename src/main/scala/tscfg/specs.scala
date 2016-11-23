package tscfg

object specs {

  sealed abstract class Spec {
    def format(indent: String = ""): String
  }

  case class AtomicSpec(typ: String) extends Spec {
    override def format(indent: String): String = {
      typ
    }
  }

  case class ObjSpec(children: Map[String, Spec]) extends Spec {
    override def format(indent: String): String = {
      val childrenStr = children.map { case (symbol, spec) ⇒
        symbol + ": " + spec.format(indent + "  ")
      }.mkString("\n" + indent + "  ")
      s"""
        |{
        |$indent  $childrenStr
        |$indent}
      """.stripMargin.trim
    }
  }

  case class ListSpec(elemSpec: Spec) extends Spec {
    override def format(indent: String): String = {
      s"ListOf(${elemSpec.format(indent)})"
    }
  }

  object atomics {
    val stringSpec    = AtomicSpec("string")
    val integerSpec   = AtomicSpec("integer")
    val longSpec      = AtomicSpec("long")
    val doubleSpec    = AtomicSpec("double")
    val booleanSpec   = AtomicSpec("boolean")
    val durationSpec  = AtomicSpec("duration")

    val recognized: Map[String, AtomicSpec] = Map(
      "string"    → atomics.stringSpec,
      "int"       → atomics.integerSpec,
      "integer"   → atomics.integerSpec,
      "long"      → atomics.longSpec,
      "double"    → atomics.doubleSpec,
      "boolean"   → atomics.booleanSpec,
      "duration"  → atomics.durationSpec
    )
  }
}
