[![Build Status](https://travis-ci.org/carueda/tscfg.svg?branch=master)](https://travis-ci.org/carueda/tscfg)
[![Coverage Status](https://coveralls.io/repos/github/carueda/tscfg/badge.svg?branch=master)](https://coveralls.io/github/carueda/tscfg?branch=master)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](http://makeapullrequest.com)

## tscfg

tscfg is a command line tool that takes a configuration specification 
parseable by [Typesafe Config](https://github.com/typesafehub/config)
and generates all the boiler-plate to make the definitions 
available in type safe, immutable objects 
(POJOs for Java, case classes for Scala).

The generated code only depends on the Typesafe Config library.  


- [status](#status)
- [configuration spec](#configuration-spec)
- [running tscfg](#running-tscfg)
  - [maven plugin](#maven-plugin)
- [configuration access](#configuration-access)
- [supported types](#supported-types)
  - [basic types](#basic-types)
  - [size-in-bytes](#size-in-bytes)
  - [durations](#durations)
  - [list type](#list-type)
  - [object type](#object-type)
  - [optional object or list](#optional-object-or-list)
- [configuration template](#configuration-template)
- [FAQ](#faq)
- [tests](#tests)


### status

The tool supports all types handled by Typesafe Config 
(string, int, long, double, boolean, duration, size-in-bytes, list)
and has good test coverage.
Possible improvements include a more standard command line interface,
a proper tscfg library,
and perhaps a revision of the syntax for types.
Feel free to fork, enter issues/reactions, submit PRs, etc.


## configuration spec

In tscfg's approach, the configuration spec itself is any source parseable by Typesafe Config,
so the familiar syntax/format and loading mechanisms are used.

For example, from this configuration:

```properties
service {
  url = "http://example.net/rest"
  poolSize = 32
  debug = true
  factor = 0.75
}
```

tscfg will generate the following (constructors and other methods omitted):
 
- Java:

    ```java
    public class Cfg {
      public final Service service;
      public static class Service {
        public final boolean debug;
        public final double factor;
        public final int poolSize;
        public final String url;
      }
    }
    ```
    
    Nesting of configuration properties is captured via inner static classes.

- Scala:

    ```scala
    case class Cfg(
      service : Cfg.Service
    )
    object Cfg {
      case class Service(
        debug : Boolean,
        factor : Double,
        poolSize : Int,
        url : String
      )
    }
    ```
    
    Nesting of configuration properties is captured via nested companion objects.

The tool determines the type of each field according to the given value
in the input configuration.
Used in this way, all fields are considered optional, with the given value as the default. 

But this wouldn't be flexible enough!
To allow the specification of
**required fields**,
**explicit types**,
and **default values**,
a string with a simple syntax as follows can be used
(illustrated with the integer type):

| field spec | meaning | java type / default | scala type / default
|---|---|---|---|
| "int"          | required integer | `int` / no default | `Int` / no default
| "int &vert; 3" | optional integer with default value `3` | `int` / `3` | `Int`/ `3`
| "int?"         | optional integer | `Integer` / `null` | `Option[Int]` / `None`

> The type syntax is still subject to change.

The following is a complete example exercising this mechanism.

```properties
endpoint {
  path: "string"
  url: "String | http://example.net"
  serial: "int?"
  interface {
    port: "int | 8080"
  }
}
```

For Java, this basically becomes the immutable class:

```java
public class JavaExampleCfg {
  public final Endpoint endpoint;

  public static class Endpoint {
    public final int intReq;
    public final Interface_ interface_;
    public final String path;
    public final Integer serial;
    public final String url;

    public static class Interface_ {
      public final int port;
      public final String type;
    }
  }
}
```

And for Scala:

```scala
case class ScalaExampleCfg(
  endpoint : ScalaExampleCfg.Endpoint
)

object ScalaExampleCfg {
  case class Endpoint(
    intReq    : Int,
    interface : Endpoint.Interface,
    path      : String,
    serial    : Option[Int],
    url       : String
  )

  object Endpoint {
    case class Interface(
      port   : Int,
      `type` : Option[String]
    )
  }
}
```

## running tscfg

You will need a JRE 8 and the latest fat JAR (tscfg-x.y.z.jar)
from the [releases](https://github.com/carueda/tscfg/releases).

> Or run `sbt assembly` under a clone of this repo to generate the fat jar. 

```shell
$ java -jar tscfg-x.y.z.jar

tscfg x.y.z
Usage:  tscfg.Main --spec inputFile [options]
Options (default):
  --pn <packageName>                                     (tscfg.example)
  --cn <className>                                       (ExampleCfg)
  --dd <destDir>                                         (/tmp)
  --j7                  generate code for java <= 7      (8)
  --scala               generate scala code              (java)
  --scala:`             use backsticks                   (false)
  --java                generate java code               (the default)
  --tpl <filename>      generate config template         (no default)
  --tpl.ind <string>    template indentation string      ("  ")
  --tpl.cp <string>     prefix for template comments     ("##")
Output is written to $destDir/$className.ext
```

So, to generate the Java class `tscfg.example.ExampleCfg` with the example above
saved in a file `example.spec.conf`, we can run:

```shell
$ java -jar tscfg-x.y.z.jar --spec example.spec.conf

parsing: example.spec.conf
generating: /tmp/ExampleCfg.java
```

### maven plugin

Please see [tscfg-maven-plugin](https://github.com/timvlaer/tscfg-maven-plugin).
Thanks [@timvlaer](https://github.com/timvlaer)!

## configuration access

Access to a configuration instance is via usual Typesafe Config mechanism
as appropriate for your application, for example, to load the default configuration:

```java
Config tsConfig = ConfigFactory.load().resolve()
```

or from a given file:

```java
Config tsConfig = ConfigFactory.parseFile(new File("my.conf")).resolve();
```

Now, to access the configuration fields, instead of, for example:
```java
Config endpoint = tsConfig.getConfig("endpoint");
String path    = endpoint.getString("path");
Integer serial = endpoint.hasPathOrNull("serial") ? endpoint.getInt("serial") : null;
int port       = endpoint.hasPathOrNull("port")   ? endpoint.getInt("interface.port") : 8080;
```

you can:

1. create the tscfg generated wrapper:
 
    ```java
    ExampleCfg cfg = new ExampleCfg(tsConfig);
    ```
    which will make all verifications about required settings and associated types. 
    In particular, as is typical with Config use, an exception will be thrown if this verification fails.

2. then, while enjoying full type safety and the code completion and navigation capabilities of your IDE:

    ```java
    String path    = cfg.endpoint.path;
    Integer serial = cfg.endpoint.serial;
    int port       = cfg.endpoint.interface_.port;
    ```

An object reference will never be `null` (`None` in Scala) if the corresponding field is required according to
the specification. It will only be `null` (`None`) if it is marked optional with no default value and
has been omitted in the input configuration.
 
With this [example spec](https://github.com/carueda/tscfg/blob/master/src/main/tscfg/example/example.spec.conf),
the generated Java code looks [like this](https://github.com/carueda/tscfg/blob/master/src/main/java/tscfg/example/JavaExampleCfg.java)
and an example of use [like this](https://github.com/carueda/tscfg/blob/master/src/main/java/tscfg/example/JavaUse.java).

For Scala
the generated code looks [like this](https://github.com/carueda/tscfg/blob/master/src/main/scala/tscfg/example/ScalaExampleCfg.scala) 
and an example of use [like this](https://github.com/carueda/tscfg/blob/master/src/main/scala/tscfg/example/scalaUse.scala).

## supported types

### basic types

The following basic types are supported:

| type in spec  | java type:<br /> req / opt  | scala type:<br /> req / opt      
|---------------|---------------------|--------------------------
| `string`      | `String`  / `String`    | `String`  / `Option[String]`    
| `int`         | `int`     / `Integer`   | `Int`     / `Option[Int]`   
| `long`        | `long`    / `Long`      | `Long`    / `Option[Long]`      
| `double`      | `double`  / `Double`    | `Double`  / `Option[Double]`    
| `boolean`     | `boolean` / `Boolean`   | `Boolean` / `Option[Boolean]`   
| `size`        | `long`    / `Long`      | `Long`    / `Option[Long]`
| `duration`    | `long`    / `Long`      | `Long`    / `Option[Long]`
    

#### size-in-bytes

The `size` type corresponds to the 
[size-in-bytes formats](https://github.com/typesafehub/config/blob/master/HOCON.md#size-in-bytes-format) 
supported by the Typesafe library. 
See [#23](https://github.com/carueda/tscfg/issues/23) for various examples. 

#### durations

A duration type can be further qualified with a suffix consisting of a colon 
and a desired time unit for the reported value. 
For example, with the type `"duration:day"`, the reported long value will be in day units, 
with conversion automatically performed if the actual configuration value is given in 
any other unit as supported by Typesafe Config according to the 
[duration format](https://github.com/typesafehub/config/blob/master/HOCON.md#duration-format).

[A more complete example](https://github.com/carueda/tscfg/blob/master/src/main/tscfg/example/duration.spec.conf)
with some additional explanation:

```properties
durations {
  # optional duration; reported Long (Option[Long] in scala) is null (None) if value is missing
  # or whatever is provided converted to days
  days: "duration:day?"

  # required duration; reported long (Long) is whatever is provided
  # converted to hours
  hours: "duration:hour"

  # optional duration with default value;
  # reported long (Long) is in milliseconds, either 550,000 if value is missing
  # or whatever is provided converted to millis
  millis: "duration:ms | 550s"

  ...
}
```

### list type

With _t_ denoting a handled type, a list of elements of that type 
is denoted `[` _t_ `]`. The corresponding types in Java and Scala are:

| type in spec  | java type:<br /> req / opt  | scala type:<br /> req / opt      
|---------------|---------------------|--------------------------
| `[` _t_ `]`   | `List<T>` / `List<T>`   | `List[T]` / `Option[List[T]]`

where `T` is the corresponding translation of _t_ in the target language, with
`List<T>` corresponding to an unmodifiable list in Java, and
`List[T]` corresponding to an immutable list in Scala.

### object type

As seen in examples above, each object in the given configuration spec becomes a class.

It is of course possible to specify a field as a list of objects, for example:

```properties
positions: [
  {
    lat: double
    lon: double
  }
]
```

In Java this basically becomes:

```java
public class Cfg {
  public final java.util.List<Cfg.Positions$Elm> positions;

  public static class Positions$Elm {
    public final double lat;
    public final double lon;
  }
}
```

and in Scala:
```scala
case class Cfg(
  positions : List[Cfg.Positions$Elm]
)

object Cfg {
  case class Positions$Elm(
    lat : Double,
    lon : Double
  )
}
```

### optional object or list

An object or a list in the input configuration can be marked optional with
the `@optional` annotation (in a comment):

```properties
#@optional
email {
  server: string
  password: string
}

#@optional
reals: [ { foo: double } ]
```

In Scala this basically becomes:

```scala
case class Cfg(
  email : Option[Cfg.Email],
  reals : Option[List[Cfg.Reals$Elm]]
)

object Cfg {
  case class Email(
    password : String,
    server   : String
  )
  case class Reals$Elm(
    foo : Double
  )
}
```

As with basic types, the meaning of an optional object or list is that the corresponding 
value will be `null` (`None` in Scala) when the corresponding actual entry is missing in 
a given configuration instance.


## configuration template

tscfg can also generate user-oriented configuration templates from given config specs.
See [this wiki](https://github.com/carueda/tscfg/wiki/template-generation).


## FAQ

**But I can just access the configuration values directly with Typesafe Config 
and even put them in my own classes**
  
Sure. However, as the number of configuration properties and levels of nesting increase,
the benefits of automated generation of the typesafe, immutable objects, 
along with the centralized verification, shall become more apparent. All of this
–worth emphasizing– based on an explicit schema for the configuration.

**How does tscfg compare to possible alternatives?**

Please see [this wiki](https://github.com/carueda/tscfg/wiki/alternatives).

**Is there any sbt plugin for tscfg that I can use as part of the build for my project?**

Not implemented yet. The issue is [#21](https://github.com/carueda/tscfg/issues/21) 
if you want to add comments or reactions.  PRs are also welcome.

**Could tscfg generate `Optional<T>` for optional fields?**

Not implemented (yet). Want to contribute?

**What happened with the generated `toString` method?**

We think it's more flexible to let client code decide how to render configuration instances 
while also recognizing that very likely typical serialization libraries are already being 
used in the application.
For example, the demo programs 
[JavaUse](https://github.com/carueda/tscfg/blob/master/src/main/java/tscfg/example/JavaUse.java) 
and [scalaUse](https://github.com/carueda/tscfg/blob/master/src/main/scala/tscfg/example/scalaUse.scala) 
use [Gson](https://github.com/google/gson) and
[json4s](https://github.com/json4s/json4s), respectively.
Although you could also use Typesafe Config itself for rendering purposes, you would be 
using the original Typesafe Config parsed configuration object, so the rendering won't 
necessarily be restricted only to the elements captured in the _configuration specification_ 
used by tscfg for the generated wrapper.

## tests

https://github.com/carueda/tscfg/tree/master/src/test/scala/tscfg
