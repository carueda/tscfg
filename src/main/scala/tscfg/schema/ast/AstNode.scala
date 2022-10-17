package tscfg.schema.ast

import cats.data.NonEmptyList
import cats.parse.Caret

sealed trait AstNode

case class Terminal(
    caret: Caret,
    lexeme: String,
)

case class TopLevel(
    meta: Meta,
    members: NonEmptyList[Member],
) extends AstNode

case class Meta(
    language: TargetLanguage,
    package_ : Option[TargetPackage],
) extends AstNode

enum TargetLanguageCode derives CanEqual:
  case SCALA
  case JAVA

case class TargetLanguage(
    languageName: Terminal
) extends AstNode:
  val code: TargetLanguageCode =
    TargetLanguageCode.valueOf(languageName.lexeme.toUpperCase)

case class TargetPackage(
    name: Terminal,
) extends AstNode

case class Member(
    name: Terminal,
    typeTerm: TypeTerminal,
) extends AstNode

case class TypeTerminal(
    caret: Caret,
    type_ : Type,
) extends AstNode

sealed trait Type

enum BasicType extends Type:
  case STRING
  case INT
  case LONG
  case DOUBLE
  case BOOLEAN

case class TypeName(name: String) extends Type

case class TypeStruct(
    members: NonEmptyList[Member],
) extends Type
