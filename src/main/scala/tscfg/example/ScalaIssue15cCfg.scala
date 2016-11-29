package tscfg.example

case class ScalaIssue15cCfg(
  positions : scala.collection.immutable.List[ScalaIssue15cCfg.Positions$1$Elm],
  qaz       : ScalaIssue15cCfg.Qaz
)

object ScalaIssue15cCfg {
  case class Positions$1$Elm(
    attrs : scala.collection.immutable.List[scala.collection.immutable.List[Positions$1$Elm.Attrs$2$Elm]],
    lat   : scala.Double,
    lon   : scala.Double
  )

  object Positions$1$Elm {
    case class Attrs$2$Elm(
      foo : scala.Long
    )

    object Attrs$2$Elm {
      def apply(c: com.typesafe.config.Config): Attrs$2$Elm = {
        Attrs$2$Elm(
          c.getLong("foo")
        )
      }
    }

    def apply(c: com.typesafe.config.Config): Positions$1$Elm = {
      Positions$1$Elm(
        $list$listAttrs$2$Elm(c.getList("attrs")),
        c.getDouble("lat"),
        c.getDouble("lon")
      )
    }

    private def $list$listAttrs$2$Elm(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[scala.collection.immutable.List[Attrs$2$Elm]] = {
      import scala.collection.JavaConversions._
      cl.map(cv => $listAttrs$2$Elm(cv.asInstanceOf[com.typesafe.config.ConfigList])).toList
    }
    private def $listAttrs$2$Elm(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[Attrs$2$Elm] = {
      import scala.collection.JavaConversions._
      cl.map(cv => Attrs$2$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
    }
  }
  case class Qaz(
    aa : Qaz.Aa
  )

  object Qaz {
    case class Aa(
      bb : scala.collection.immutable.List[Aa.Bb$Elm]
    )

    object Aa {
      case class Bb$Elm(
        cc : java.lang.String
      )

      object Bb$Elm {
        def apply(c: com.typesafe.config.Config): Bb$Elm = {
          Bb$Elm(
            c.getString("cc")
          )
        }
      }
      def apply(c: com.typesafe.config.Config): Aa = {
        Aa(
          $listBb$Elm(c.getList("bb"))
        )
      }

      private def $listBb$Elm(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[Bb$Elm] = {
        import scala.collection.JavaConversions._
        cl.map(cv => Bb$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
      }
    }
    def apply(c: com.typesafe.config.Config): Qaz = {
      Qaz(
        Aa(c.getConfig("aa"))
      )
    }
  }
  def apply(c: com.typesafe.config.Config): ScalaIssue15cCfg = {
    ScalaIssue15cCfg(
      $listPositions$1$Elm(c.getList("positions")),
      Qaz(c.getConfig("qaz"))
    )
  }

  private def $listPositions$1$Elm(cl:com.typesafe.config.ConfigList): scala.collection.immutable.List[Positions$1$Elm] = {
    import scala.collection.JavaConversions._
    cl.map(cv => Positions$1$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
  }
}
