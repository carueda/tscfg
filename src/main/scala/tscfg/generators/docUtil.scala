package tscfg.generators

import tscfg.model.{AnnType, ObjectRealType}

object docUtil {

  /** This is only for `case class` (scala) and `record` (java). For the root
    * class, for which we don't have a general description, only the params are
    * generated in these cases. For simplicity in initial implementation, only
    * first non-empty comment line for each is used.
    */
  def getRootClassDoc(
      objectType: ObjectRealType,
      genOpts: GenOpts,
      symbol2id: String => String,
      genScala: Boolean = false,
  ): String = {
    if (genOpts.genDoc) {
      val paramDocs = objectType.members.toList
        .filterNot(_._2.isDefine)
        .flatMap { case (k, memberAnnType) =>
          // only first comment line:
          memberAnnType.docComments
            .map(_.trim)
            .filterNot(_.isEmpty)
            .headOption
            .map(comment => ParamDoc(symbol2id(k), List(comment)))
        }
      if (paramDocs.isEmpty) ""
      else formatDocComment(Nil, paramDocs, genScala, "").trim + "\n"
    }
    else ""
  }

  def getDoc(
      a: AnnType,
      genOpts: GenOpts,
      symbol2id: String => String,
      onlyField: Boolean = false,
      genScala: Boolean = false,
      indent: String = "",
  ): String = {
    if (!genOpts.genDoc) ""
    else {
      val withParams = !onlyField && (genScala || genOpts.genRecords)
      a.t match {
        case ot: ObjectRealType =>
          val paramDocs = if (withParams) {
            // only reflect params with comments
            ot.members.toList.flatMap { case (k, memberAnnType) =>
              val paramComments = memberAnnType.docComments
              if (paramComments.isEmpty) None
              else Some(ParamDoc(symbol2id(k), paramComments))
            }
          }
          else Nil
          if (a.docComments.isEmpty && paramDocs.isEmpty) ""
          else formatDocComment(a.docComments, paramDocs, genScala, indent)

        case _ if onlyField =>
          if (a.docComments.isEmpty) ""
          else formatDocComment(a.docComments, Nil, genScala, indent)

        case _ => ""
      }
    }
  }

  def getEnumDoc(
      docComments: List[String],
      genScala: Boolean = false,
      indent: String = ""
  ): String =
    if (docComments.nonEmpty) {
      formatDocComment(docComments, Nil, genScala = genScala, indent = indent)
    }
    else ""

  private case class ParamDoc(name: String, docLines: List[String])

  private def formatDocComment(
      docComments: List[String],
      paramDocs: List[ParamDoc],
      genScala: Boolean = false,
      indent: String,
  ): String = {
    val docBodyComments = adjustComments(docComments)
    val lines           = collection.mutable.ArrayBuffer[String]()
    lines.addAll(docBodyComments)

    if (docBodyComments.nonEmpty && paramDocs.nonEmpty) {
      lines += ""
    }

    if (paramDocs.nonEmpty) {
      paramDocs foreach { pd =>
        lines += s"@param ${pd.name}"
        pd.docLines.foreach(lines += "  " + _)
      }
    }
    val (start, sep, end) = if (genScala) {
      ("\n/** ", "\n  * ", "\n  */\n")
    }
    else {
      ("\n/**\n * ", "\n * ", "\n */\n")
    }
    val res = lines.map(escapeForDoc).mkString(start, sep, end)
    if (res.isEmpty) ""
    else indent + res.replaceAll("\n", "\n" + indent)
  }

  /** Ignores leading and trailing empty lines, and dedents the remaining
    * non-empty lines.
    */
  private def adjustComments(commentLines: List[String]): List[String] = {
    val body = commentLines
      .dropWhile(_.trim.isEmpty)
      .reverse
      .dropWhile(_.trim.isEmpty)
      .reverse
    if (body.nonEmpty) {
      val leadingSpaces =
        body.filterNot(_.trim.isEmpty).map(_.takeWhile(_.isWhitespace))
      val commonLeadingSpace = leadingSpaces.minBy(_.length)
      body.map(_.stripPrefix(commonLeadingSpace))
    }
    else body
  }

  private def escapeForDoc(content: String): String = content
    .replace("/*", "/\\*") // start of comment
    .replace("*/", "*\\/") // end of comment
}
