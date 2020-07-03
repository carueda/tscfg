package tscfg

import tscfg.DefineCase._
import tscfg.model.durations._

object model {

  object implicits {

    import scala.language.implicitConversions

    implicit def type2ann(t: Type): AnnType = AnnType(t)

    implicit class RichString(s: String) {
      def :=(a: AnnType): (String, AnnType) = s -> a

      def %(a: AnnType): AnnType = a.copy(comments = Some(s + a.comments.getOrElse("")))

      def %(t: Type): AnnType = AnnType(t, comments = Some(s))
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
    "string" -> STRING,
    "int" -> INTEGER,
    "integer" -> INTEGER,
    "long" -> LONG,
    "double" -> DOUBLE,
    "boolean" -> BOOLEAN,
    "size" -> SIZE,
    "duration" -> DURATION(ms)
  )

  case class ListType(t: Type) extends Type


  final object AnnType {

    def isParent(commentString: String): Boolean =
      getDefineCase(commentString).exists(_.isParent)

    def isEnum(commentString: String): Boolean =
      getDefineCase(commentString).exists(_.isEnum)
  }

  final case class AnnType(t: Type,
                           optional: Boolean = false,
                           default: Option[String] = None,
                           comments: Option[String] = None,
                           parentClassMembers: Option[Map[String, model.AnnType]] = None
                          ) {

    val defineCase: Option[DefineCase] = comments.flatMap(cs => getDefineCase(cs))

    val isDefine: Boolean = defineCase.isDefined

    val abstractClass: Option[String] = defineCase flatMap {
      case ExtendsDefineCase(name) => Some(name)
      case _ => None
    }

    def |(d: String): AnnType = copy(default = Some(d))

    def unary_~ : AnnType = copy(optional = true)
  }


  sealed abstract class ObjectAbsType extends Type

  case class EnumObjectType(members: List[String]) extends ObjectAbsType

  sealed abstract class ObjectRealType(val members: Map[String, AnnType]) extends ObjectAbsType

  case class ObjectType(override val members: Map[String, AnnType] = Map.empty) extends ObjectRealType(members)

  case class AbstractObjectType(override val members: Map[String, AnnType] = Map.empty) extends ObjectRealType(members)

  case class ObjectRefType(namespace: Namespace, simpleName: String) extends ObjectAbsType {
    override def toString: String = s"ObjectRefType(namespace='${namespace.getPathString}', simpleName='$simpleName')"
  }

  object ObjectType {
    def apply(elems: (String, AnnType)*): ObjectType = {
      val x = elems.groupBy(_._1).transform((_, v) => v.length).filter(_._2 > 1)
      if (x.nonEmpty) throw new RuntimeException(s"key repeated in object: ${x.head}")

      val noQuotes = elems map { case (k, v) =>
        // per Lightbend Config restrictions involving $, leave the key alone if
        // contains $, otherwise unquote the key in case is quoted.
        val adjName = if (k.contains("$")) k else k.replaceAll("^\"|\"$", "")
        adjName.replaceAll("^\"|\"$", "") -> v
      }
      ObjectType(Map(noQuotes: _*))
    }
  }

  // $COVERAGE-OFF$
  object util {
    val IND = "    "

    def format(typ: Type, ind: String = ""): String = typ match {
      case b: BasicType => b.toString

      case ListType(t) => s"[ ${format(t, ind)} ]"

      case EnumObjectType(members) => s"ENUM: ${members.mkString(", ")}"

      case o: ObjectRealType =>
        val symbols = o.members.keys.toList.sorted
        val membersStr = symbols.map { symbol =>
          val a = o.members(symbol)

          val cmn = if (a.comments.isEmpty) "" else {
            val lines = a.comments.get.split("\n")
            val noOptComments = lines.filterNot(_.trim.startsWith("@optional"))
            if (noOptComments.isEmpty) "" else {
              val x = noOptComments.mkString(s"\n$ind$IND#")
              s"#$x\n$ind$IND"
            }
          }

          val opt = if (a.optional) "optional " else ""
          val typ = format(a.t, ind + IND)
          val dfl = if (a.default.nonEmpty) s" default='${a.default.get}'" else ""

          cmn +
            symbol + ": " + opt + typ + dfl
        }.mkString("\n" + ind + IND)

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
      "positions" := "Position information" % ListType(ObjectType(
        "lat" := DOUBLE,
        "lon" := DOUBLE,
        "attrs" := ListType(ObjectType(
          "b" := BOOLEAN,
          "d" := DURATION(hour)
        ))
      )),
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
