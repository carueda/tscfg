package tscfg

import org.specs2.mutable.Specification

class modelSpec extends Specification {
  import model._
  import model.implicits._
  import model.durations._

  "basic ObjectType construction" should {
    val objectType = ObjectType(
      "positions" := "Position information" % ListType(ObjectType(
        "lat" := DOUBLE,
        "lon" := DOUBLE,
        "attrs" := ListType(ObjectType(
          "b" := BOOLEAN,
          "d" := DURATION(hour)
        ))
      )),
      "baz" := "comments for baz..." % ~ObjectType(
        "b" := ~ STRING | "some value",
        "a" := LONG | "99999999",
        "i" := "i, an integer" % INTEGER
      ),
      "xyz" := "other string xyz" % ~STRING
    )

    "build empty ObjectType" in {
      val baz = objectType.members("baz")
      baz.optional must beTrue
      baz.comments must beSome("comments for baz...")
    }
  }

  "repeated name" should {
    def a = ObjectType(
      "foo" := STRING,
      "baz" := LONG,
      "foo" := DOUBLE
    )

    "throw RuntimeException" in {
      a must throwA[RuntimeException]
    }
  }
}
