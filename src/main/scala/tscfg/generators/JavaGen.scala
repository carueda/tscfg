package tscfg.generators

import tscfg.generators.JavaGen.MethodNames
import tscfg.generators.java.javaUtil._
import tscfg.model._


class JavaGen(genOpts: GenOpts) extends Gen(genOpts) {

  import JavaGen.defs._

  implicit val methodNames = JavaGen.MethodNames()

  import methodNames._

  private val rootListAccessors = collection.mutable.LinkedHashMap[String,String]()

  def generate(objectType: ObjectType): GenResult = {
    genResults = GenResult()

    //checkUserSymbol(className)
    val res = generateForObj(objectType, classNameOpt = Some(className), isRoot = true)

    val header = if (genOpts.preamble.isEmpty) "" else {
      genOpts.preamble.map { p =>
        p.replaceAll("\n", "\n// ")
      } + "\n\n"
    }
    val packageStr = s"package ${genOpts.packageName};\n\n"

    val definition = (header + packageStr + res.definition).trim
    res.copy(definition = (header + res.definition).trim)
    genResults.copy(code = definition)
  }

  private def generate(typ: Type,
                       ind: String = "",
                       classNamePrefixOpt: Option[String],
                       classNameOpt: Option[String] = None
                      ): Res = typ match {

    case ot: ObjectType ⇒ generateForObj(ot, classNamePrefixOpt, classNameOpt, ind)
    case lt: ListType   ⇒ generateForList(lt, classNamePrefixOpt)
    case bt: BasicType  ⇒ generateForBasic(bt)
  }

  private def generateForObj(ot: ObjectType,
                             classNamePrefixOpt: Option[String] = None,
                             classNameOpt: Option[String],
                             ind: String = "",
                             isRoot: Boolean = false
                            ): Res = {

    val className = classNameOpt.getOrElse(generateClassName())
    genResults = genResults.copy(classNames = genResults.classNames + className)

    val symbols = ot.members.keys.toList.sorted

    val results = symbols.map { symbol ⇒
      val javaId = javaIdentifier(symbol)
      genResults = genResults.copy(fieldNames = genResults.fieldNames + javaId)
      val a = ot.members(symbol)
      val res = generate(a.t, ind + "  ", classNamePrefixOpt = Some(className + "."))
      (symbol, res)
    }

    val classDeclMembersStr = results.map { case (symbol, res) ⇒
      val a = ot.members(symbol)
      val memberType = res.javaType
      val typ = if (a.optional) toObjectType(memberType) else memberType
      val javaId = javaIdentifier(symbol)
      s"public final $typ $javaId;"
    }.mkString("\n" + ind + "  ")

    val membersStr = results.map(_._2.definition).filter(_.nonEmpty).
      map(_.replaceAll("\n", "\n" + ind + "  ")).
      mkString("\n")

    implicit val listAccessors = collection.mutable.LinkedHashMap[String,String]()

    val ctorMembersStr = results.map { case (symbol, res) ⇒
      val a = ot.members(symbol)
      val javaId = javaIdentifier(symbol)
      "this." + javaId + " = " + instance(a, res, symbol) + ";"
    }.mkString("\n" + ind + "    ")

    val ind2 = ind + "  "

    val elemAccessorsStr = {
      val objOnes = if (listAccessors.isEmpty) "" else {
        "\n" + ind2 + listAccessors.keys.toList.sorted.map { methodName ⇒
          listAccessors(methodName).replaceAll("\n", "\n" + ind2)
        }.mkString("\n" + ind2)
      }
      val rootOnes = if (!isRoot) "" else {
        if (rootListAccessors.isEmpty) "" else {
          "\n\n" + ind2 + rootListAccessors.keys.toList.sorted.map { methodName ⇒
            rootListAccessors(methodName).replaceAll("\n", "\n" + ind2)
          }.mkString("\n" + ind2)
        }
      }
      objOnes + rootOnes
    }

    val classStr = {
      s"""public ${if (isRoot) "" else "static "}class $className {
         |  $classDeclMembersStr
         |  $membersStr
         |  public $className(com.typesafe.config.Config c) {
         |    $ctorMembersStr
         |  }$elemAccessorsStr
         |}
         |""".stripMargin.replaceAll("\n", "\n" + ind)
    }

    Res(ot,
      javaType = BaseJavaType(classNamePrefixOpt.getOrElse("") + className),
      definition = classStr
    )
  }

  def generateForList(lt: ListType, classNamePrefixOpt: Option[String], ind: String = ""): Res = {
    val elem = generate(lt.t, ind = ind, classNamePrefixOpt)
    val elemRefType = toObjectType(elem.javaType)
    Res(lt,
      javaType = ListJavaType(elemRefType),
      definition = elem.definition
    )
  }

  def generateForBasic(b: BasicType): Res = {
    Res(b, javaType = BaseJavaType(name = b match {
      case STRING    ⇒ "java.lang.String"
      case INTEGER   ⇒ "int"
      case LONG      ⇒ "long"
      case DOUBLE    ⇒ "double"
      case BOOLEAN   ⇒ "boolean"
      case DURATION(q)  ⇒ "long"
    }))
  }

  def toObjectType(javaType: JavaType): JavaType = javaType match {
    case BaseJavaType(name) ⇒ name match {
      case "int"     ⇒ BaseJavaType("java.lang.Integer")
      case "long"    ⇒ BaseJavaType("java.lang.Long")
      case "double"  ⇒ BaseJavaType("java.lang.Double")
      case "boolean" ⇒ BaseJavaType("java.lang.Boolean")
      case _         ⇒ javaType
    }
    case other ⇒ other
  }

  private def instance(a: AnnType, res: Res, path: String)
                      (implicit listAccessors: collection.mutable.LinkedHashMap[String, String]): String = {
    a.t match {
      case bt:BasicType  ⇒ basicInstance(a, bt, path)
      case _:ObjectType  ⇒ objectInstance(a, res, path)
      case lt:ListType   ⇒ listInstance(a, lt, res, path)
    }
  }

  private def objectInstance(a: AnnType, res: Res, path: String): String = {
    val className = res.javaType.toString
    if (a.optional) {
      s"""c.$hasPath("$path") ? $className(c.getConfig("$path")) : null"""
    }
    else s"""$className(c.getConfig("$path"))"""
  }

  private def listInstance(a: AnnType, lt: ListType, res: Res, path: String)
                          (implicit listAccessors: collection.mutable.Map[String, String]
                          ): String = {
    val javaType = res.javaType.asInstanceOf[ListJavaType]
    val base = listMethodName(javaType, lt, path)
    if (a.optional) {
      s"""c.$hasPath("$path") ? $base : null"""
    }
    else base
  }

  private def basicInstance(a: AnnType, bt: BasicType, path: String): String = {
    val getter = tsConfigUtil.basicGetter(bt, path)

    a.default match {
      case Some(v) ⇒
        val value = tsConfigUtil.basicValue(a.t, v)
        s"""c.$hasPath("$path") ? c.$getter : $value"""

      case None if a.optional ⇒
        s"""c.$hasPath("$path") ? c.$getter : null"""

      case _ ⇒
        s"""c.$getter"""
    }
  }

  def listMethodName(javaType: ListJavaType, lt: ListType, path: String)
                    (implicit listAccessors: collection.mutable.Map[String, String],
                     methodNames: MethodNames
                    ): String = {

    val (_, methodName) = rec(javaType, lt, "")
    methodName + s"""(c.getList("$path"))"""
  }

  private def rec(ljt: ListJavaType, lt: ListType, prefix: String
                 )(implicit listAccessors: collection.mutable.Map[String, String],
                   methodNames: MethodNames
                 ): (Boolean, String) = {

    val (isBasic, elemMethodName) = ljt.jt match {
      case bst:BaseJavaType ⇒
        val basic = lt.t.isInstanceOf[BasicType]
        val methodName = baseName(lt.t, bst.toString)
        if (basic) {
          rootListAccessors += methodName → methodNames.basicElemAccessDefinition(methodName)
          rootListAccessors += methodNames.expE → methodNames.expEDef
        }
        (basic, methodName)

      case lst:ListJavaType  ⇒
        rec(lst, lt.t.asInstanceOf[ListType], prefix + methodNames.listPrefix)
    }

    val (methodName, methodBody) = listMethodDefinition(elemMethodName, ljt.jt)

    if (isBasic)
      rootListAccessors += methodName → methodBody
    else
      listAccessors += methodName → methodBody

    (isBasic, methodName)
  }

  private def baseName(t: Type, name: String)
                      (implicit methodNames: MethodNames): String = t match {
    case STRING   ⇒ methodNames.strA
    case INTEGER  ⇒ methodNames.intA
    case LONG     ⇒ methodNames.lngA
    case DOUBLE   ⇒ methodNames.dblA
    case BOOLEAN  ⇒ methodNames.blnA
    case DURATION(q) ⇒ methodNames.durA

    case _: ObjectType  ⇒ name.replace('.', '_')

    case _: ListType ⇒ throw new AssertionError()
  }

  def listMethodDefinition(elemMethodName: String, javaType: JavaType)
                          (implicit methodNames: MethodNames): (String, String) = {

    val elem = if (elemMethodName.startsWith(methodNames.listPrefix))
      s"$elemMethodName((com.typesafe.config.ConfigList)cv)"
    else if (elemMethodName.startsWith("$"))
      s"$elemMethodName(cv)"
    else {
      val adjusted = elemMethodName.replace("_" + methodNames.elemPrefix, "." + methodNames.elemPrefix)
      s"new $adjusted(((com.typesafe.config.ConfigObject)cv).toConfig())"
    }

    val methodName = methodNames.listPrefix + elemMethodName
    val methodDef =
      s"""
         |private static java.util.List<$javaType> $methodName(com.typesafe.config.ConfigList cl) {
         |  java.util.ArrayList<$javaType> al = new java.util.ArrayList<>();
         |  for (com.typesafe.config.ConfigValue cv: cl) {
         |    al.add($elem);
         |  }
         |  return java.util.Collections.unmodifiableList(al);
         |}""".stripMargin.trim
    (methodName, methodDef)
  }
}

object JavaGen {

  object defs {
    abstract sealed class JavaType
    case class BaseJavaType(name: String) extends JavaType {
      override def toString: String = name
    }
    case class ListJavaType(jt: JavaType) extends JavaType {
      override def toString: String = s"java.util.List<$jt>"
    }

    case class Res(typ: Type,
                   javaType: JavaType,
                   definition: String = "")
  }

  case class MethodNames(prefix: String = "$_") {
    val strA = prefix + "str"
    val intA = prefix + "int"
    val lngA = prefix + "lng"
    val dblA = prefix + "dbl"
    val blnA = prefix + "bln"
    val durA = prefix + "dur"
    val expE = prefix + "expE"
    val listPrefix = prefix + "L"
    val elemPrefix = prefix + "E"

    def checkUserSymbol(symbol: String): Unit = {
      if (symbol.startsWith(prefix))
        println(
          s"""
             |WARNING: Symbol $symbol may cause conflict with generated code.
             |         Avoid the $prefix prefix in your spec's identifiers.
         """.stripMargin
        )
    }

    def generateClassName(): String = {
      newClassCount += 1
      elemPrefix + newClassCount
    }

    private var newClassCount = 0

    // definition of methods used to access list's elements of basic type
    val basicElemAccessDefinition: Map[String, String] = {
      Map(
        strA → s"""
          |private static java.lang.String $strA(com.typesafe.config.ConfigValue cv) {
          |  return java.lang.String.valueOf(cv.unwrapped());
          |}""".stripMargin.trim,

        intA → s"""
          |private static java.lang.Integer $intA(com.typesafe.config.ConfigValue cv) {
          |  java.lang.Object u = cv.unwrapped();
          |  if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
          |    !(u instanceof java.lang.Integer)) throw $expE(cv, "integer");
          |  return (java.lang.Integer) u;
          |}
          |""".stripMargin.trim,

        lngA → s"""
          |private static java.lang.Long $lngA(com.typesafe.config.ConfigValue cv) {
          |  java.lang.Object u = cv.unwrapped();
          |  if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
          |    !(u instanceof java.lang.Long) && !(u instanceof java.lang.Integer)) throw $expE(cv, "long");
          |  return ((java.lang.Number) u).longValue();
          |}
          |""".stripMargin.trim,

        dblA → s"""
          |private static java.lang.Double $dblA(com.typesafe.config.ConfigValue cv) {
          |  java.lang.Object u = cv.unwrapped();
          |  if (cv.valueType() != com.typesafe.config.ConfigValueType.NUMBER ||
          |    !(u instanceof java.lang.Number)) throw $expE(cv, "double");
          |  return ((java.lang.Number) u).doubleValue();
          |}
          |""".stripMargin.trim,

        blnA → s"""
          |private static java.lang.Boolean $blnA(com.typesafe.config.ConfigValue cv) {
          |  java.lang.Object u = cv.unwrapped();
          |  if (cv.valueType() != com.typesafe.config.ConfigValueType.BOOLEAN ||
          |    !(u instanceof java.lang.Boolean)) throw $expE(cv, "boolean");
          |  return (java.lang.Boolean) u;
          |}
          |""".stripMargin.trim
      )
    }

    val expEDef: String = {
      s"""
        |private static java.lang.RuntimeException $expE(com.typesafe.config.ConfigValue cv, java.lang.String exp) {
        |  java.lang.Object u = cv.unwrapped();
        |  return new java.lang.RuntimeException(cv.origin().lineNumber()
        |    + ": expecting: " +exp + " got: " + (u instanceof java.lang.String ? "\\"" +u+ "\\"" : u));
        |}
        |""".stripMargin.trim
    }
  }
}
