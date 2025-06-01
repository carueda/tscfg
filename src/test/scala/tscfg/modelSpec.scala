package tscfg

import org.scalatest.wordspec.AnyWordSpec

class modelSpec extends AnyWordSpec {
  import model._
  import model.implicits._
  import model.durations._

  "basic ObjectType construction" should {
    val objectType = ObjectType(
      "positions" := "Position information" % ListType(
        ObjectType(
          "lat"   := DOUBLE,
          "lon"   := DOUBLE,
          "attrs" := ListType(
            ObjectType(
              "b" := BOOLEAN,
              "d" := DURATION(hour)
            )
          )
        )
      ),
      "baz" := "comments for baz..." % ~ObjectType(
        "b" := ~STRING | "some value",
        "a" := LONG | "99999999",
        "i" := "i, an integer" % INTEGER
      ),
      "xyz" := "other string xyz" % ~STRING
    )

    "build empty ObjectType" in {
      val baz = objectType.members("baz")
      assert(baz.optional)
      assert(baz.comments.contains("comments for baz..."))
    }
  }

  "repeated name" should {
    "throw exception" in {
      assertThrows[RuntimeException] {
        ObjectType(
          "foo" := STRING,
          "baz" := LONG,
          "foo" := DOUBLE
        )
      }
    }
  }
}
