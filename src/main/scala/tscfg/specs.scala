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

    case class ObjectType(name: String)     extends SpecType
    case class ListType(elemType: SpecType) extends SpecType

    val recognizedAtomic: Map[String, AtomicType] = Map(
      "string"    → STRING,
      "int"       → INTEGER,
      "integer"   → INTEGER,
      "long"      → LONG,
      "double"    → DOUBLE,
      "boolean"   → BOOLEAN,
      "duration"  → DURATION
    )
  }
  import types._

  sealed abstract class Spec {
    def typ: SpecType
    def isOptional: Boolean
    def defaultValue: Option[String]
    def qualification: Option[String]
    def format(indent: String = ""): String
  }

  case class AtomicSpec(typ: AtomicType,
                        isOptional: Boolean = false,
                        defaultValue: Option[String] = None,
                        qualification: Option[String] = None
                       ) extends Spec {

    override def format(indent: String): String = {
      typ.toString + {
        (if (isOptional) " optional" else "") +
        (if (defaultValue.isDefined) s" default=${defaultValue.get}" else "") +
        (if (qualification.isDefined) s" qualification=${qualification.get}" else "")
      }
    }
  }

  case class ObjSpec(name: String,
                     children: Map[String, Spec],
                     isOptional: Boolean = false,         // TODO
                     defaultValue: Option[String] = None,
                     qualification: Option[String] = None
                    ) extends Spec {

    def typ: SpecType = ObjectType(name)
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

  case class ListSpec(elemSpec: Spec,
                      isOptional: Boolean = false,           // TODO
                      defaultValue: Option[String] = None,
                      qualification: Option[String] = None
                     ) extends Spec {

    def typ: SpecType = ListType(elemSpec.typ)
    override def format(indent: String): String = {
      s"ListOf(${elemSpec.format(indent)})"
    }
  }
}
