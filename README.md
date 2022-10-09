[![Build Status](https://github.com/carueda/tscfg/actions/workflows/ci.yml/badge.svg)](https://github.com/carueda/tscfg/actions)
[![Coverage Status](https://coveralls.io/repos/github/carueda/tscfg/badge.svg?branch=main)](https://coveralls.io/github/carueda/tscfg?branch=main)
[![Known Vulnerabilities](https://snyk.io/test/github/carueda/tscfg/badge.svg?targetFile=build.sbt)](https://snyk.io/test/github/carueda/tscfg?targetFile=build.sbt)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://makeapullrequest.com/)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)


## tscfg

tscfg is a command line tool that takes a configuration specification
parseable by [Typesafe Config](https://github.com/typesafehub/config)
and generates all the boiler-plate to make the definitions
available in type-safe, immutable objects
(POJOs/records for Java, case classes for Scala).

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
  - [shared objects](#shared-objects)
    - [shared object inheritance](#shared-object-inheritance)
  - [enum](#enum)
- [configuration template](#configuration-template)
- [FAQ](#faq)
- [tests](#tests)


### status

The tool supports all types handled by Typesafe Config
(string, int, long, double, boolean, duration, size-in-bytes, list, object)
and has great test coverage.
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

**But this wouldn't be flexible enough!**
To allow the specification of
**required fields**,
**explicit types**,
and **default values**,
a string with a simple syntax as follows can be used
(illustrated with the integer type):

| field spec | meaning | java type / default | scala type / default
|---|---|---|---|
| a: "int"          | required integer | `int` / no default | `Int` / no default
| a: "int &vert; 3" | optional integer with default value `3` | `int` / `3` | `Int`/ `3`
| a: "int?"         | optional integer | `Integer` / `null`(*) | `Option[Int]` / `None`

> **NOTE**
> - (*) You can use the `--java:optionals` flag to generate `Optional<T>` instead of `null`.
> - The type syntax is still subject to change.

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

You will need a JRE 8+ and the latest fat JAR (tscfg-x.y.z.jar)
from the [releases](https://github.com/carueda/tscfg/releases).

> Or run `sbt assembly` (or `sbt ++2.13.7 assembly`)
> under a clone of this repo to generate the fat jar.

```
$ java -jar tscfg-x.y.z.jar

tscfg x.y.z
Usage:  tscfg.Main --spec inputFile [options]
Options (default):
  --pn <packageName>                                     (tscfg.example)
  --cn <className>                                       (ExampleCfg)
  --dd <destDir>                                         (/tmp if existing or OS dependent temp dir)
  --java                generate java code               (the default)
  --j7                  generate code for java <= 7      (>= 8)
  --java:getters        generate getters (see #31)       (false)
  --java:records        generate records                 (false)
  --java:optionals      use optionals                    (false)
  --scala               generate scala code              (java)
  --scala:2.12          generate code for scala 2.12     (2.13)
  --scala:bt            use backticks (see #30)          (false)
  --durations           use java.time.Duration           (false)
  --all-required        assume all properties are required (see #47)
  --tpl <filename>      generate config template         (no default)
  --tpl.ind <string>    template indentation string      ("  ")
  --tpl.cp <string>     prefix for template comments     ("##")
  --withoutTimestamp    generate header w/out timestamp  (false)
Output is written to $destDir/$className.ext
```

So, to generate the Java class `tscfg.example.ExampleCfg` with the example above
saved in a file `example.spec.conf`, you can run:

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

1. Create the tscfg generated wrapper:

    ```java
    ExampleCfg cfg = new ExampleCfg(tsConfig);
    ```
    which will make all verifications about required settings and associated types.
    In particular, as is typical with Config use, an exception will be thrown if this verification fails.

2. Then, while enjoying full type safety and the code completion and navigation capabilities of your editor or IDE:

    ```java
    String path    = cfg.endpoint.path;
    Integer serial = cfg.endpoint.serial;
    int port       = cfg.endpoint.interface_.port;
    ```

An object reference will never be `null` (or `Optional.empty()`) (`None` in Scala) if the corresponding field is required according to
the specification. It will only be `null` (or `Optional.empty()`) (`None` in Scala) if it is marked optional with no default value and
has been omitted in the input configuration.

With this [example spec](https://github.com/carueda/tscfg/blob/main/src/main/tscfg/example/example.spec.conf),
the generated Java code looks [like this](https://github.com/carueda/tscfg/blob/main/src/main/java/tscfg/example/JavaExampleCfg.java)
and an example of use [like this](https://github.com/carueda/tscfg/blob/main/src/main/java/tscfg/example/JavaUse.java).

For Scala
the generated code looks [like this](https://github.com/carueda/tscfg/blob/main/src/main/scala/tscfg/example/ScalaExampleCfg.scala)
and an example of use [like this](https://github.com/carueda/tscfg/blob/main/src/main/scala/tscfg/example/scalaUse.scala).

## supported types

### basic types

The following basic types are supported:

| type in spec                         | java type:<br /> req / opt  | scala type:<br /> req / opt
|--------------------------------------|--------------------------|---------------------------------
| `string`                             | `String`   / `String`    | `String`   / `Option[String]`
| `int`                                | `int`      / `Integer`   | `Int`      / `Option[Int]`
| `long`                               | `long`     / `Long`      | `Long`     / `Option[Long]`
| `double`                             | `double`   / `Double`    | `Double`   / `Option[Double]`
| `boolean`                            | `boolean`  / `Boolean`   | `Boolean`  / `Option[Boolean]`
| `size`                               | `long`     / `Long`      | `Long`     / `Option[Long]`
| `duration`                           | `long`     / `Long`      | `Long`     / `Option[Long]`
| `duration` (using `--durations` flag) | `Duration` / `Duration`  | `Duration` / `Option[Duration]`

> **NOTE**
> - please read `Optional<T>` instead of the `T` values in the
    java "opt" column above if using the `--java:optionals` flag.
> - using the `--durations` flag, `java.time.Duration` is used instead of `long` / `Long`. See [durations](#durations) for further information.


#### size-in-bytes

The `size` type corresponds to the
[size-in-bytes formats](https://github.com/typesafehub/config/blob/main/HOCON.md#size-in-bytes-format)
supported by the Typesafe library.
See [#23](https://github.com/carueda/tscfg/issues/23) for various examples.

> NOTE: As of 0.0.984, a setting with a default value like
> `memory: 50G` (or `memory: "50G"`) will no longer be inferred as with `size` type, but just as a string
> (with default value `"50G"`).
> For the size type effect, you will need to be explicit: `memory: "size | 50G"`.
> See [#42](https://github.com/carueda/tscfg/issues/42) and [#41](https://github.com/carueda/tscfg/issues/41).

#### durations

A duration type can be further qualified with a suffix consisting of a colon
and a desired time unit for the reported value.
For example, with the type `"duration:day"`, the reported long value will be in day units,
with conversion automatically performed if the actual configuration value is given in
any other unit as supported by Typesafe Config according to the
[duration format](https://github.com/typesafehub/config/blob/main/HOCON.md#duration-format).

[A more complete example](https://github.com/carueda/tscfg/blob/main/src/main/tscfg/example/duration.spec.conf)
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
Using the `--durations` flag, the reported value will be a `java.time.Duration` instead of a `long` / `Long` and the suffix will be ignored:
`"duration:hours | 3day"` is `java.time.Duration.ofDays(3)` if value is missing or whatever is provided converted to a `java.time.Duration`


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
value will be `null` (or `Optional.empty()`) (`None` in Scala) when the corresponding actual entry is missing in
a given configuration instance.

### shared objects

Since version 0.9.94 we started adding support for "shared objects" (#54),
a feature that has been enhanced in later versions.
This is exercised by using the `@define` annotation:

```properties
#@define
Struct {
  c: string
  d: int
}

example {
  a: Struct
  b: [ Struct ]
}
```

In this example, the annotation will only generate the definition of the
corresponding class `Struct` in the wrapper but not the member of that
type itself. Then, the type can be referenced for other definitions.

> Note: The `@define` annotation is only supported for objects and enumerations (see below),
> not for a basic types or lists.

#### shared object inheritance

As of 0.9.98 shared objects now support simple inheritance by an abstract superclass. The following syntax can be used
to define a simple inheritance:

```properties
#@define abstract
BaseStruct {
  a: [string]
  b: double
}

#@define extends BaseStruct
ChildStruct {
  c: string
  d: string
}

example {
  child: ChildStruct
}
```

In this example, the annotation will generate an abstract class definition of the ``BaseStruct`` as well as a definition
of ``ChildStruct`` which extends ``BaseStruct``. This inheritance structure simplifies processing of the config with
structs that have multiple common fields.

Only leaf members of the inheritance tree may be instantiable instances, all other shared objects in between have to be
abstract classes.
The following are valid definition comments:

| Comment                          | Meaning                                                                              |
| :------------------------------- | :----------------------------------------------------------------------------------- |
| `#@define abstract`              | Root of an inheritance tree                                                          |
| `#@define abstract extends Foo`   | Intermediate member of the tree, that extends the shared object `Foo`               |
| `#@define extends Bla`           | Leaf member of the inheritance tree, that extends the (abstract) shared object `Bla` |


#### known issues with shared objects

- the current support for shared objects as field types in another shared object is unstable and not yet fully supported
- empty structs without any fields are treated as strings. Hence, having a child struct without new fields in addition
to its superclass is not supported yet


## enum

Enumerations can also be defined and this is done through the
`@define enum` annotation:

```properties
#@define enum
FruitType = [apple, banana, pineapple]

fruit: FruitType

someFruits: [FruitType]

other: {
  aFruit: FruitType
}
```

As with other uses of `@define`, the enumeration annotation will only generate
the enumeration type itself, but the associated name can then be used for other
field definitions in your configuration schema.

The type defined in the example above basically gets translated into Java and Scala
as follows:

```java
public enum FruitType {
    apple,
    banana,
    pineapple;
}
```

```scala
sealed trait FruitType
object FruitType {
  object apple     extends FruitType
  object banana    extends FruitType
  object pineapple extends FruitType
}
```

## configuration template

tscfg can also generate user-oriented configuration templates from the given
configuration schema.
See [this wiki](https://github.com/carueda/tscfg/wiki/template-generation).


## FAQ

**But I can just access the configuration values directly with Typesafe Config
and even put them in my own classes**

Sure. However, as the number of configuration properties and levels of nesting increase,
the benefits of automated generation of the typesafe, immutable objects,
along with the centralized verification, shall become more apparent. All of this
–worth emphasizing– **based on an explicit *schema* for the configuration.**

**Any tscfg best practice for my development workflow?**

Please see [this wiki](https://github.com/carueda/tscfg/wiki/workflow).

**Is there any sbt plugin for tscfg that I can use as part of the build for my project?**

Not implemented yet. The issue is [#21](https://github.com/carueda/tscfg/issues/21)
if you want to add comments or reactions.  PRs are also welcome.

**Can tscfg generate `Optional<T>` for optional fields?**

Use the `--java:optionals` flag for enabling `Optional<T>` instead of `null` for optional fields in java.

**What happened with the generated `toString` method?**

We think it's more flexible to let client code decide how to render configuration instances
while also recognizing that very likely typical serialization libraries are already being
used in the application.
For example, the demo programs
[JavaUse](https://github.com/carueda/tscfg/blob/main/src/main/java/tscfg/example/JavaUse.java)
and [scalaUse](https://github.com/carueda/tscfg/blob/main/src/main/scala/tscfg/example/scalaUse.scala)
use [Gson](https://github.com/google/gson) and
[pprint](https://com-lihaoyi.github.io/PPrint/), respectively.
Although you could also use Typesafe Config itself for rendering purposes, you would be
using the original Typesafe Config parsed configuration object, so the rendering won't
necessarily be restricted only to the elements captured in the _configuration specification_
used by tscfg for the generated wrapper.

## tests

https://github.com/carueda/tscfg/tree/main/src/test/scala/tscfg
