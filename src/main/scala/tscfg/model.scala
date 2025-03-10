package tscfg

import tscfg.DefineCase._
import tscfg.model.durations._
import tscfg.ns.NamespaceMan

object model {

  object implicits {

    import scala.language.implicitConversions

    implicit def type2ann(t: Type): AnnType = AnnType(t)

    implicit class RichString(s: String) {
      def :=(a: AnnType): (String, AnnType) = s -> a

      def %(a: AnnType): AnnType =
        a.copy(comments = List(s) ++ a.comments)

      def %(t: Type): AnnType = AnnType(t, comments = List(s))
    }

  }

  object durations {

    sealed abstract class DurationQualification

    case object ns extends DurationQualification

    case object us extends DurationQualification

    case object ms extends DurationQualification

    case object second extends DurationQualification

    case object minute extends DurationQualification

    case object hour extends DurationQualification

    case object day extends DurationQualification

  }

  sealed abstract class Type

  sealed abstract class BasicType extends Type

  case object STRING extends BasicType

  case object INTEGER extends BasicType

  case object LONG extends BasicType

  case object DOUBLE extends BasicType

  case object BOOLEAN extends BasicType

  case object SIZE extends BasicType

  case class DURATION(q: DurationQualification) extends BasicType

  val recognizedAtomic: Map[String, BasicType] = Map(
    "string"   -> STRING,
    "int"      -> INTEGER,
    "integer"  -> INTEGER,
    "long"     -> LONG,
    "double"   -> DOUBLE,
    "boolean"  -> BOOLEAN,
    "size"     -> SIZE,
    "duration" -> DURATION(ms)
  )

  case class ListType(t: Type) extends Type

  object AnnType {
    def isAbstract(commentString: String): Boolean =
      getDefineCase(commentString).exists(_.isAbstract)

    def isEnum(commentString: String): Boolean =
      getDefineCase(commentString).exists(_.isEnum)
  }

  final case class AnnType(
      t: Type,
      optional: Boolean = false,
      default: Option[String] = None,
      defineCase: Option[DefineCase] = None,
      docComments: List[String] = Nil,
      comments: List[String] = Nil,
      parentClassMembers: Option[Map[String, model.AnnType]] = None,
  ) {

    val isDefine: Boolean = defineCase.isDefined

    def nameIsImplementsIsExternal: Option[(String, Boolean, Boolean)] =
      defineCase flatMap {
        case c: ExtendsDefineCase    => Some((c.name, false, c.isExternal))
        case c: ImplementsDefineCase => Some((c.name, true, c.isExternal))
        case _                       => None
      }

    def |(d: String): AnnType = copy(default = Some(d))

    def unary_~ : AnnType = copy(optional = true)
  }

  sealed abstract class ObjectAbsType extends Type

  case class EnumObjectType(members: List[EnumElement]) extends ObjectAbsType

  case class EnumElement(name: String, comments: List[String]) {
    override def toString: String = throw new AssertionError(
      "should not be called"
    )
  }

  sealed abstract class ObjectRealType(val members: Map[String, AnnType])
      extends ObjectAbsType

  case class ObjectType(override val members: Map[String, AnnType] = Map.empty)
      extends ObjectRealType(members)

  case class AbstractObjectType(
      override val members: Map[String, AnnType] = Map.empty
  ) extends ObjectRealType(members)

  case class ObjectRefType(namespace: String, simpleName: String)
      extends ObjectAbsType {
    require(NamespaceMan.validName(namespace))
    override def toString: String =
      s"ObjectRefType(namespace='$namespace', simpleName='$simpleName')"
  }

  object ObjectType {
    def apply(elems: (String, AnnType)*): ObjectType = {
      val x = elems.groupBy(_._1).transform((_, v) => v.length).filter(_._2 > 1)
      if (x.nonEmpty)
        throw new RuntimeException(s"key repeated in object: ${x.head}")

      val noQuotes = elems map { case (k, v) =>
        // per Lightbend Config restrictions involving $, leave the key alone if
        // contains $, otherwise unquote the key in case is quoted.
        val adjName = if (k.contains("$")) k else k.replaceAll("^\"|\"$", "")
        adjName.replaceAll("^\"|\"$", "") -> v
      }
      ObjectType(Map.from(noQuotes))
    }
  }

  // $COVERAGE-OFF$
  object util {
    private val IND = "    "

    def format(typ: Type, ind: String = ""): String = typ match {
      case b: BasicType => b.toString

      case ListType(t) => s"[ ${format(t, ind)} ]"

      case EnumObjectType(members) => s"ENUM: ${members.mkString(", ")}"

      case o: ObjectRealType =>
        val symbols = o.members.keys.toList.sorted
        val membersStr = symbols
          .map { symbol =>
            val a = o.members(symbol)

            val cmn =
              if (a.comments.isEmpty) ""
              else {
                val lines = a.comments
                val noOptComments =
                  lines.filterNot(_.trim.startsWith("@optional"))
                if (noOptComments.isEmpty) ""
                else {
                  val x = noOptComments.mkString(s"\n$ind$IND#")
                  s"#$x\n$ind$IND"
                }
              }

            val opt = if (a.optional) "optional " else ""
            val typ = format(a.t, ind + IND)
            val dfl =
              if (a.default.nonEmpty) s" default='${a.default.get}'" else ""

            cmn +
              symbol + ": " + opt + typ + dfl
          }
          .mkString("\n" + ind + IND)

        s"""{
           |$ind$IND$membersStr
           |$ind}""".stripMargin

      case o: ObjectRefType => o.toString
    }
  }

  // $COVERAGE-ON$
}

object modelMain {

  import model._
  import model.implicits._

  // $COVERAGE-OFF$
  def main(args: Array[String]): Unit = {

    val objectType = ObjectType(
      "positions" := "Position information" % ListType(
        ObjectType(
          "lat" := DOUBLE,
          "lon" := DOUBLE,
          "attrs" := ListType(
            ObjectType(
              "b" := BOOLEAN,
              "d" := DURATION(hour)
            )
          )
        )
      ),
      "baz" := "comments for baz..." % ~ObjectType(
        "b" := ~STRING | "some value",
        "a" := LONG | "99999999",
        "i" := "i, an integer" % INTEGER
      ),
      "xyz" := "other string xyz" % ~STRING
    )

    println(model.util.format(objectType))
  }

  // $COVERAGE-ON$
}
