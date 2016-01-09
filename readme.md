## what is this?

tscfg is a command line tool that takes a configuration specification 
parseable by [Typesafe Config](https://github.com/typesafehub/config)
and generates all the Java boiler-plate to make the definitions 
available in type safe POJOs.

Typesafe Config is used by the tool for the generation, and required for compilation 
and execution of the generated classes in your code.

### status

Note, this was just an interesting programing exercise `;)`.
It usable to some extent but can be improved in several ways
(for example, missing types include lists, durations, ..).
Feel free to play, fork, etc.


## configuration spec

It's a regular configuration file where each field value indicates the
corresponding type and, optionally, a default value, for example:

```properties
endpoint {
  # a String with default value "/"
  path = "string | /"                   
  
  # a String with default value "http://example.net"
  url = "String | http://example.net"   
  
  # a String with default value null
  name = "String?"     
                
  # an optional Integer with default value null
  serial = "int?"                   
                   
  interface {
    # an int with default value 8080
    port = "Int | 8080"                 
  }
}
```

## generation

The usage of the program:

```shell
$ java -jar tscfg-0.0.1.jar

USAGE: tscfg.Main inputFile [packageName [className [destDir]]]
Defaults:
  packageName:  mypackage
  className:    TsCfg
  destDir:      /tmp
Output is written to $destDir/$className.java
```

So, with the example above saved in `def.example.conf` we can run:

```shell
$ java -jar tscfg-0.0.1.jar def.example.conf

parsing: def.example.conf
generating: /tmp/TsCfg.java
```

to generate the class `mypackage.TsCfg`.

## using the generated pojos

```java
File configFile = new File("my.conf");

// usual Typesafe Config mechanism to load the file
Config tsConfig = ConfigFactory.parseFile(configFile).resolve();
```

To access the configuration fields, instead of 
(for brevity, omitting `.hasPath` checks for the fields intended to be optional):
```java
String path    = tsConfig.getString("endpoint.path");
Integer serial = tsConfig.getString("endpoint.serial");
int port       = tsConfig.getInt("endpoint.interface.port");
```

you can:
```java
TsCfg cfg = new TsCfg(tsConfig);
```
which will make all verifications about required settings and associated types.

then, while enjoying full type safety and the code completion capability of your IDE:
```java
String path    = cfg.endpoint.path;
Integer serial = cfg.endpoint.serial;
int port       = cfg.endpoint.interface_.port;
```

Reference objects will never be null if the corresponding field is required according to
the specification. It will only be null if it is marked optional with no default value and
has been omitted in the input configuration. 