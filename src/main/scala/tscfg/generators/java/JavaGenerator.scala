package tscfg.generators.java

import java.io.{FileWriter, PrintWriter}

import tscfg.generator._
import tscfg.generators.Generator
import tscfg.javaUtil._
import tscfg.specs._
import tscfg.specs.types._
import tscfg.{Key, util}

import scala.annotation.tailrec

class JavaGenerator(genOpts: GenOpts) extends Generator {

  val hasPath = if (genOpts.j7) "hasPath" else "hasPathOrNull"

  // defined in terms of corresponding elemAccessor:
  type JavaElemTypeAndAccessor = (String,String)
  val rootDefinedListElemAccessors = collection.mutable.LinkedHashSet[JavaElemTypeAndAccessor]()

  private var staticConfigUsed: Boolean = _

  def generate(objSpec: ObjSpec): GenResult = {

    rootDefinedListElemAccessors.clear()

    var results = GenResult()
    staticConfigUsed = false

    def genForObjSpec(objSpec: ObjSpec, indent: String, isRoot: Boolean = false): Code = {
      // <class>
      val className = getClassName(objSpec.key.simple)
      results = results.copy(classNames = results.classNames + className)

      val staticStr = if (isRoot) "" else " static"
      val code = Code(objSpec.key.simple, objSpec, className)

      code.println(indent + s"public$staticStr class $className {")

      // generate for members:
      val codes = objSpec.orderedNames map { name =>
        genCode(name, objSpec.children(name), indent + IND)
      }

      // member declarations:
      codes.map(_.declaration).filter(_.nonEmpty).map(indent + IND + _).foreach(code.println)

      // member definitions:
      val definitions = codes.map(_.definition).filter(_.nonEmpty)
      if (definitions.nonEmpty) {
        code.println("")
        definitions.foreach(code.print)
      }

      // <constructor>
      code.println("")
      code.println(indent + IND + s"public $className(${util.TypesafeConfigClassName} c) {")
      codes foreach { memberCode ⇒
        code.println(
          indent + IND + IND + "this." + memberCode.javaId +
            " = " + instance(code, memberCode.spec, memberCode.name) + ";"
        )
      }
      code.println(indent + IND + "}")
      // </constructor>

      // auxiliary methods:
      accessors.insertStaticAuxMethods(code, isRoot, indent + IND, results)

      code.println(indent + "}")
      // </class>

      code
    }

    def genForAtomicSpec(name: String, spec: AtomicSpec): Code = {
      Code(name, spec, getJavaType(spec))
    }

    def genForListSpec(name: String, listSpec: ListSpec, indent: String): Code = {
      @tailrec
      def listNesting(ls: ListSpec, levels: Int): (Spec, Int) = ls.elemSpec match {
        case subListSpec: ListSpec ⇒ listNesting(subListSpec, levels + 1)
        case nonListSpec           ⇒ (nonListSpec, levels)
      }

      val (elemSpec, levels) = listNesting(listSpec, 1)

      val elemCode = genCode(name, elemSpec, indent)
      val elemObjType = toObjectType(elemCode.spec)
      val javaType = ("java.util.List<" * levels) + elemObjType + (">" * levels)
      val code = Code(name, listSpec, javaType)

      if (elemCode.definition.nonEmpty) code.println(elemCode.definition)

      code
    }

    def genCode(name: String, spec: Spec, indent: String = ""): Code = spec match {
      case spec: AtomicSpec    ⇒ genForAtomicSpec(name, spec)
      case spec: ObjSpec       ⇒ genForObjSpec(spec, indent)
      case spec: ListSpec      ⇒ genForListSpec(name, spec, indent)
    }

    val header = new StringBuilder()
    //header.append(s"// generated by tscfg $version on ${new Date()}\n")
//    genOpts.preamble foreach { p =>
//      header.append(s"// ${p.replace("\n", "\n// ")}\n\n")
//    }
    header.append(s"package ${genOpts.packageName};\n\n")

    // main class:
    val elemSpec = genForObjSpec(objSpec, "", isRoot = true)

    results = results.copy(code = header.toString() + elemSpec.definition)

    results
  }

  /**
    * Captures code associated with a spec.
    * @param spec       the spec
    * @param javaType   for declaration
    */
  private case class Code(name: String, spec: Spec, javaType: String) {

    val javaId = javaIdentifier(name) //spec.key.simple)

    val declaration = "public final " + javaType + " " + javaId + ";"

    def println(str: String): Unit = defn.append(str).append('\n')

    def print(str: String): Unit = defn.append(str)

    def definition = defn.toString

    val objectDefinedListElemAccessors = collection.mutable.LinkedHashSet[JavaElemTypeAndAccessor]()

    private val defn = new StringBuilder()
  }

  private val IND = "    "

  private def getJavaType(spec: Spec): String = {
    spec match {
      case a: AtomicSpec ⇒
        a.typ match {
            case STRING   ⇒ "java.lang.String"
            case INTEGER  ⇒ if (spec.isOptional) "java.lang.Integer" else "int"
            case LONG     ⇒ if (spec.isOptional) "java.lang.Long"    else "long"
            case DOUBLE   ⇒ if (spec.isOptional) "java.lang.Double"  else "double"
            case BOOLEAN  ⇒ if (spec.isOptional) "java.lang.Boolean" else "boolean"
            case DURATION ⇒ if (spec.isOptional) "java.lang.Long"    else "long"
        }

      case o: ObjSpec  ⇒ getClassName(o.key.simple)

      case l: ListSpec  ⇒
        val elemJavaType = toObjectType(l.elemSpec)
        s"java.util.List<$elemJavaType>"
      }
  }

  private def toObjectType(spec: Spec): String = {
    spec match {
      case a: AtomicSpec ⇒
        a.typ match {
          case STRING   ⇒ "java.lang.String"
          case INTEGER  ⇒ "java.lang.Integer"
          case LONG     ⇒ "java.lang.Long"
          case DOUBLE   ⇒ "java.lang.Double"
          case BOOLEAN  ⇒ "java.lang.Boolean"
          case DURATION ⇒ "java.lang.Long"
        }

      case o: ObjSpec  ⇒
        getClassName(o.key.simple)

      case l: ListSpec  ⇒
        val elemJavaType = toObjectType(l.elemSpec)
        s"java.util.List<$elemJavaType>"
    }
  }

  private def instance(objCode: Code, spec: Spec, path: String): String = {
    spec match {
      case a: AtomicSpec ⇒
        a.typ match {
          case STRING ⇒
            if (spec.defaultValue.isDefined) {
              val value = spec.defaultValue.get
              s"""c != null && c.$hasPath("$path") ? c.getString("$path") : "$value""""
            }
            else if (spec.isOptional) {
              s"""c != null && c.$hasPath("$path") ? c.getString("$path") : null"""
            }
            else
              s"""c.getString("$path")"""

          case INTEGER ⇒
            if (spec.defaultValue.isDefined) {
              val value = spec.defaultValue.get
              s"""c != null && c.$hasPath("$path") ? c.getInt("$path") : $value"""
            }
            else if (spec.isOptional) {
              s"""c != null && c.$hasPath("$path") ? c.getInt("$path") : null"""
            }
            else
              s"""c.getInt("$path")"""

          case LONG ⇒
            if (spec.defaultValue.isDefined) {
              val value = spec.defaultValue.get
              s"""c != null && c.$hasPath("$path") ? c.getLong("$path") : $value"""
            }
            else if (spec.isOptional) {
              s"""c != null && c.$hasPath("$path") ? c.getLong("$path") : null"""
            }
            else
              s"""c.getLong("$path")"""

          case DOUBLE ⇒
            if (spec.defaultValue.isDefined) {
              val value = spec.defaultValue.get
              s"""c != null && c.$hasPath("$path") ? c.getDouble("$path") : $value"""
            }
            else if (spec.isOptional) {
              s"""c != null && c.$hasPath("$path") ? c.getDouble("$path") : null"""
            }
            else
              s"""c.getDouble("$path")"""

          case BOOLEAN ⇒
            if (spec.defaultValue.isDefined) {
              val value = spec.defaultValue.get
              s"""c != null && c.$hasPath("$path") ? c.getBoolean("$path") : $value"""
            }
            else if (spec.isOptional) {
              s"""c != null && c.$hasPath("$path") ? c.getBoolean("$path") : null"""
            }
            else
              s"""c.getBoolean("$path")"""


          case DURATION ⇒ s"""TODO_getDuration("$path")"""
        }

      case o: ObjSpec  ⇒
        staticConfigUsed = true
        val className = getClassName(o.key.simple)
        s"""new $className(${methodNames.configAccess}(c, "$path"))"""

      case l: ListSpec  ⇒
        accessors._listMethodName(l.elemSpec, Some(objCode)) + s"""(c.getList("$path"))"""
    }
  }

  private object accessors {
    def _listMethodName(spec: Spec, objCodeOpt: Option[Code] = None): String = {
      val elemAccessor = spec match {
        case a: AtomicSpec ⇒
          a.typ match {
            case STRING   ⇒ methodNames.strA
            case INTEGER  ⇒ methodNames.intA
            case LONG     ⇒ methodNames.lngA
            case DOUBLE   ⇒ methodNames.dblA
            case BOOLEAN  ⇒ methodNames.blnA
            case DURATION ⇒ methodNames.durA
          }

        case o: ObjSpec  ⇒ getClassName(o.key.simple)

        case l: ListSpec ⇒ _listMethodName(l.elemSpec, objCodeOpt)
      }
      val javaType = toObjectType(spec)

      val definedListElemAccessors = objCodeOpt match {
        case None ⇒
          rootDefinedListElemAccessors

        case Some(objCode) ⇒
          if (atomicElemAccessDefinition.contains(elemAccessor))
            rootDefinedListElemAccessors
          else
            objCode.objectDefinedListElemAccessors
      }

      definedListElemAccessors += ((javaType, elemAccessor))

      "$list" + elemAccessor
    }

    def _listMethodDefinition(elemJavaType: String, elemAccessor: String): (String, String) = {
      val elem = if (elemAccessor.startsWith("$list"))
        s"$elemAccessor((com.typesafe.config.ConfigList)cv)"
      else if (elemAccessor.startsWith("$"))
          s"$elemAccessor(cv)"
      else
          s"new $elemAccessor(((com.typesafe.config.ConfigObject)cv).toConfig())"

      val methodName = s"$$list$elemAccessor"
      val methodDef =
      s"""
         |private static java.util.List<$elemJavaType> $methodName(com.typesafe.config.ConfigList cl) {
         |  java.util.ArrayList<$elemJavaType> al = new java.util.ArrayList<>();
         |  for (com.typesafe.config.ConfigValue cv: cl) {
         |    al.add($elem);
         |  }
         |  return java.util.Collections.unmodifiableList(al);
         |}""".stripMargin.trim

      (methodName, methodDef)
    }

    // definition of methods used to access list's elements of basic type
    val atomicElemAccessDefinition: Map[String, String] = {
      import methodNames._
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

    def _expE: String = {
      val expE = methodNames.expE
      s"""
        |private static java.lang.RuntimeException $expE(com.typesafe.config.ConfigValue cv, java.lang.String exp) {
        |  java.lang.Object u = cv.unwrapped();
        |  return new java.lang.RuntimeException(cv.origin().lineNumber()
        |    + ": expecting: " +exp + " got: " + (u instanceof java.lang.String ? "\\"" +u+ "\\"" : u));
        |}
        |""".stripMargin.trim
    }

    val configGetter = {
      val tscc = util.TypesafeConfigClassName
      s"""
         |private static $tscc ${methodNames.configAccess}($tscc c, java.lang.String path) {
         |  return c != null && c.$hasPath(path) ? c.getConfig(path) : null;
         |}""".stripMargin.trim
    }

    def insertStaticAuxMethods(code:Code, isRoot: Boolean, indent: String, results: GenResult): Unit = {

      val methods = collection.mutable.HashMap[String, String]()

      code.objectDefinedListElemAccessors foreach { case (javaType, elemAccessor) ⇒
        val (methodName, methodDef) = _listMethodDefinition(javaType, elemAccessor)
        methods += methodName → methodDef
      }

      var insertExpE = false
      if (isRoot) {
        rootDefinedListElemAccessors foreach { case (javaType, elemAccessor) ⇒
          val (methodName, methodDef) = _listMethodDefinition(javaType, elemAccessor)
          methods += methodName → methodDef
        }

        rootDefinedListElemAccessors foreach { case (_, elemAccessor) ⇒
          atomicElemAccessDefinition.get(elemAccessor) foreach { methodDef ⇒
            methods += elemAccessor → methodDef
            if (elemAccessor != methodNames.strA) insertExpE = true
          }
        }
      }

      if (isRoot) {
        if (staticConfigUsed) {
          methods += methodNames.configAccess → configGetter
        }
        if (insertExpE) {
          methods += methodNames.expE → _expE
        }
      }

      if (methods.nonEmpty) {
        code.println("")
        methods.keys.toList.sorted foreach { methodName ⇒
          code.println(indent + methods(methodName).replaceAll("\n", "\n" + indent))
        }
      }
    }
  }

  object methodNames {
    val strA      = "$str"
    val intA      = "$int"
    val lngA      = "$lng"
    val dblA      = "$dbl"
    val blnA      = "$bln"
    val durA      = "$dur"
    val configAccess   = "_$config"
    val expE    = "$expE"
  }
}

object JavaGenerator {
  import com.typesafe.config.ConfigFactory
  import tscfg.SpecBuilder
  import java.io.File

  def main(args: Array[String]): Unit = {
    val filename = args(0)
    val file = new File(filename)
    val src = io.Source.fromFile(file).mkString.trim
    println("src:\n  |" + src.replaceAll("\n", "\n  |"))
    val config = ConfigFactory.parseString(src).resolve()

    val className = "Java" + {
      val noPath = filename.substring(filename.lastIndexOf('/') + 1)
      val noDef = noPath.replaceAll("""^def\.""", "")
      val symbol = noDef.substring(0, noDef.indexOf('.'))
      symbol.charAt(0).toUpper + symbol.substring(1) + "Cfg"
    }

    val objSpec = new SpecBuilder(Key(className)).fromConfig(config)
    println("\nobjSpec:\n  |" + objSpec.format().replaceAll("\n", "\n  |"))

    val genOpts = GenOpts("tscfg.example", className,
      preamble = Some(s"source: (a test)")
    )

    val generator: Generator = new JavaGenerator(genOpts)

    val results = generator.generate(objSpec)

    //println("\n" + results.code)

    val destFilename  = s"src/main/java/tscfg/example/$className.java"
    val destFile = new File(destFilename)
    val out = new PrintWriter(new FileWriter(destFile), true)
    out.println(results.code)
  }
}
