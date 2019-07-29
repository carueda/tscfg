package tscfg.generators.scala

import tscfg.{ModelBuilder, model}
import tscfg.generators._
import tscfg.model._
import tscfg.util.escapeString
import tscfg.codeDefs.scalaDef



class ScalaGen(genOpts: GenOpts) extends Generator(genOpts) {
  val accessors = new Accessors
  import defs._
  implicit val methodNames = MethodNames()
  val getter = Getter(genOpts, hasPath, accessors, methodNames)
  import methodNames._

  val scalaUtil: ScalaUtil = new ScalaUtil(useBackticks = genOpts.useBackticks)
  import scalaUtil.{scalaIdentifier, getClassName}

  def generate(objectType: ObjectType): GenResult = {
    genResults = GenResult()

    checkUserSymbol(className)
    val res = generateForObj(objectType, className = className, isRoot = true)

    val packageStr = s"package ${genOpts.packageName}\n\n"

    val definition = (packageStr + res.definition).trim
    res.copy(definition = res.definition.trim)
    genResults.copy(code = definition)
  }

  private def generate(typ: Type,
                       classNamesPrefix: List[String],
                       className: String
                      ): Res = typ match {

    case ot: ObjectType ⇒ generateForObj(ot, classNamesPrefix, className)
    case lt: ListType   ⇒ generateForList(lt,classNamesPrefix, className)
    case bt: BasicType  ⇒ generateForBasic(bt)
  }

  private def generateForObj(ot: ObjectType,
                             classNamesPrefix: List[String] = List.empty,
                             className: String,
                             isRoot: Boolean = false
                            ): Res = {

    genResults = genResults.copy(classNames = genResults.classNames + className)

    val symbols = ot.members.keys.toList.sorted
    symbols.foreach(checkUserSymbol)

    val padScalaIdLength = if (symbols.isEmpty) 0 else
      symbols.map(scalaIdentifier).maxBy(_.length).length
    def padId(id: String) = id + (" " * (padScalaIdLength - id.length))

    val results = symbols.map { symbol ⇒
      val a = ot.members(symbol)
      val res = generate(a.t,
        classNamesPrefix = className+"." :: classNamesPrefix,
        className = getClassName(symbol)
      )
      (symbol, res)
    }

    val classMembersStr = results.map { case (symbol, res) ⇒
      val a = ot.members(symbol)
      val memberType = res.scalaType
      val typ = if (a.optional && a.default.isEmpty) s"scala.Option[$memberType]" else memberType
      val scalaId = scalaIdentifier(symbol)
      genResults = genResults.copy(fields = genResults.fields + (scalaId → typ.toString))
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
      padId(scalaId) + " = " + getter.instance(a, res, path = escapeString(symbol))
    }.mkString(",\n      ")

    val innerClassesStr = {
      val defs = results.map(_._2.definition).filter(_.nonEmpty)
      if (defs.isEmpty) "" else {
        "\n  " + defs.mkString("\n").replaceAll("\n", "\n  ")
      }
    }

    val elemAccessorsStr = {
      val objOnes = if (listAccessors.isEmpty) "" else {
        "\n" + listAccessors.keys.toList.sorted.map { methodName ⇒
          listAccessors(methodName)
        }.mkString("\n")
      }
      val rootOnes = if (!isRoot) "" else {
        if (accessors.rootListAccessors.isEmpty) "" else {
          "\n\n" + accessors.rootListAccessors.keys.toList.sorted.map { methodName ⇒
            accessors.rootListAccessors(methodName)
          }.mkString("\n")
        }
      }
      objOnes + rootOnes
    }

    val rootAuxClasses = if (isRoot) {
      scalaDef("$TsCfgValidator")
    }
    else ""

    val (ctorParams, errHandlingDecl, errHandlingDispatch) = if (isRoot) {
      ( "c: com.typesafe.config.Config",
        """val $tsCfgValidator: $TsCfgValidator = new $TsCfgValidator()
          |    val parentPath: java.lang.String = ""
          |    val $result = """.stripMargin,

        s"""
           |    $$tsCfgValidator.validate()
           |    $$result
           |""".stripMargin
      )
    }
    else (
      "c: com.typesafe.config.Config, parentPath: java.lang.String, $tsCfgValidator: $TsCfgValidator",
      "",
      ""
    )

    val fullClassName = classNamesPrefix.reverse.mkString + className
    val objectString = {
      s"""object $className {$innerClassesStr
         |  def apply($ctorParams): $fullClassName = {
         |    $errHandlingDecl$fullClassName(
         |      $objectMembersStr
         |    )$errHandlingDispatch
         |  }$elemAccessorsStr
         |$rootAuxClasses}
      """.stripMargin
    }

    Res(ot,
      scalaType = BaseScalaType(classNamesPrefix.reverse.mkString + className),
      definition = classStr +  objectString
    )
  }

  private def generateForList(lt: ListType,
                              classNamesPrefix: List[String],
                              className: String
                             ): Res = {
    val className2 = className + (if (className.endsWith("$Elm")) "" else "$Elm")
    val elem = generate(lt.t, classNamesPrefix, className2)
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
      case SIZE     ⇒ "scala.Long"
      case DURATION(_) ⇒ if(genOpts.useDurations) "java.time.Duration" else "scala.Long"
    }))
  }
}

object ScalaGen {
  import _root_.java.io.{File, PrintWriter, FileWriter}

  import tscfg.util

  // $COVERAGE-OFF$
  def generate(filename: String,
               j7: Boolean = false,
               assumeAllRequired: Boolean = false,
               showOut: Boolean = false,
               useBackticks: Boolean = false
              ): GenResult = {
    val file = new File("src/main/tscfg/" + filename)
    val source = io.Source.fromFile(file).mkString.trim

    if (showOut)
      println("source:\n  |" + source.replaceAll("\n", "\n  |"))

    val className = "Scala" + {
      val noPath = filename.substring(filename.lastIndexOf('/') + 1)
      val noDef = noPath.replaceAll("""^def\.""", "")
      val symbol = noDef.substring(0, noDef.indexOf('.'))
      util.upperFirst(symbol) + "Cfg"
    }

    val buildResult = ModelBuilder(source, assumeAllRequired = assumeAllRequired)
    val objectType = buildResult.objectType
    if (showOut) {
      println("\nobjectType:\n  |" + model.util.format(objectType).replaceAll("\n", "\n  |"))
      if (buildResult.warnings.nonEmpty) {
        println("warnings:")
        buildResult.warnings.foreach(w ⇒ println(s"   line ${w.line}: ${w.source}: ${w.message}"))
      }
    }

    val genOpts = GenOpts("tscfg.example", className, j7 = j7,
                          useBackticks = useBackticks)

    val generator = new ScalaGen(genOpts)

    val results = generator.generate(objectType)

    val destFilename  = s"src/test/scala/tscfg/example/$className.scala"
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

private[scala] case class MethodNames() {
  val strA       = "$_str"
  val intA       = "$_int"
  val lngA       = "$_lng"
  val dblA       = "$_dbl"
  val blnA       = "$_bln"
  val durA       = "$_dur"
  val sizA       = "$_siz"
  val expE       = "$_expE"
  val listPrefix = "$_L"
  val requireName = "$_require"

  def checkUserSymbol(symbol: String): Unit = {
    if (symbol.startsWith("$_"))
      println(
        s"""
           |WARNING: Symbol $symbol may cause conflict with generated code.
           |         Avoid the $$_ prefix in your spec's identifiers.
         """.stripMargin
      )
  }

  // definition of methods used to access list's elements of basic type
  val basicElemAccessDefinition: Map[String, String] = {
    List(strA, intA, lngA, dblA, blnA, sizA)
      .map(k ⇒ k → scalaDef(k))
      .toMap
  }

  val expEDef: String = scalaDef(expE)

  val requireDef: String = scalaDef(requireName)
}

private[scala] case class Getter(genOpts: GenOpts, hasPath: String, accessors: Accessors, implicit val methodNames: MethodNames) {
  import defs._

  def instance(a: AnnType, res: Res, path: String)
              (implicit listAccessors: collection.mutable.LinkedHashMap[String, String]): String = {
    a.t match {
      case bt:BasicType   ⇒ basicInstance(a, bt, path)
      case _:ObjectType   ⇒ objectInstance(a, res, path)
      case lt:ListType    ⇒ listInstance(a, lt, res, path)
    }
  }

  private def objectInstance(a: AnnType, res: Res, path: String)
                            (implicit listAccessors: collection.mutable.Map[String, String]): String = {
    val className = res.scalaType.toString

    val ppArg = s""", parentPath + "$path.", $$tsCfgValidator"""

    val methodName = "$_reqConfig"
    val reqConfigCall = s"""$methodName(parentPath, c, "$path", $$tsCfgValidator)"""
    listAccessors += methodName → scalaDef(methodName)

    if (genOpts.assumeAllRequired)
      s"""$className($reqConfigCall$ppArg)"""

    else if (a.optional) {
      s"""if(c.$hasPath("$path")) scala.Some($className(c.getConfig("$path")$ppArg)) else None"""
    }
    else {
      s"""$className(if(c.$hasPath("$path")) c.getConfig("$path") else com.typesafe.config.ConfigFactory.parseString("$path{}")$ppArg)"""
    }
  }

  private def listInstance(a: AnnType, lt: ListType, res: Res, path: String)
                          (implicit listAccessors: collection.mutable.Map[String, String]
                          ): String = {
    val scalaType: ListScalaType = res.scalaType.asInstanceOf[ListScalaType]
    val base = accessors.listMethodName(scalaType, lt, path)
    if (a.optional) {
      s"""if(c.$hasPath("$path")) scala.Some($base) else None"""
    }
    else base
  }

  private def basicInstance(a: AnnType, bt: BasicType, path: String)
                           (implicit listAccessors: collection.mutable.Map[String, String]): String = {
    val getter = tsConfigUtil.basicGetter(bt, path, genOpts.useDurations)

    a.default match {
      case Some(v) ⇒
        val value = tsConfigUtil.basicValue(a.t, v, useDurations = genOpts.useDurations)
        (bt, value) match {
          case (BOOLEAN, "true")    ⇒ s"""!c.$hasPath("$path") || c.$getter"""
          case (BOOLEAN, "false")   ⇒ s"""c.$hasPath("$path") && c.$getter"""
          case (DURATION(qs), duration) if genOpts.useDurations ⇒ s"""if(c.$hasPath("$path")) c.$getter else java.time.Duration.parse("$duration")"""
          case _                    ⇒ s"""if(c.$hasPath("$path")) c.$getter else $value"""
        }

      case None if a.optional ⇒
        s"""if(c.$hasPath("$path")) Some(c.$getter) else None"""

      case _  ⇒
        bt match {
          case DURATION(_) ⇒ s"""c.$getter"""
          case _ ⇒
            val (methodName, methodCall) = tsConfigUtil.basicRequiredGetter(bt, path, genOpts.useDurations)
            listAccessors += methodName → scalaDef(methodName)
            methodCall
        }
    }
  }
}

private[scala] class Accessors {
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
    methodName + s"""(c.getList("$path"), parentPath, $$tsCfgValidator)"""
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
    case SIZE     ⇒ methodNames.sizA
    case DURATION(_) ⇒ methodNames.durA

    case _: ObjectType  ⇒ name.replace('.', '_')

    case _: ListType ⇒ throw new AssertionError()
  }

  def listMethodDefinition(elemMethodName: String, scalaType: ScalaType)
                          (implicit methodNames: MethodNames): (String, String) = {

    val elem = if (elemMethodName.startsWith(methodNames.listPrefix))
      s"$elemMethodName(cv.asInstanceOf[com.typesafe.config.ConfigList], parentPath, $$tsCfgValidator)"
    else if (elemMethodName.startsWith("$"))
      s"$elemMethodName(cv)"
    else {
      val adjusted = elemMethodName.replace("_", ".")
      s"$adjusted(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig, parentPath, $$tsCfgValidator)"
    }

    val methodName = methodNames.listPrefix + elemMethodName
    val methodDef =
      s"""  private def $methodName(cl:com.typesafe.config.ConfigList, parentPath: java.lang.String, $$tsCfgValidator: $$TsCfgValidator): scala.List[$scalaType] = {
         |    import scala.collection.JavaConverters._
         |    cl.asScala.map(cv => $elem).toList
         |  }""".stripMargin
    (methodName, methodDef)
  }
}
