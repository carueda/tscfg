package tscfg.generators

import tscfg.model._

case class TemplateOpts(indent: String = "  ", commentPrefix: String = "##")

class TemplateGenerator(opts: TemplateOpts) {
  def generate(o: ObjectType): String = genObjectTypeMembers(o)

  private def genEnumType(et: EnumObjectType): String = {
    s"[${et.members.mkString(", ")}]"
  }

  private def genObjectType(o: ObjectRealType): String = {
    val members =
      opts.indent + genObjectTypeMembers(o).replaceAll("\n", "\n" + opts.indent)
    s"""{
       |$members
       |}""".stripMargin
  }

  private def genObjectTypeMembers(o: ObjectRealType): String = {
    val symbols = o.members.keys.toList.sorted
    symbols
      .map { symbol =>
        val a = o.members(symbol)

        // TODO simplify after having changed to List from Option for a.comments
        val (annotations, comments) = a.comments match {
          case Nil => (List.empty, List.empty)
          case comments =>
            val lines = comments.filterNot(_.startsWith("!"))
            lines.partition(_.startsWith("@"))
        }

        val cmn =
          if (comments.isEmpty) ""
          else {
            opts.commentPrefix + comments.mkString(
              "\n" + opts.commentPrefix
            ) + "\n"
          }

        val opt = if (a.optional) "optional" else "required"

        a.t match {
          case _: EnumObjectType =>
            val decl     = opts.commentPrefix + s" '$symbol': Enumeration.\n"
            val (typ, _) = gen(a.t)
            val assign   = symbol + " = " + typ

            decl +
              cmn +
              assign

          case _ =>
            val dfl = a.default.map(d => s". Default: $d").getOrElse("")
            val (typ, abbrev) = gen(a.t)
            val abbrev2 = if (abbrev == abbrevObject) "section" else abbrev
            val decl = opts.commentPrefix + s" '$symbol': $opt $abbrev2$dfl.\n"

            val sysPropOpt: Option[String] = annotations
              .find(_.startsWith(sysPropAnn))
              .map(_.substring(sysPropAnn.length).trim)

            val envVarOpt: Option[String] = annotations
              .find(_.startsWith(envVarAnn))
              .map(_.substring(envVarAnn.length).trim)

            val assign =
              if (abbrev == abbrevObject)
                symbol + " " + typ // do not include `=' for an object
              else if (isBasicAbbrev(abbrev))
                symbol + " = " + "?"
              else
                symbol + " = " + typ

            val assignSysProp =
              sysPropOpt.map(e => s"\n$symbol = $${$e}").getOrElse("")
            val assignEnvVar =
              envVarOpt.map(e => s"\n$symbol = $${?$e}").getOrElse("")

            decl +
              cmn +
              (if (a.optional) "#" + assign.replaceAll("\n", "\n#")
               else assign) +
              assignSysProp +
              assignEnvVar
        }

      }
      .mkString("\n\n")
  }

  private def gen(typ: Type): (String, String) = typ match {
    case o: ObjectType =>
      (genObjectType(o), abbrevObject)

    case aot: AbstractObjectType =>
      (genObjectType(aot), abbrevAbsObject)

    case ObjectRefType(ns, simpleName) =>
      (simpleName, simpleName)

    case b: BasicType =>
      val lc = b.toString.toLowerCase
      (lc, lc)

    case ListType(t) =>
      val (subTyp, abbrev) = gen(t)
      (s"[$subTyp, ...]", abbrevListOf + " " + abbrev)

    case et: EnumObjectType =>
      (genEnumType(et), abbrevEnum)
  }

  private def isBasicAbbrev(abbrev: String): Boolean =
    abbrev != abbrevEnum &&
      abbrev != abbrevObject && abbrev != abbrevAbsObject &&
      !abbrev.startsWith(abbrevListOf)

  private val abbrevEnum      = "enumeration"
  private val abbrevObject    = "object"
  private val abbrevAbsObject = "abstract class"
  private val abbrevListOf    = "list of"

  private val envVarAnn  = "@envvar"
  private val sysPropAnn = "@sysprop"
}
