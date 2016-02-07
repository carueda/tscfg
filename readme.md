[![Build Status](https://travis-ci.org/carueda/tscfg.svg?branch=master)](https://travis-ci.org/carueda/tscfg)

## tscfg

tscfg is a command line tool that takes a configuration specification 
parseable by [Typesafe Config](https://github.com/typesafehub/config)
and generates all the Java boiler-plate to make the definitions 
available in type safe, immutable objects.

Typesafe Config is used by the tool for the generation, and required for compilation 
and execution of the generated classes in your code.

### status

This tool was motivated by the lack of something similar to 
[PureConfig](https://github.com/melrief/pureconfig)
but for java (please point me to any that I may have missed!)
It's already usable but can be improved in several ways
(for example, missing types include lists, durations).
Feel free to play, fork, enter issues, submit PRs, etc.

Avoiding boiler-plate is in general much easier in Scala than in Java!
(PureConfig, for example, uses case classes for the configuration spec).

In tscfg's approach, the configuration spec is itself also captured in a configuration file
so the familiar syntax/format (as supported by Typesafe Config) is still used.
With this input the tool generates corresponding POJO classes. 

But, you may wonder, why the trouble if the properties can simply be accessed with Typesafe Config directly? -- see FAQ below.

> As of v0.1.5, Scala output can also be generated.

## configuration spec

Any source parseable by Typesafe Config can be used as input to the tscfg generator.
For example, from this configuration:

```properties
service {
  url = "http://example.net/rest"
  poolSize = 32
  debug = true
  factor = 0.75
}
```

tscfg will generate the following immutable class
(excluding constructors and other methods):

```java
public class Cfg {
  public final Service service;
  public static class Service {
    public final String url;
    public final int poolSize;
    public final boolean debug;
    public final double factor;
  }
}
```

The tool determines the type of each field according to the given value
in the input configuration.
Used in this way, however, all fields are required. 

To allow the specification of optional fields and default values, 
while also indicating the type,
a string with a simple syntax as follows can be used
(illustrated below with the integer type):

| field spec | meaning | java type/default | scala type/default
|---|---|---|---|
| `name = "int"`  | required integer | `int` | `Int`
| `name = "int|3"`  | optional integer with default value `3` | `int` | `Int`
| `name = "int?"` | optional integer | `Integer/null` | `Option[Int]/None`

The following is a complete example exercising this mechanism.

```properties
endpoint {
  path = "string"
  url = "String | http://example.net"
  serial = "int?"
  interface {
    port = "int | 8080"
  }
}
```

Again, excluding constructors and other methods, this basically becomes 
the immutable class:

```java
public class ExampleCfg {
  public final Endpoint endpoint;
  public static class Endpoint {
    public final String path;
    public final String url;
    public final Integer serial;

    public final Interface interface_;
    public static class Interface {
      public final int port;
    }
  }
}
```

## generation

You will need a JRE 8 and the latest "fat" tscfg-x.y.z.jar from the [releases](https://github.com/carueda/tscfg/releases).

> Or run `sbt assembly` under a clone of this repo. 

```shell
$ java -jar tscfg-x.y.z.jar

tscfg x.y.z
Usage:  tscfg.Main --spec inputFile [options]
Options (default):
  --pn packageName  (tscfg.example)
  --cn className    (ExampleCfg)
  --dd destDir      (/tmp)
  --j7 generate code for java <= 7 (8)
  --scala generate scala code  (java)
Output is written to $destDir/$className.ext
```

So, to generate class `tscfg.example.ExampleCfg` with the example above 
saved in `def.example.conf`, we can run:

```shell
$ java -jar tscfg-x.y.z.jar --spec def.example.conf

parsing: def.example.conf
generating: /tmp/ExampleCfg.java
```

## configuration access

Usual Typesafe Config mechanism to load the file, for example:

```java
File configFile = new File("my.conf");
Config tsConfig = ConfigFactory.parseFile(configFile).resolve();
```

Now, to access the configuration fields, instead of:
```java
Config endpoint = tsConfig.getConfig("endpoint");
String path    = endpoint.getString("path");
Integer serial = endpoint.hasPathOrNull("serial") ? endpoint.getInt("serial") : null;
int port       = endpoint.hasPathOrNull("port")   ? endpoint.getInt("interface.port") : 8080;
```

you can:
```java
ExampleCfg cfg = new ExampleCfg(tsConfig);
```
which will make all verifications about required settings and associated types. 
In particular, as is typical with Config use, an exception will be thrown is this verification fails.

Then, while enjoying full type safety and the code completion and navigation capabilities of your IDE:
```java
String path    = cfg.endpoint.path;
Integer serial = cfg.endpoint.serial;
int port       = cfg.endpoint.interface_.port;
```

> note that java keywords are appended "_".

An object reference will never be null if the corresponding field is required according to
the specification. It will only be null if it is marked optional with no default value and
has been omitted in the input configuration.
 
The generated code looks [like this](https://github.com/carueda/tscfg/blob/master/src/main/java/tscfg/example/ExampleCfg.java). 
Example of use [here](https://github.com/carueda/tscfg/blob/master/src/main/java/tscfg/example/Use.java).

## FAQ

**Why the trouble? -- I can just access the configuration values directly with Typesafe Config 
and put them in my own classes**
  
Sure. However, as the number of configuration properties and levels of nesting increase,
the benefits of automated generation of the typesafe, immutable objects, 
along with the centralized verification,
may become more apparent.

**Could tscfg generate `Optional<T>` by default for optional fields?**

Yes, and it would be rather straightforward to add. Feel free to contribute!
 
**What about generating for Scala?**

Yes, it's there too. Use the `--scala` option and case classes will be generated.
Optional fields are generated with type `Option[T]`.
Looks [like this](https://github.com/carueda/tscfg/blob/master/src/main/scala/tscfg/example/ScalaExampleCfg.scala). 
Example of use [here](https://github.com/carueda/tscfg/blob/master/src/main/scala/tscfg/example/scalaUse.scala).
  

## tests

### java

- [ExampleSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/example/ExampleSpec.scala) 
- [JavaAccessorSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/JavaAccessorSpec.scala)
- [Java7AccessorSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/Java7AccessorSpec.scala)
- [javaIdentifierSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/JavaIdentifierSpec.scala)

### scala

- [ScalaExampleSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/example/ScalaExampleSpec.scala) 
- [scalaAccessorSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/ScalaAccessorSpec.scala)
- [scalaIdentifierSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/scalaIdentifierSpec.scala)
