import $ivy.`com.typesafe:config:1.3.0`
import com.typesafe.config._

//val nested1 = ConfigFactory.parseString(
//  """
//    |nested1 = [ [ {foo: "baz"} ] ]
//  """.stripMargin
//)
//println(s"nested1: $nested1")

val nested2 = ConfigFactory.parseString(
  """
    |nested2 = { a = [[[
    |  {
    |    caz = {
    |      bik = [[ {foo: "baz"} ]]
    |    }
    |  }
    |]]] }
  """.stripMargin
)
println(s"nested2: $nested2")
