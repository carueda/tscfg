package tscfg.schema.parsing

import cats.parse.{Numbers, Parser as P, Parser0 as P0}
import tscfg.schema.ast.*
import tscfg.schema.parsing.lx.keywords.*
import tscfg.schema.parsing.lx.{identifier, s, term, whitespaces0}

def withCtx[T](p: P[T])(implicit name: sourcecode.Name): P[T] =
  p.withContext(name.value)

object SchemaParser:

  val languageName: P[Terminal] = withCtx {
    val lowerCaseNames = TargetLanguageCode.values.toList
      .map(v => term(v.toString.toLowerCase))
    P.oneOf(lowerCaseNames)
  }

  val targetLanguage: P[TargetLanguage] = withCtx {
    ` target ` *> languageName <* whitespaces0
  } map TargetLanguage.apply

  val packageName: P[String] = withCtx {
    identifier.repSep(1, P.char('.').string).string <* whitespaces0
  }

  val targetPackage: P[TargetPackage] = withCtx {
    ` package ` *> term(packageName)
  } map TargetPackage.apply

  val meta: P[Meta] = withCtx {
    targetLanguage ~
      targetPackage.?
  } map Meta.apply

  val typeName: P[TypeName] = withCtx {
    identifier
  } map TypeName.apply

  val typeBasic: P[Type] = {
    val typeString  = s("string").map(_ => BasicType.STRING)
    val typeInt     = s("int").map(_ => BasicType.INT)
    val typeLong    = s("long").map(_ => BasicType.LONG)
    val typeBoolean = s("boolean").map(_ => BasicType.BOOLEAN)

    typeString | typeInt | typeLong | typeBoolean
  }

  val typeStruct: P[TypeStruct] = P.defer {
    term("{") *> member.rep(1) <* term("}")
  } map TypeStruct.apply

  val type_ : P[Type] = typeBasic | typeName | typeStruct

  val typeTerm: P[TypeTerminal] = withCtx {
    whitespaces0.with1 *> (P.caret.with1 ~ type_) <* whitespaces0
  } map TypeTerminal.apply

  val member: P[Member] = withCtx {
    term(identifier) ~ (s(":") *> typeTerm)
  } map { case (id, t) =>
    Member(id, t)
  }

  val topLevel: P[TopLevel] = withCtx {
    (meta ~ member.rep(1)) <* (whitespaces0 ~ P.end)
  } map TopLevel.apply
