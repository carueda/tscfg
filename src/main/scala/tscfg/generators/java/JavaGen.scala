package tscfg.generators.java

import tscfg.generators.java.javaUtil._
import tscfg.generators._
import tscfg.model._
import tscfg.util.escapeString


class JavaGen(genOpts: GenOpts) extends Generator(genOpts) {

  import defs._
  implicit val methodNames = MethodNames()

  def generate(objectType: ObjectType): GenResult = {
    genResults = GenResult()

    methodNames.checkUserSymbol(className)
    val res = generateForObj(objectType, className = className, isRoot = true)

    val packageStr = s"package ${genOpts.packageName};\n\n"

    val definition = (packageStr + res.definition).trim
    res.copy(definition = res.definition.trim)
    genResults.copy(code = definition)
  }

  private def generate(typ: Type,
                       classNamePrefixOpt: Option[String],
                       className: String
                      ): Res = typ match {

    case ot: ObjectType ⇒ generateForObj(ot, classNamePrefixOpt, className)
    case lt: ListType   ⇒ generateForList(lt, classNamePrefixOpt, className)
    case bt: BasicType  ⇒ generateForBasic(bt)
  }

  private def generateForObj(ot: ObjectType,
                             classNamePrefixOpt: Option[String] = None,
                             className: String,
                             isRoot: Boolean = false
                            ): Res = {

    val classNameAdjusted = adjustClassName(className)
    genResults = genResults.copy(classNames = genResults.classNames + classNameAdjusted)

    val symbols = ot.members.keys.toList.sorted
    symbols.foreach(methodNames.checkUserSymbol)

    val results = symbols.map { symbol ⇒
      val a = ot.members(symbol)
      val res = generate(a.t,
        classNamePrefixOpt = Some(classNameAdjusted + "."),
        className = javaUtil.getClassName(symbol)
      )
      (symbol, res)
    }

    val classDeclMembers = results.map { case (symbol, res) ⇒
      val a = ot.members(symbol)
      val memberType = res.javaType
      val typ = if (a.optional && a.default.isEmpty) toObjectType(memberType) else memberType
      val javaId = javaIdentifier(symbol)
      (typ, javaId)
    }

    val classDeclMembersStr = classDeclMembers.map{ case (typ, javaId) ⇒
      genResults = genResults.copy(fields = genResults.fields + (javaId → typ.toString))
      s"public final $typ $javaId;"
    }.mkString("\n  ")

    val classMemberGettersStr = if (genOpts.genGetters) {
      classDeclMembers.map{ case (typ, javaId) ⇒
        val getter = s"get${javaId.capitalize}"
        genResults = genResults.copy(getters = genResults.getters + (getter → typ.toString))
        s"public final $typ $getter() { return $javaId; }"
      }.mkString("\n  ", "\n  ", "")
    }
    else ""

    val membersStr = {
      val defined = results.map(_._2.definition).filter(_.nonEmpty)
      if (defined.isEmpty) "" else {
        defined.map(_.replaceAll("\n", "\n  ")).mkString("\n  ")
      }
    }

    implicit val listAccessors = collection.mutable.LinkedHashMap[String,String]()

    val ctorMembersStr = results.map { case (symbol, res) ⇒
      val a = ot.members(symbol)
      val javaId = javaIdentifier(symbol)
      "this." + javaId + " = " + instance(a, res, path = escapeString(symbol)) + ";"
    }.mkString("\n    ")

    val elemAccessorsStr = {
      val objOnes = if (listAccessors.isEmpty) "" else {
        "\n  " + listAccessors.keys.toList.sorted.map { methodName ⇒
          listAccessors(methodName).replaceAll("\n", "\n  ")
        }.mkString("\n  ")
      }
      val rootOnes = if (!isRoot) "" else {
        if (rootListAccessors.isEmpty) "" else {
          "\n\n  " + rootListAccessors.keys.toList.sorted.map { methodName ⇒
            rootListAccessors(methodName).replaceAll("\n", "\n  ")
          }.mkString("\n  ")
        }
      }
      objOnes + rootOnes
    }

    val classStr = {
      s"""public ${if (isRoot) "" else "static "}class $classNameAdjusted {
         |  $classDeclMembersStr$classMemberGettersStr
         |  $membersStr
         |  public $classNameAdjusted(com.typesafe.config.Config c) {
         |    $ctorMembersStr
         |  }$elemAccessorsStr
         |}
         |""".stripMargin
    }

    Res(ot,
      javaType = BaseJavaType(classNamePrefixOpt.getOrElse("") + classNameAdjusted),
      definition = classStr
    )
  }

  private def generateForList(lt: ListType,
                              classNamePrefixOpt: Option[String],
                              className: String): Res = {
    val className2 = className + (if (className.endsWith("$Elm")) "" else "$Elm")
    val elem = generate(lt.t, classNamePrefixOpt, className2)
    val elemRefType = toObjectType(elem.javaType)
    Res(lt,
      javaType = ListJavaType(elemRefType),
      definition = elem.definition
    )
  }

  private def generateForBasic(b: BasicType): Res = {
    Res(b, javaType = BaseJavaType(name = b match {
      case STRING    ⇒ "java.lang.String"
      case INTEGER   ⇒ "int"
      case LONG      ⇒ "long"
      case DOUBLE    ⇒ "double"
      case BOOLEAN   ⇒ "boolean"
      case SIZE      ⇒ "long"
      case DURATION(q) ⇒ "long"
    }))
  }

  private val rootListAccessors = collection.mutable.LinkedHashMap[String,String]()

  /**
    * Avoids duplicate class names as they are not supported by Java (the namespaces implied
    * by nesting are not enough to distinguish inner classes with the same name)
    */
  private def adjustClassName(className: String): String = {
    classNameCounter.get(className) match {
      case None ⇒
        classNameCounter.put(className, 1)
        className
      case Some(counter) ⇒
        classNameCounter.put(className, counter + 1)
        className + (counter + 1)
    }
  }
  private val classNameCounter = collection.mutable.HashMap[String,Int]()

  private def toObjectType(javaType: JavaType): JavaType = javaType match {
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
      s"""c.$hasPath("$path") ? new $className(c.getConfig("$path")) : null"""
    }
    else s"""new $className(c.getConfig("$path"))"""
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
        (bt, value) match {
          case (BOOLEAN, "true")  ⇒ s"""!c.$hasPath("$path") || c.$getter"""
          case (BOOLEAN, "false") ⇒ s"""c.$hasPath("$path") && c.$getter"""
          case _                  ⇒ s"""c.$hasPath("$path") ? c.$getter : $value"""
        }

      case None if a.optional ⇒
        s"""c.$hasPath("$path") ? c.$getter : null"""

      case _ ⇒
        s"""c.$getter"""
    }
  }

  private def listMethodName(javaType: ListJavaType, lt: ListType, path: String)
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
    case SIZE     ⇒ methodNames.sizA
    case DURATION(q) ⇒ methodNames.durA

    case _: ObjectType  ⇒ name.replace('.', '_')

    case _: ListType ⇒ throw new AssertionError()
  }

  private def listMethodDefinition(elemMethodName: String, javaType: JavaType)
                                  (implicit methodNames: MethodNames): (String, String) = {

    val elem = if (elemMethodName.startsWith(methodNames.listPrefix))
      s"$elemMethodName((com.typesafe.config.ConfigList)cv)"
    else if (elemMethodName.startsWith("$"))
      s"$elemMethodName(cv)"
    else {
      val adjusted = elemMethodName.replace("_", ".")
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
  import _root_.java.io.{File, PrintWriter, FileWriter}
  import tscfg.{ModelBuilder, model}
  import tscfg.util

  // $COVERAGE-OFF$
  def generate(filename: String, showOut: Boolean = false,
               genGetters: Boolean = false): GenResult = {
    val file = new File("src/main/tscfg/" + filename)
    val source = io.Source.fromFile(file).mkString.trim

    if (showOut)
      println("source:\n  |" + source.replaceAll("\n", "\n  |"))

    val className = "Java" + {
      val noPath = filename.substring(filename.lastIndexOf('/') + 1)
      val noDef = noPath.replaceAll("""^def\.""", "")
      val symbol = noDef.substring(0, noDef.indexOf('.'))
      util.upperFirst(symbol) + "Cfg"
    }

    val buildResult = ModelBuilder(source)
    val objectType = buildResult.objectType
    if (showOut) {
      println("\nobjectType:\n  |" + model.util.format(objectType).replaceAll("\n", "\n  |"))
      if (buildResult.warnings.nonEmpty) {
        println("warnings:")
        buildResult.warnings.foreach(w ⇒ println(s"   line ${w.line}: ${w.source}: ${w.message}"))
      }
    }

    val genOpts = GenOpts("tscfg.example", className, j7 = false,
                          genGetters = genGetters)

    val generator = new JavaGen(genOpts)

    val results = generator.generate(objectType)

    //println("\n" + results.code)

    val destFilename  = s"src/test/java/tscfg/example/$className.java"
    val destFile = new File(destFilename)
    val out = new PrintWriter(new FileWriter(destFile), true)
    out.println(results.code)
    results
  }

  def main(args: Array[String]): Unit = {
    val filename = args(0)
    val results = generate(filename, showOut = true)
    println(
      s"""classNames: ${results.classNames}
         |fields    : ${results.fields}
      """.stripMargin)
  }
  // $COVERAGE-ON$

}

private[java] object defs {
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

private[java] case class MethodNames(prefix: String = "$_") {
  val strA = prefix + "str"
  val intA = prefix + "int"
  val lngA = prefix + "lng"
  val dblA = prefix + "dbl"
  val blnA = prefix + "bln"
  val durA = prefix + "dur"
  val sizA = prefix + "siz"
  val expE = prefix + "expE"
  val listPrefix = prefix + "L"

  def checkUserSymbol(symbol: String): Unit = {
    if (symbol.startsWith(prefix))
      println(
        s"""
           |WARNING: Symbol $symbol may cause conflict with generated code.
           |         Avoid the $prefix prefix in your spec's identifiers.
         """.stripMargin
      )
  }

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

      // since there's no something like cv.getBytes() nor is SimpleConfig.parseBytes visible,
      // use ConfigFactory.parseString:
      sizA → s"""
                |private static java.lang.Long $sizA(com.typesafe.config.ConfigValue cv) {
                |  java.lang.Object u = cv.unwrapped();
                |  if (cv.valueType() == com.typesafe.config.ConfigValueType.NUMBER ||
                |     (u instanceof java.lang.Long) || (u instanceof java.lang.Integer))
                |    return ((java.lang.Number) u).longValue();
                |  if (cv.valueType() == com.typesafe.config.ConfigValueType.STRING) {
                |    return com.typesafe.config.ConfigFactory.parseString("s = " + '"' + u + '"').getBytes("s");
                |  }
                |  throw $expE(cv, "size");
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
