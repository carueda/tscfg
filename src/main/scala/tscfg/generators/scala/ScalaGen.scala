package tscfg.generators.scala

import tscfg.{ModelBuilder, model}
import tscfg.generators.scala.scalaUtil.scalaIdentifier
import tscfg.generators.{Gen, GenOpts, GenResult, tsConfigUtil}
import tscfg.model._


class ScalaGen(genOpts: GenOpts) extends Gen(genOpts) {
  import accessors._
  import defs._
  implicit val methodNames = MethodNames()
  val getter = Getter(hasPath, methodNames)
  import methodNames._

  def generate(objectType: ObjectType): GenResult = {
    genResults = GenResult()

    checkUserSymbol(className)
    val res = generateForObj(objectType, classNameOpt = Some(className), isRoot = true)

    val header = if (genOpts.preamble.isEmpty) "" else {
      genOpts.preamble.map { p =>
        p.replaceAll("\n", "\n// ")
      } + "\n\n"
    }
    val packageStr = s"package ${genOpts.packageName}\n\n"

    val definition = (header + packageStr + res.definition).trim
    res.copy(definition = (header + res.definition).trim)
    genResults.copy(code = definition)
  }

  private def generate(typ: Type,
                       classNamesPrefix: List[String],
                       classNameOpt: Option[String] = None
                      ): Res = typ match {

    case ot: ObjectType ⇒ generateForObj(ot, classNamesPrefix, classNameOpt)
    case lt: ListType   ⇒ generateForList(lt,classNamesPrefix, classNameOpt)
    case bt: BasicType  ⇒ generateForBasic(bt)
  }

  private def generateForObj(ot: ObjectType,
                             classNamesPrefix: List[String] = List.empty,
                             classNameOpt: Option[String],
                             isRoot: Boolean = false
                            ): Res = {

    val className = classNameOpt.getOrElse(throw new AssertionError())
    genResults = genResults.copy(classNames = genResults.classNames + className)

    val symbols = ot.members.keys.toList.sorted
    symbols.foreach(checkUserSymbol)

    val padScalaIdLength = if (symbols.isEmpty) 0 else
      symbols.map(scalaIdentifier).maxBy(_.length).length
    def padId(id: String) = id + (" " * (padScalaIdLength - id.length))

    val results = symbols.map { symbol ⇒
      val scalaId = scalaIdentifier(symbol)
      genResults = genResults.copy(fieldNames = genResults.fieldNames + scalaId)
      val a = ot.members(symbol)
      val res = generate(a.t,
        classNamesPrefix = className+"." :: classNamesPrefix,
        classNameOpt = Some(scalaUtil.getClassName(symbol))
      )
      (symbol, res)
    }

    val classMembersStr = results.map { case (symbol, res) ⇒
      val a = ot.members(symbol)
      val memberType = res.scalaType
      val typ = if (a.optional && a.default.isEmpty) s"scala.Option[$memberType]" else memberType
      val scalaId = scalaIdentifier(symbol)
      padId(scalaId) + " : " + typ
    }.mkString(",\n  ")

    val classStr = {
      s"""case class $className(
         |  $classMembersStr
         |)
         |""".stripMargin
    }

    implicit val listAccessors = collection.mutable.LinkedHashMap[String,String]()

    val objectMembersStr = results.map { case (symbol, res) ⇒
      val a = ot.members(symbol)
      val scalaId = scalaIdentifier(symbol)
      padId(scalaId) + " = " + getter.instance(a, res, symbol)
    }.mkString(",\n      ")

    val innerClassesStr = {
      val defs = results.map(_._2.definition).filter(_.nonEmpty)
      if (defs.isEmpty) "" else {
        "\n  " + defs.mkString("\n").replaceAll("\n", "\n  ")
      }
    }

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

    val fullClassName = classNamesPrefix.reverse.mkString + className
    val objectString = {
      s"""object $className {$innerClassesStr
         |  def apply(c: com.typesafe.config.Config): $fullClassName = {
         |    $fullClassName(
         |      $objectMembersStr
         |    )
         |  }$elemAccessorsStr
         |}
      """.stripMargin
    }

    Res(ot,
      scalaType = BaseScalaType(classNamesPrefix.reverse.mkString + className),
      definition = classStr +  objectString
    )
  }

  private def generateForList(lt: ListType,
                              classNamesPrefix: List[String],
                              classNameOpt: Option[String]
                             ): Res = {
    val classNameOpt2 = Some(classNameOpt match {
      case None    ⇒ "$Elm"
      case Some(n) ⇒ n + (if (n.endsWith("$Elm")) "" else "$Elm")
    })
    val elem = generate(lt.t, classNamesPrefix, classNameOpt2)
    Res(lt,
      scalaType = ListScalaType(elem.scalaType),
      definition = elem.definition
    )
  }

  private def generateForBasic(b: BasicType): Res = {
    Res(b, scalaType = BaseScalaType(name = b match {
      case STRING   ⇒ "java.lang.String"
      case INTEGER  ⇒ "scala.Int"
      case LONG     ⇒ "scala.Long"
      case DOUBLE   ⇒ "scala.Double"
      case BOOLEAN  ⇒ "scala.Boolean"
      case DURATION(_) ⇒ "scala.Long"
    }))
  }
}

object ScalaGen {
  import java.io.{File, PrintWriter, FileWriter}

  import tscfg.util

  def generate(filename: String, showOut: Boolean = false): GenResult = {
    val file = new File(filename)
    val src = io.Source.fromFile(file).mkString.trim

    if (showOut)
      println("src:\n  |" + src.replaceAll("\n", "\n  |"))

    val className = "Scala" + {
      val noPath = filename.substring(filename.lastIndexOf('/') + 1)
      val noDef = noPath.replaceAll("""^def\.""", "")
      val symbol = noDef.substring(0, noDef.indexOf('.'))
      util.upperFirst(symbol) + "Cfg"
    }

    val objectType = ModelBuilder(src)
    if (showOut)
      println("\nobjSpec:\n  |" + model.util.format(objectType).replaceAll("\n", "\n  |"))

    val genOpts = GenOpts("tscfg.example", className)

    val generator = new ScalaGen(genOpts)

    val results = generator.generate(objectType)

    //println("\n" + results.code)

    val destFilename  = s"src/main/scala/tscfg/example/$className.scala"
    val destFile = new File(destFilename)
    val out = new PrintWriter(new FileWriter(destFile), true)
    out.println(results.code)
    results
  }

  // $COVERAGE-OFF$
  def main(args: Array[String]): Unit = {
    val filename = args(0)
    val results = generate(filename, showOut = true)
    println(
      s"""classNames: ${results.classNames}
         |fieldNames: ${results.fieldNames}
      """.stripMargin)
  }
  // $COVERAGE-ON$
}

private[scala] object defs {
  abstract sealed class ScalaType
  case class BaseScalaType(name: String) extends ScalaType {
    override def toString: String = name
  }
  case class ListScalaType(st: ScalaType) extends ScalaType {
    override def toString: String = s"scala.List[$st]"
  }

  case class Res(typ: Type,
                 scalaType: ScalaType,
                 definition: String = "")
}

private[scala] case class MethodNames(prefix: String = "$_") {
  val strA       = prefix + "str"
  val intA       = prefix + "int"
  val lngA       = prefix + "lng"
  val dblA       = prefix + "dbl"
  val blnA       = prefix + "bln"
  val durA       = prefix + "dur"
  val expE       = prefix + "expE"
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
                |private def $strA(cv:com.typesafe.config.ConfigValue) =
                |  java.lang.String.valueOf(cv.unwrapped())
                |""".stripMargin.trim,

      intA → s"""
                |private def $intA(cv:com.typesafe.config.ConfigValue): scala.Int = {
                |  val u: Any = cv.unwrapped
                |  if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
                |    !u.isInstanceOf[Integer]) throw $expE(cv, "integer")
                |  u.asInstanceOf[Integer]
                |}""".stripMargin.trim,

      lngA → s"""
                |private def $lngA(cv:com.typesafe.config.ConfigValue): scala.Long = {
                |  val u: Any = cv.unwrapped
                |  if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
                |    !u.isInstanceOf[java.lang.Integer] && !u.isInstanceOf[java.lang.Long]) throw $expE(cv, "long")
                |  u.asInstanceOf[java.lang.Number].longValue()
                |}""".stripMargin.trim,

      dblA → s"""
                |private def $dblA(cv:com.typesafe.config.ConfigValue): scala.Double = {
                |  val u: Any = cv.unwrapped
                |  if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) ||
                |    !u.isInstanceOf[java.lang.Number]) throw $expE(cv, "double")
                |  u.asInstanceOf[java.lang.Number].doubleValue()
                |}""".stripMargin.trim,

      blnA → s"""
                |private def $blnA(cv:com.typesafe.config.ConfigValue): scala.Boolean = {
                |  val u: Any = cv.unwrapped
                |  if ((cv.valueType != com.typesafe.config.ConfigValueType.BOOLEAN) ||
                |    !u.isInstanceOf[java.lang.Boolean]) throw $expE(cv, "boolean")
                |  u.asInstanceOf[java.lang.Boolean].booleanValue()
                |}""".stripMargin.trim
    )
  }

  val expEDef: String = {
    s"""
       |private def $expE(cv:com.typesafe.config.ConfigValue, exp:java.lang.String) = {
       |  val u: Any = cv.unwrapped
       |  new java.lang.RuntimeException(cv.origin.lineNumber +
       |    ": expecting: " + exp + " got: " +
       |    (if (u.isInstanceOf[java.lang.String]) "\\"" + u + "\\"" else u))
       |}""".stripMargin.trim
  }
}

private[scala] case class Getter(hasPath: String, implicit val methodNames: MethodNames) {
  import accessors._
  import defs._

  def instance(a: AnnType, res: Res, path: String)
              (implicit listAccessors: collection.mutable.LinkedHashMap[String, String]): String = {
    a.t match {
      case bt:BasicType   ⇒ basicInstance(a, bt, path)
      case _:ObjectType   ⇒ objectInstance(a, res, path)
      case lt:ListType    ⇒ listInstance(a, lt, res, path)
    }
  }

  private def objectInstance(a: AnnType, res: Res, path: String): String = {
    val className = res.scalaType.toString
    if (a.optional) {
      s"""if(c.$hasPath("$path")) scala.Some($className(c.getConfig("$path"))) else None"""
    }
    else s"""$className(c.getConfig("$path"))"""
  }

  private def listInstance(a: AnnType, lt: ListType, res: Res, path: String)
                          (implicit listAccessors: collection.mutable.Map[String, String]
                          ): String = {
    val scalaType: ListScalaType = res.scalaType.asInstanceOf[ListScalaType]
    val base = listMethodName(scalaType, lt, path)
    if (a.optional) {
      s"""if(c.$hasPath("$path")) scala.Some($base) else None"""
    }
    else base
  }

  private def basicInstance(a: AnnType, bt: BasicType, path: String): String = {
    val getter = tsConfigUtil.basicGetter(bt, path)

    a.default match {
      case Some(v) ⇒
        val value = tsConfigUtil.basicValue(a.t, v)
        s"""if(c.$hasPath("$path")) c.$getter else $value"""

      case None if a.optional ⇒
        s"""if(c.$hasPath("$path")) Some(c.$getter) else None"""

      case _ ⇒
        s"""c.$getter"""
    }
  }
}

private[scala] object accessors {
  import defs._

  val rootListAccessors = collection.mutable.LinkedHashMap[String,String]()

  def listMethodName(scalaType: ListScalaType,
                     lt: ListType,
                     path: String
                    )
                    (implicit listAccessors: collection.mutable.Map[String, String],
                     methodNames: MethodNames
                    ): String = {

    val (_, methodName) = rec(scalaType, lt, "")
    methodName + s"""(c.getList("$path"))"""
  }

  private def rec(lst: ListScalaType, lt: ListType, prefix: String
                 )(implicit listAccessors: collection.mutable.Map[String, String],
                   methodNames: MethodNames
                 ): (Boolean, String) = {

    val (isBasic, elemMethodName) = lst.st match {
      case bst:BaseScalaType ⇒
        val basic = lt.t.isInstanceOf[BasicType]
        val methodName = baseName(lt.t, bst.toString)
        if (basic) {
          rootListAccessors += methodName → methodNames.basicElemAccessDefinition(methodName)
          rootListAccessors += methodNames.expE → methodNames.expEDef
        }
        (basic, methodName)

      case lst:ListScalaType  ⇒
        rec(lst, lt.t.asInstanceOf[ListType], prefix + methodNames.listPrefix)
    }

    val (methodName, methodBody) = listMethodDefinition(elemMethodName, lst.st)

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
    case DURATION(_) ⇒ methodNames.durA

    case _: ObjectType  ⇒ name.replace('.', '_')

    case _: ListType ⇒ throw new AssertionError()
  }

  def listMethodDefinition(elemMethodName: String, scalaType: ScalaType)
                          (implicit methodNames: MethodNames): (String, String) = {

    val elem = if (elemMethodName.startsWith(methodNames.listPrefix))
      s"$elemMethodName(cv.asInstanceOf[com.typesafe.config.ConfigList])"
    else if (elemMethodName.startsWith("$"))
      s"$elemMethodName(cv)"
    else {
      val adjusted = elemMethodName.replace("_", ".")
      s"$adjusted(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)"
    }

    val methodName = methodNames.listPrefix + elemMethodName
    val methodDef =
      s"""
         |private def $methodName(cl:com.typesafe.config.ConfigList): scala.List[$scalaType] = {
         |  import scala.collection.JavaConversions._
         |  cl.map(cv => $elem).toList
         |}""".stripMargin.trim
    (methodName, methodDef)
  }
}
