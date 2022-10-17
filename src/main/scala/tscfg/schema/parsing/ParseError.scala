package tscfg.schema.parsing

import cats.parse.Parser.{Error, Expectation}
import cats.parse.{LocationMap, Numbers, Parser as P, Parser0 as P0}

import scala.annotation.tailrec

case class ParseError(
    offset: Int,
    position: Option[Position],
    msg: String
)

def parseAll[A](p: P0[A], source: String): Either[ParseError, A] =
  p.parseAll(source) match
    case Right(a)    => Right(a)
    case Left(error) => Left(ParseError(source, error))

object ParseError:

  def apply(source: String, error: Error): ParseError =
    val lm     = LocationMap(source)
    val offset = error.failedAtOffset
    val position = lm
      .toLineCol(offset)
      .map { case (r, c) => Position(r, c) }

    val fragmentLines = collection.mutable.ArrayBuffer.empty[String]

    ParseError(
      offset = offset,
      position = position,
      msg = prettyPrint(lm, error) + "\n" + fragmentLines.mkString("\n")
    )

  // Adapted from https://gist.github.com/re-xyr/b0f1988e744c93d4c83a5f17f58484ea

  @tailrec
  def description(x: Expectation): String = x match
    case Expectation.OneOfStr(_, str :: Nil) =>
      str
    case Expectation.OneOfStr(_, strs) =>
      s"one of ${strs.mkString(", ")}"
    case Expectation.InRange(_, lower, upper) =>
      if lower == upper then lower.toString
      else s"one from $lower to $upper"
    case Expectation.StartOfString(_) =>
      "start-of-string"
    case Expectation.EndOfString(_, _) =>
      "end-of-string"
    case Expectation.Length(_, expected, actual) =>
      s"unexpected eof; expected ${expected - actual} more characters"
    case Expectation.ExpectedFailureAt(_, matched) =>
      s"unexpected: $matched"
    case Expectation.Fail(_) =>
      "failed to parse"
    case Expectation.FailWith(_, message) =>
      message
    case Expectation.WithContext(contextStr, x2) =>
      // not the "sequence" of contexts and description of the top expectation ...
      //      s"$contextStr ${description(x2)}"
      // ... but just the description of the top expectation:
      description(x2)

  def prettyPrint(lm: LocationMap, x: Expectation): String =
    val (row, col) = lm.toLineCol(x.offset).get
    val (r, c)     = (row + 1, col + 1)
    s"$r:$c: error: ${description(x)}"

  private def prettyPrint(lm: LocationMap, error: Error): String =
    error.expected.map(prettyPrint(lm, _)).toList.mkString("\n")
