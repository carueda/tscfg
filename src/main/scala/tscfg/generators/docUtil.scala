package tscfg.generators

import tscfg.model.{AnnType, ObjectRealType}

object docUtil {

  /** This is only for scala. For the root class, for which we don't have a
    * general description, only the params are generated. For simplicity in
    * initial implementation, only first non-empty comment line for each is
    * used.
    */
  def getRootClassDoc(
      objectType: ObjectRealType,
      genOpts: GenOpts,
      symbol2id: String => String,
  ): String = {
    if (genOpts.genDoc) {
      val docLines = objectType.members.toList
        .filterNot(_._2.isDefine)
        .flatMap { case (symbol, a) =>
          a.docComments.map(_.trim).filterNot(_.isEmpty).headOption map {
            firstLine =>
              List(s"@param ${symbol2id(symbol)}", s"  $firstLine")
          }
        }
        .flatten
      if (docLines.nonEmpty) {
        docLines.mkString(s"/** ", "\n  * ", "\n  */\n")
      }
      else ""
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

  private case class ParamDoc(name: String, docLines: List[String])

  private def formatDocComment(
      docComments: List[String],
      paramDocs: List[ParamDoc],
      genScala: Boolean = false,
      indent: String,
  ): String = {
    val lines = collection.mutable.ArrayBuffer[String]()
    val (start, sep, end) = if (genScala) {
      ("\n/** ", "\n  * ", "\n  */\n")
    }
    else {
      lines += ""
      ("\n/**", "\n * ", "\n */\n")
    }
    lines.addAll(docComments.map(_.trim).filter(_.nonEmpty))
    if (paramDocs.nonEmpty) {
      lines += ""
      paramDocs foreach { pd =>
        lines += s"@param ${pd.name}"
        pd.docLines.foreach(lines += "  " + _)
      }
    }
    val res = lines.map(escapeForDoc).mkString(start, sep, end)
    if (res.isEmpty) ""
    else indent + res.replaceAll("\n", "\n" + indent)
  }

  private def escapeForDoc(content: String): String = content
    .replace("/*", "/\\*") // start of comment
    .replace("*/", "*\\/") // end of comment
}
