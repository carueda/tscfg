package tscfg.generators

object ScalaGenMain {
  // $COVERAGE-OFF$
  def main(args: Array[String]): Unit = {
    import tscfg.model._
    import tscfg.model.implicits._

    val objectType = ObjectType(
      "positions" := "Position information" % ListType(ListType(ObjectType(
        "lat" := DOUBLE,
        "lon" := DOUBLE,
        "attrs" := ~ListType(BOOLEAN)
      ))),
      "$_xyz" := "other string xyz" % ~STRING
    )
    val className = "Cfg"
    val genOpts = GenOpts("tscfg.example", className)
    val gen = new ScalaGen(genOpts)
    val res = gen.generate(objectType)
    println(s"classNames = ${res.classNames}")
    println(s"fieldNames = ${res.fieldNames}")

    import _root_.java.io._
    val destFilename  = s"src/main/scala/tscfg/example/Cfg.scala"
    val destFile = new File(destFilename)
    val out = new PrintWriter(new FileWriter(destFile), true)
    out.println(res.code)
  }
  // $COVERAGE-ON$
}
