package tscfg

object specs {

  object types {
    sealed abstract class SpecType

    sealed abstract class AtomicType extends SpecType

    case object STRING    extends AtomicType
    case object INTEGER   extends AtomicType
    case object LONG      extends AtomicType
    case object DOUBLE    extends AtomicType
    case object BOOLEAN   extends AtomicType
    case object DURATION  extends AtomicType

    case object ObjectType  extends SpecType
    case object ListType    extends SpecType
  }
  import types._

  sealed abstract class Spec {
    def typ: SpecType
    def format(indent: String = ""): String
  }

  case class AtomicSpec(typ: AtomicType) extends Spec {
    override def format(indent: String): String = typ.toString
  }

  case class ObjSpec(children: Map[String, Spec]) extends Spec {
    def typ: SpecType = ObjectType
    override def format(indent: String): String = {
      val symbols = children.keys.toList.sorted
      val childrenStr = symbols.map { symbol ⇒
        symbol + ": " + children(symbol).format(indent + "  ")
      }.mkString("\n" + indent + "  ")
      s"""
        |{
        |$indent  $childrenStr
        |$indent}
      """.stripMargin.trim
    }
  }

  case class ListSpec(elemSpec: Spec) extends Spec {
    def typ: SpecType = ListType
    override def format(indent: String): String = {
      s"ListOf(${elemSpec.format(indent)})"
    }
  }

  object atomics {
    val stringSpec    = AtomicSpec(STRING)
    val integerSpec   = AtomicSpec(INTEGER)
    val longSpec      = AtomicSpec(LONG)
    val doubleSpec    = AtomicSpec(DOUBLE)
    val booleanSpec   = AtomicSpec(BOOLEAN)
    val durationSpec  = AtomicSpec(DURATION)

    val recognized: Map[String, AtomicSpec] = Map(
      "string"    → stringSpec,
      "int"       → integerSpec,
      "integer"   → integerSpec,
      "long"      → longSpec,
      "double"    → doubleSpec,
      "boolean"   → booleanSpec,
      "duration"  → durationSpec
    )
  }
}
