package tscfg.generators

import tscfg.generators.java.JavaGen
import tscfg.model
import tscfg.model.durations.hour

object JavaGenMain {
  // $COVERAGE-OFF$
  def main(args: Array[String]): Unit = {
    import tscfg.model._
    import tscfg.model.implicits._

    val objectType = ObjectType(
      "positions" := "Position information" % ListType(ListType(ObjectType(
        "lat" := DOUBLE | "35.1",
        "lon" := DOUBLE,
        "attrs" := ~ListType(BOOLEAN)
      ))),
      "durHr" := "A duration" % ~DURATION(hour),
      "foo" := STRING | """foo "val" etc """,
      "optStr" := ~STRING
    )
    println(model.util.format(objectType))

    val className = "JCfg"
    val genOpts = GenOpts("tscfg.example", className)
    val gen = new JavaGen(genOpts)
    val res = gen.generate(objectType)
    println(s"classNames = ${res.classNames}")
    println(s"fieldNames = ${res.fieldNames}")

    import _root_.java.io._
    val destFilename  = s"src/main/java/tscfg/example/JCfg.java"
    val destFile = new File(destFilename)
    val out = new PrintWriter(new FileWriter(destFile), true)
    out.println(res.code)
  }
  // $COVERAGE-ON$
}
