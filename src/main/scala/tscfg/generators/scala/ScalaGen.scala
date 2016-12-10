package tscfg.generators.scala

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
                       classNamePrefixOpt: Option[String],
                       classNameOpt: Option[String] = None
                      ): Res = typ match {

    case ot: ObjectType ⇒ generateForObj(ot, classNamePrefixOpt, classNameOpt)
    case lt: ListType   ⇒ generateForList(lt,classNamePrefixOpt)
    case bt: BasicType  ⇒ generateForBasic(bt)
  }

  private def generateForObj(ot: ObjectType,
                             classNamePrefixOpt: Option[String] = None,
                             classNameOpt: Option[String],
                             isRoot: Boolean = false
                            ): Res = {

    val className = classNameOpt.getOrElse(generateClassName())
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
        classNamePrefixOpt = Some(className + "."))
      (symbol, res)
    }

    val classMembersStr = results.map { case (symbol, res) ⇒
      val a = ot.members(symbol)
      val memberType = res.scalaType
      val typ = if (a.optional) s"scala.Option[$memberType]" else memberType
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

    val ind2 = "  "

    val innerClassesStr = {
      val defs = results.map(_._2.definition).filter(_.nonEmpty)
      if (defs.isEmpty) "" else {
        "\n" + ind2 + defs.mkString("\n").replaceAll("\n", "\n" + ind2)
      }
    }

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

    val objectString = {
      s"""object $className {$innerClassesStr
         |  def apply(c: com.typesafe.config.Config): $className = {
         |    $className(
         |      $objectMembersStr
         |    )
         |  }$elemAccessorsStr
         |}
      """.stripMargin
    }

    Res(ot,
      scalaType = BaseScalaType(classNamePrefixOpt.getOrElse("") + className),
      definition = classStr +  objectString
    )
  }

  private def generateForList(lt: ListType, classNamePrefixOpt: Option[String]): Res = {
    val elem = generate(lt.t, classNamePrefixOpt)
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
  // $COVERAGE-OFF$
  def main(args: Array[String]): Unit = {
    import _root_.java.io.{File, FileWriter, PrintWriter}

    import tscfg.{ModelBuilder, model}

    val filename = args(0)
    val file = new File(filename)
    val source = io.Source.fromFile(file).mkString.trim
    println("source:\n  |" + source.replaceAll("\n", "\n  |"))
    val objectType = ModelBuilder(source)
    println(s"objectType:")
    println(model.util.format(objectType))

    val className = "Scala" + {
      val noPath = filename.substring(filename.lastIndexOf('/') + 1)
      val noDef = noPath.replaceAll("""^def\.""", "")
      val symbol = noDef.substring(0, noDef.indexOf('.'))
      tscfg.util.upperFirst(symbol) + "Cfg"
    }

    val genOpts = GenOpts("tscfg.example", className)

    val res = new ScalaGen(genOpts).generate(objectType)
    //println("\n" + res.code)

    val destFilename  = s"src/main/scala/tscfg/example/$className.scala"
    val destFile = new File(destFilename)
    val out = new PrintWriter(new FileWriter(destFile), true)
    out.println(res.code)
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
                |private def $strA(cv:com.typesafe.config.ConfigValue) =
                |  java.lang.String.valueOf(cv.unwrapped())
                |""".stripMargin.trim,

      intA → s"""
                |private def $intA(cv:com.typesafe.config.ConfigValue): scala.Int = {
                |  val u: Any = cv.unwrapped
                |  if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER)
                |    || !u.isInstanceOf[Integer]) throw $expE(cv, "integer")
                |  u.asInstanceOf[Integer]
                |}""".stripMargin.trim,

      lngA → s"""
                |private def $lngA(cv:com.typesafe.config.ConfigValue): scala.Long = {
                |  val u: Any = cv.unwrapped
                |  if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER)
                |    || !u.isInstanceOf[java.lang.Integer] && !u.isInstanceOf[java.lang.Long]) throw $expE(cv, "long")
                |  u.asInstanceOf[java.lang.Number].longValue()
                |}""".stripMargin.trim,

      dblA → s"""
                |private def $dblA(cv:com.typesafe.config.ConfigValue): scala.Double = {
                |  val u: Any = cv.unwrapped
                |  if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER) || !u.isInstanceOf[java.lang.Number])
                |    throw $expE(cv, "double")
                |  u.asInstanceOf[java.lang.Number].doubleValue()
                |}""".stripMargin.trim,

      blnA → s"""
                |private def $blnA(cv:com.typesafe.config.ConfigValue): scala.Boolean = {
                |  val u: Any = cv.unwrapped
                |  if ((cv.valueType != com.typesafe.config.ConfigValueType.BOOLEAN) || !u.isInstanceOf[java.lang.Boolean])
                |    throw $expE(cv, "boolean")
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
    else if (elemMethodName.startsWith(methodNames.elemPrefix)) {
      val adjusted = elemMethodName.replace("_" + methodNames.elemPrefix, "." + methodNames.elemPrefix)
      s"$adjusted(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)"
    }
    else if (elemMethodName.startsWith("$"))
      s"$elemMethodName(cv)"
    else {
      val adjusted = elemMethodName.replace("_" + methodNames.elemPrefix, "." + methodNames.elemPrefix)
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
