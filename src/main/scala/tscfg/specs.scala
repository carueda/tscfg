package tscfg

object specs {

  object types {
    sealed abstract class AtomicType

    case object STRING    extends AtomicType
    case object INTEGER   extends AtomicType
    case object LONG      extends AtomicType
    case object DOUBLE    extends AtomicType
    case object BOOLEAN   extends AtomicType
    case object DURATION  extends AtomicType

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

  case class ObjSpec(key: Key,
                     children: Map[String, Spec],
                     isOptional: Boolean = false, // TODO
                     defaultValue: Option[String] = None,
                     qualification: Option[String] = None
                    ) extends Spec {

    val orderedNames = children.keys.toList.sorted

    override def format(indent: String): String = {
      val symbols = orderedNames
      val childrenStr = symbols.map { symbol ⇒
        symbol + ": " + children(symbol).format(indent + "  ")
      }.mkString("\n" + indent + "  ")
      s"""
        |$key{
        |$indent  $childrenStr
        |$indent}
      """.stripMargin.trim
    }
  }

  case class ListSpec(elemSpec: Spec,
                      isOptional: Boolean = false, // TODO
                      defaultValue: Option[String] = None,
                      qualification: Option[String] = None
                     ) extends Spec {

    override def format(indent: String): String = {
      s"ListOf(${elemSpec.format(indent)})"
    }
  }
}
