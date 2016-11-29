package tscfg.example

case class ScalaIssue15Cfg(
  positions : scala.collection.immutable.List[ScalaIssue15Cfg.Positions$Elm]
)

object ScalaIssue15Cfg {
  case class Positions$Elm(
    numbers   : scala.collection.immutable.List[scala.Int],
    positions : scala.collection.immutable.List[scala.collection.immutable.List[Positions$Elm.Positions$2$Elm]]
  )

  object Positions$Elm {
    case class Positions$2$Elm(
      other : scala.Int,
      stuff : java.lang.String
    )

    object Positions$2$Elm {
      def apply(c: com.typesafe.config.Config): Positions$2$Elm = {
        Positions$2$Elm(
          c.getInt("other"),
          c.getString("stuff")
        )
      }
    }


    def apply(c: com.typesafe.config.Config): Positions$Elm = {
      Positions$Elm(
        $list$int(c.getList("numbers")),
        $list$listPositions$2$Elm(c.getList("positions"))
      )
    }

    private def $list$listPositions$2$Elm(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[scala.collection.immutable.List[Positions$2$Elm]] = {
      import scala.collection.JavaConversions._
      cl.map(cv => $listPositions$2$Elm(cv.asInstanceOf[com.typesafe.config.ConfigList])).toList
    }
    private def $listPositions$2$Elm(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[Positions$2$Elm] = {
      import scala.collection.JavaConversions._
      cl.map(cv => Positions$2$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
    }
  }

  def apply(c: com.typesafe.config.Config): ScalaIssue15Cfg = {
    ScalaIssue15Cfg(
      $listPositions$Elm(c.getList("positions"))
    )
  }

  private def $expE(cv:com.typesafe.config.ConfigValue, exp:java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(cv.origin.lineNumber +
      ": expecting: " + exp + " got: " +
      (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u))
  }
  private def $int(cv:com.typesafe.config.ConfigValue): scala.Int = {
    val u: Any = cv.unwrapped
    if ((cv.valueType != com.typesafe.config.ConfigValueType.NUMBER)
      || !u.isInstanceOf[Integer]) throw $expE(cv, "integer")
    u.asInstanceOf[Integer]
  }
  private def $list$int(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[scala.Int] = {
    import scala.collection.JavaConversions._
    cl.map(cv => $int(cv)).toList
  }
  private def $listPositions$Elm(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[Positions$Elm] = {
    import scala.collection.JavaConversions._
    cl.map(cv => Positions$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
  }
}

