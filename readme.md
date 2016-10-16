[![Build Status](https://travis-ci.org/carueda/tscfg.svg?branch=master)](https://travis-ci.org/carueda/tscfg)
[![Coverage Status](https://coveralls.io/repos/github/carueda/tscfg/badge.svg?branch=master)](https://coveralls.io/github/carueda/tscfg?branch=master)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](http://makeapullrequest.com)

## tscfg

tscfg is a command line tool that takes a configuration specification 
parseable by [Typesafe Config](https://github.com/typesafehub/config)
and generates all the boiler-plate to make the definitions 
available in type safe, immutable objects 
(POJOs for Java, case classes for Scala).

Typesafe Config is used by the tool for the generation, and required for compilation 
and execution of the generated classes in your code.

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->


- [status](#status)
- [configuration spec](#configuration-spec)
- [running tscfg](#running-tscfg)
- [configuration access](#configuration-access)
- [supported types](#supported-types)
  - [durations](#durations)
- [template generation](#template-generation)
- [FAQ](#faq)
- [tests](#tests)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

### status

The tool is already pretty usable.
It supports a good part of the common types as supported by Typesafe Config 
(string, int, long, double, duration);
can generate configuration templates for end users;
and has good test coverage.
However, it's in general still work in progress and as time permits. 
Missing types include lists and bytes;
command line interface can be improved;
syntax for types is not stable yet.
Feel free fork, enter issues, submit PRs, etc.

Also, see FAQ below.


## configuration spec

In tscfg's approach, the configuration spec itself is any source parseable by Typesafe Config,
so the familiar syntax/format and loading mechanisms are used.

For example, from 
[this configuration](https://github.com/carueda/tscfg/blob/master/example/example0.spec.conf):

```properties
service {
  url = "http://example.net/rest"
  poolSize = 32
  debug = true
  factor = 0.75
}
```

tscfg will generate (constructors and other methods omitted):
 
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

But this wouldn't be flexible enough.
To allow the specification of
**required fields**,
**explicit types**,
and **default values**,
a string with a simple syntax as follows can be used
(illustrated with the integer type):

| field spec | meaning | java type / default | scala type / default
|---|---|---|---|
| `name = "int"`  | required integer | `int` / no default | `Int` / no default
| `name = "int|3"`  | optional integer with default value `3` | `int` / `3` | `Int`/ `3`
| `name = "int?"` | optional integer | `Integer` / `null` | `Option[Int]` / `None`

> The type syntax is still subject to change.

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

For Java, this basically becomes the immutable class:

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

> note that java keywords are appended "_".

## running tscfg

You will need a JRE 8 and the latest "fat" tscfg-x.y.z.jar from the [releases](https://github.com/carueda/tscfg/releases).

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
  --java                generate java code               (the default)
  --noToString          do not generate toString method  (generated)
  --toPropString        generate toPropString method     (not generated)
  --tpl <type> <filename>  generate configuration template;  <type> = base, local, all
Output is written to $destDir/$className.ext
```

So, to generate the Java class `tscfg.example.ExampleCfg` with the example above
saved in `def.example.conf`, we can run:

```shell
$ java -jar tscfg-x.y.z.jar --spec def.example.conf

parsing: def.example.conf
generating: /tmp/ExampleCfg.java
```

## configuration access

Access to a configuration instance is via usual Typesafe Config mechanism
as appropriate for your application, for example, to load the default configuration:

```java
Config tsConfig = ConfigFactory.load().resolve()
```

or from a given file:

```java
File configFile = new File("my.conf");
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
```java
ExampleCfg cfg = new ExampleCfg(tsConfig);
```
which will make all verifications about required settings and associated types. 
In particular, as is typical with Config use, an exception will be thrown if this verification fails.

Then, while enjoying full type safety and the code completion and navigation capabilities of your IDE:
```java
String path    = cfg.endpoint.path;
Integer serial = cfg.endpoint.serial;
int port       = cfg.endpoint.interface_.port;
```

An object reference will never be null (None in Scala) if the corresponding field is required according to
the specification. It will only be null (None) if it is marked optional with no default value and
has been omitted in the input configuration.
 
With this [example spec](https://github.com/carueda/tscfg/blob/master/example/def.example.conf),
the generated Java code looks [like this](https://github.com/carueda/tscfg/blob/master/src/main/java/tscfg/example/JavaExampleCfg.java)
and an example of use [like this](https://github.com/carueda/tscfg/blob/master/src/main/java/tscfg/example/JavaUse.java).

For Scala
the generated code looks [like this](https://github.com/carueda/tscfg/blob/master/src/main/scala/tscfg/example/ScalaExampleCfg.scala) 
and an example of use [like this](https://github.com/carueda/tscfg/blob/master/src/main/scala/tscfg/example/scalaUse.scala).

## supported types

With explicit field typing, the following base types are currently supported:

| type in spec  | java type:<br /> req / opt  | scala type:<br /> req / opt      
|---------------|---------------------|--------------------------
| `string`      | String  / String    | String  / Option[String]    
| `int`         | int     / Integer   | Int     / Option[Int]   
| `long`        | long    / Long      | Long    / Option[Long]      
| `double`      | double  / Double    | Double  / Option[Double]    
| `boolean`     | boolean / Boolean   | Boolean / Option[Boolean]   
| `duration`    | long    / Long      | Long    / Option[Long]
      
### durations

A duration type can be further qualified with a suffix consisting of a colon 
an a desired time unit for the reported value. 
For example, with the type `"duration:day"`, the reported long value will be in day units, 
with conversion automatically performed if the actual configuration value is given in 
any other unit as supported by Typesafe Config according to the 
[duration format](https://github.com/typesafehub/config/blob/master/HOCON.md#duration-format).

[A more complete example](https://github.com/carueda/tscfg/blob/master/example/example-duration.spec.conf)
with some additional explanation:

```properties
durations {
  # optional duration; reported Long (Option[Long] in scala) is null (None) if value is missing
  # or whatever is provided converted to days
  days = "duration:day?"

  # required duration; reported long (Long) is whatever is provided
  # converted to hours
  hours = "duration:hour"

  # optional duration with default value;
  # reported long (Long) is in milliseconds, either 550,000 if value is missing
  # or whatever is provided converted to millis
  millis = "duration:ms | 550s"
}
```

## template generation

Configuration templates are often a means to provide end users with a description of 
the properties that should be set (or that could be overwritten) 
for the proper operation of an application or library. 
Based on the template, end users will then enter the concrete settings that are appropriate.

On the other hand, configuration "specs," as used for tscfg generation, 
are mainly intended to be used by the developer of such application or library.

tscfg can also generate template configuration files from the given spec so the developer 
does not have to manually create/edit such templates.

For example, from the configuration spec:

```properties
# Some description of the endpoint section
#@optional
endpoint {
  # The path associated with the endpoint.
  # For example, "/home/foo/bar"
  path = "string"

  # Port where the endpoint service is running
  port = "int | 8080"

  # Email to send notifications to. If missing, no emails are sent.
  email = "string?"

  # Link in emails
  link = "string | http://example.net"

  value = 3.14
}
```

the generated template (with the `--tpl all` option) will look like:

```properties
# endpoint: Optional section.
# Some description of the endpoint section
endpoint {

  # email: Optional string.
  # Email to send notifications to. If missing, no emails are sent.
  #email =

  # link: Optional string. Default value "http://example.net".
  # Link in emails
  link = "http://example.net"

  # path: Required string.
  # The path associated with the endpoint.
  # For example, "/home/foo/bar"
  path =

  # port: Optional int. Default value 8080.
  # Port where the endpoint service is running
  port = 8080

  # value: Optional double. Default value 3.14.
  value = 3.14
}
```

> Annotations like `#@optional` are still preliminary.


## FAQ

**Why the trouble? -- I can just access the configuration values directly with Typesafe Config 
and put them in my own classes**
  
Sure. However, as the number of configuration properties and levels of nesting increase,
the benefits of automated generation of the typesafe, immutable objects, 
along with the centralized verification,
may become more apparent.

**Could tscfg generate `Optional<T>` by default for optional fields?**

Yes, but it's not implemented yet. Feel free to contribute!

## tests

### java

- [ExampleSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/example/JavaExampleSpec.scala)
- [JavaAccessorSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/JavaAccessorSpec.scala)
- [Java7AccessorSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/Java7AccessorSpec.scala)
- [javaIdentifierSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/JavaIdentifierSpec.scala)

### scala

- [ScalaExampleSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/example/ScalaExampleSpec.scala) 
- [scalaAccessorSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/ScalaAccessorSpec.scala)
- [scalaIdentifierSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/scalaIdentifierSpec.scala)

### Type

- [TypeSpec](https://github.com/carueda/tscfg/blob/master/src/test/scala/tscfg/TypeSpec.scala) 
