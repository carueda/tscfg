package tscfg.schema.parsing
import tscfg.schema.ast.Terminal

import cats.parse.{Numbers, Parser => P, Parser0 => P0}

object lx {

  val whitespace: P[Unit] = P.charIn(" \t\r\n").void

  val whitespaces0: P0[Unit] = whitespace.rep0.void

  val letter: P[String] = P.ignoreCaseCharIn('a' to 'z').string

  val letters: P[String] = letter.rep.string

  val underscore: P[String] = P.char('_').string

  val digit: P[String] = Numbers.digit.string

  val idChar: P[String] = letter | digit | underscore

  /** Subset of keywords that are not allowed as identifiers.
    */
  private val Keyword: P[String] =
    val kws = List(
      "define",
      "enum",
      "package",
      "target",
    )
    (P.stringIn(kws) <* !idChar).string

  def s(s: String): P[String] = P.string(s).string

  def term(p: P[String]): P[Terminal] = {
    whitespaces0.with1 *> (P.caret.with1 ~ p) <* whitespaces0
  } map { case (caret, lexeme) =>
    Terminal(caret = caret, lexeme = lexeme)
  }

  def term(str: String): P[Terminal] = term(s(str))

  val identifier: P[String] =
    ((!Keyword).with1 ~
      ((letter | underscore) ~ (letter | digit | underscore).rep0)).string

  private def pKeyword(implicit name: sourcecode.Name): P[Terminal] =
    term(s(name.value.trim))

  object keywords:
    val ` define ` : P[Terminal]  = pKeyword
    val ` enum ` : P[Terminal]    = pKeyword
    val ` package ` : P[Terminal] = pKeyword
    val ` target ` : P[Terminal]  = pKeyword
}
