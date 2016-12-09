package tscfg

import com.typesafe.config._

import scala.collection.JavaConversions._


class ModelBuilder {
  import collection._

  def fromConfig(conf: Config): model.ObjectType = {
    val memberStructs = getMemberStructs(conf)
    val members: immutable.Map[String, model.AnnType] = memberStructs.map { childStruct ⇒
      val name = childStruct.name
      val cv = conf.getValue(name)

      val (childType, optional, default, qualification) = {
        if (childStruct.isLeaf) {
          val typ = fromConfigValue(cv)
          val valueString = cv.unwrapped().toString
          if (typ == model.STRING) {
            toAnnBasicType(valueString) match {
              case Some(annBasicType) ⇒
                annBasicType
              case None ⇒
                (typ, true, Some(valueString), None)
            }
          }
          else if (typ.isInstanceOf[model.BasicType])
            (typ, true, Some(valueString), None)
          else
            (typ, false, None, None)
        }
        else {
          (fromConfig(conf.getConfig(name)), false, None, None)
        }
      }

      val comments = cv.origin().comments().toList
      val optFromComments = comments.exists(_.trim.startsWith("@optional"))
      val commentsOpt = if (comments.isEmpty) None else Some(comments.mkString("\n"))

      name -> model.AnnType(childType,
        optional      = optional || optFromComments,
        default       = default,
        qualification = qualification,
        comments      = commentsOpt
      )
    }.toMap
    model.ObjectType(members)
  }

  private case class Struct(name: String, members: mutable.HashMap[String, Struct] = mutable.HashMap.empty) {
    def isLeaf: Boolean = members.isEmpty
    // $COVERAGE-OFF$
    def format(indent: String = ""): String = {
      if (members.isEmpty)
        name
      else
        s"$name:\n" +
          members.map(e ⇒ indent + e._1 + ": " + e._2.format(indent + "    ")).mkString("\n")
    }
    // $COVERAGE-ON$
  }

  private def getMemberStructs(conf: Config): List[Struct] = {
    val structs = mutable.HashMap[Key, Struct](Key.root → Struct(""))
    def resolve(key: Key): Struct = {
      if (!structs.contains(key)) structs.put(key, Struct(key.simple))
      structs(key)
    }

    conf.entrySet() foreach { e ⇒
      val name = e.getKey
      val key = Key(name)
      val leaf = Struct(name)
      doAncestorsOf(key, leaf)

      def doAncestorsOf(childKey: Key, childStruct: Struct): Unit = {
        createParent(childKey.parent, childKey.simple, childStruct)

        @annotation.tailrec
        def createParent(parentKey: Key, simple: String, child: Struct): Unit = {
          val parentGroup = resolve(parentKey)
          parentGroup.members.put(simple, child)
          if (parentKey != Key.root) {
            createParent(parentKey.parent, parentKey.simple, parentGroup)
          }
        }
      }
    }
    structs(Key.root).members.values.toList
  }

  private def fromConfigValue(cv: ConfigValue): model.Type = {
    import ConfigValueType._
    cv.valueType() match {
      case STRING  => model.STRING
      case BOOLEAN => model.BOOLEAN
      case NUMBER  => numberType(cv.unwrapped().toString)
      case LIST    => listType(cv.asInstanceOf[ConfigList])
      case OBJECT  => objType(cv.asInstanceOf[ConfigObject])
      case NULL    => throw new AssertionError("null unexpected")
    }
  }

  def toAnnBasicType(valueString: String):
      Option[(model.BasicType, Boolean, Option[String], Option[String])] = {

    val tokens = valueString.split("""\s*\|\s*""")
    val typePart = tokens(0).toLowerCase
    val hasDefault = tokens.size == 2
    val defaultValue = if (hasDefault) Some(tokens(1)) else None

    val (baseString, isOpt) = if (typePart.endsWith("?"))
      (typePart.substring(0, typePart.length - 1), true)
    else
      (typePart, false)

    val (base, qualification) = {
      val parts = baseString.split("""\s*\:\s*""", 2)
      if (parts.length == 1)
        (parts(0), None)
      else
        (parts(0), Some(parts(1)))
    }

    model.recognizedAtomic.get(base) map { basicType ⇒
      (basicType, isOpt, defaultValue, qualification)
    }
  }

  private def listType(cv: ConfigList): model.ListType = {
    if (cv.isEmpty) throw new IllegalArgumentException("list with one element expected")

    if (cv.size() > 1) {
      val line = cv.origin().lineNumber()
      val options: ConfigRenderOptions = ConfigRenderOptions.defaults
        .setFormatted(false).setComments(false).setOriginComments(false)
      println(s"$line: ${cv.render(options)}: WARN: only first element will be considered")
    }

    val cv0: ConfigValue = cv.get(0)
    cv0.valueType()
    model.ListType(fromConfigValue(cv0))
  }

  private def objType(cv: ConfigObject): model.ObjectType = fromConfig(cv.toConfig)

  private def numberType(valueString: String): model.BasicType = {
    try {
      valueString.toInt
      model.INTEGER
    }
    catch {
      case _:NumberFormatException =>
        try {
          valueString.toLong
          model.LONG
        }
        catch {
          case _:NumberFormatException =>
            try {
              valueString.toDouble
              model.DOUBLE
            }
            catch {
              case _:NumberFormatException => throw new AssertionError()
            }
        }
    }
  }
}

object ModelBuilder {
  import java.io.File
  import com.typesafe.config.ConfigFactory

  // $COVERAGE-OFF$
  def main(args: Array[String]): Unit = {
    val filename = args(0)
    val file = new File(filename)
    val src = io.Source.fromFile(file).mkString.trim
    println("source:\n  |" + src.replaceAll("\n", "\n  |"))
    val config = ConfigFactory.parseString(src).resolve()

    val objectType = new ModelBuilder().fromConfig(config)
    println(s"objectType:")
    println(model.util.format(objectType))
  }
  // $COVERAGE-ON$
}
