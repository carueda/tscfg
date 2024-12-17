2024-12

- preliminary partial impl of #312 "Reflect doc comments in generated code"
    - only for scala at the moment, and only scaladoc for object, but not `@param`s yet.
    - for now, this is an opt-in feature with `--doc-comments` flag,
      but this should be the default behavior.

1.1.5

- Fixed #309 "Empty object not reflected in generated wrapper."
  `Struct` construction now based on `Config.root.entrySet` (instead of `Config.entrySet`).
  For model construction, two passes are now performed to have all `@define`s captured for resolution.
  This also resolves the closely related issue of wrongly resolving a `SomeName` as string when it is
  actually a `@define`. The two passes are in particular to address types like `[SomeName]`, that is,
  when the name is referenced indirectly via some container type.

2024-08

- 1.1.4: maintenance (incorporate some generated java files (related with records) previously skipped for CI)
- 1.1.3: maintenance (dependency updates, code cleanup)
- 1.1.2: published
- 1.1.0:  dropped support for scala 2.12 and java 7.

- maintenance:
    - dependency updates, more cross-builds, expanded ci; cleanup most compile warnings.

2022-10 - 1.0.2

- scala 2.13.9

2022-10 - 1.0.1

- fix #180 "Wrapped Data Types"
- reapply some lib updates

2022-07

1.0.0

- bump version to 1.0.0; No changes in functionality -- https://github.com/carueda/tscfg/issues/155
- some dependency updates

2022-01

0.9.998

- bumped versions for scala, scribe and pprint
- adjusted defaultDestDir: "/tmp" if already existing; else as given by "java.io.tmpdir" property.
  (Thanks @ckittl for motivating this adjustment.)

0.9.997

- use sbt-buildinfo

- fix #125 "Getter is created for shared object definition in Java"

- Re #127: just added tests as I was not able to reproduce this issue
  (maybe I had some stale stuff under my clone... weird)

- fix #124 optional references to shared objects
  (thanks @sebastian-peter)

2021-11 - 0.9.996

- 0.9.996 release

- gen4test: generate only for specific language depending on given options.
  NOTE: not all CmdLineOpts are handled via options from input file.

- No more a common, singleton Namespace object to make handling more self-contained and
  less error-prone, in fact, avoiding the occasional race condition among test suites.
- more complete handling of `--java:records`

2021-11 - 0.9.995

- resolve #73 "Ability to extend or implement external type
  that is an interface or class with no-arg, default constructor"
  - use "!" prefix to indicate an external extension name
  - Besides `extends` now also `implements` accepted
  - Examples:

      - `#@define abstract extends !java.lang.Object`
      - `#@define abstract implements !java.io.Serializable`

- use pprint, remove json4s
- 0.9.994 release
- core handling in place for `--java:records` #75.
  This along with needed scala 2.13.7 to support record compilation.

- re #69 "Use consistent formatter for the generated code"
  actually decided to remove the extra libraries as they were causing more friction
  than benefit (in particular re modernization of this tool), and users can actually
  do any post-generation formatting as needed.
  For this project itself, we just use the scalafmt plugin as a common dependency.
  We could also use it on generated files as needed/convenient.

2021-10

- re #75: initial --java:records handling

- some cleanup in generated java code related with unneeded
  `final .. parentPath = ""` in root objects;  TODO complete.

- avoid migration warning in generated scala code
- remove travis
- adjustments to build project with scala3 (3.1.0)

- use scalatest (simpler and scala3-ready)
- prelims for eventual migration to scala3
- fix setup for tests

2021-08 - 0.9.994

- hmm, sort of revert to travis (will still work?) due to coveralls issue
  https://github.com/scoverage/sbt-coveralls/issues/126
- use github actions for ci
- upgrade coverage deps

2021-08 - 0.9.992

- use a newer sbt version
  - fix sbt 0.12->0.13 warnings
  - use sbt 1.5.5, while upgrading various build/release dependencies
  - all good ... but `+sonatypeBundleRelease` is failing `:(`
  - retrying with incremented versions 0.9.991 ... 993

2021-07 - 0.9.986

- fix #74 "Hardcoded example code in ScalaGen.scala" 😂  🍌🍎🍍

2021-03 - 0.9.985

- env var `NO_FORMATTER` convenience to skip the standard formatting of
  generated code while speeding up testing a bit

- adjust template generation
  TODO remove this feature?

2021-03 - 0.9.984

- resolve #42 "Size-in-bytes should be explicitly indicated in config spec"
  - updated TS Config to 1.4.1, as a general measure, but also to quickly check about the
    Quoted vs. Unquoted "issue" demonstrated with `> runMain misc.QuotedUnquoted` => No changes.

2021-03 - 0.9.983

- fix #62 "Ability to set enums in config"
  - completed handling at first level
  - in general, generated scala class names are now fully qualified

2021-03 - 0.9.982

fix #71 "Two shared objects leading to string conversion"

- this was a regression; model built incorrectly:

        > runMain tscfg.ModelBuilder src/main/tscfg/example/issue71.spec.conf
        ...
        ModelBuilderResult:
        {
            example: {
                a: optional STRING default='Shared'
                b: [ STRING ]
                c: [ STRING ]
            }
        }

    All the `@define`s lost!

- trying the same with commit eccee9a16 creates the model as expected
- did a general revision of the model building code
- simplified handling of the "linearization" of extends given, in particular,
  that an 'extends' construct is for only one direct parent class (not multiple)
- ObjectRefType now more flexible, just in terms of strings, so it can
  be created more directly (e.g., for testing purpose)
- TODO revisit 64b and re-enable associated direct tests.
  Note that issue64b.spec.conf has been skipped for genCode for a while.
  This one generates invalid Scala due to LoadModelConfig extending
  a final case class BaseModelConfig.
- TODO some more general revision as the code has become a bit unwieldy


2020-12 - 0.9.982

- correct setting in readme is `--durations` not `--duration`

2020-10 - 0.9.981

- resolve #69 "Use consistent formatter for the generated code"
    - scalafmt for scala
    - google-java-format (v1.7, so tscfg can still run on JRE 8)

2020-09

- comment out some incorrect tests
- some adjustments and fix some warnings (still a couple remaining under 2.13 not as straightforward to fix)
- set 2.13.3 as default, locally and in ci, and fix compile error
- keep generated code under version control to
  facilitate diff inspection upon changes in the tool

2020-07

- some refact as a bit of cleanup and as preparation for #67
    - move DefineCase stuff to separate module
    - add `defineCaseOpt` to `Struct`
    - this define information should facilitate ordering the
      traversal with consideration for any `@define` interdependency

2020-06-29 - 0.9.981

- partial implementation of #62 "Ability to set enums in config"
  This is initially working in general for java and for references in nested objects for scala.
  TODO(scala) proper reference at first level. See generated `ScalaIssue62Cfg` upon running:

      > runMain tscfg.Main --spec src/main/tscfg/example/issue62.spec.conf --scala --cn ScalaIssue62Cfg --dd src/test/scala/tscfg/example

    where it should generate:

            final case class ScalaIssue62Cfg(
              fruit     : ScalaIssue62Cfg.FruitType
            )

    instead of:

            final case class ScalaIssue62Cfg(
              fruit     : FruitType
            )

- some refact for more general `@define` handling,
  and in preparation for #62 "Ability to set enums in config"

- add special `--skip-gen4tests` option so the config spec is skipped in the genCode
  phase prior to the tests. This will help with invalid specs whose attempted code
  generation is part of the test itself.
  (in general, we want to KEEP the `*.spec.conf` naming convention for specs)

2020-06-28 - 0.9.98

- publish v0.9.98 with "Ability to allow shared object inheritance" #64. Thanks @johanneshiry.

2020-06-11 - 0.9.97

- new `--withoutTimestamp` option, thanks @JustinPihony.

2020-05-06

- add a test program re array parsing in LB Config while replying to #61 "Default value for List type"

        > runMain tscfg.example.ConfigArrays

2020-02-28 - 0.9.96

- publish v0.9.96 with new `--scala:2.12` flag. Thanks @johanneshiry!

- some adjustments triggered by #59 "switch to scala 2.13 and remove deprecation warning for JavaConverters"
    - cross build to 2.13 (besides 2.12)
    - fix a bunch of warnings in general

2019-10-07 - 0.9.95

- fix #54 "Shared config objects"
  https://github.com/carueda/tscfg/issues/54#issuecomment-539096913.
  In short, the `@define`s are traversed first in ModelBuilder.

2019-09-14 - 0.9.94

- resolve #54 "Shared config objects"
    - initial implementation
    - only exercised with the explicit use of `@define` annotation
    - scoping should be handled already
    - no support for recursive type
      (example spec would look like issue54c.spec.conf)

2019-09-03 - 0.9.93

- fix #55 "Regex not properly captured".
  This was a regression, not sure when it was introduced.
  The fix is to escape each provided config value. Tests for multiline
  strings and containing some control chars also added.

2019-07-27 - 0.9.92

- resolve #49 "Fully validate given config on construction"
    - for both java and scala, wrapper construction will now throw a
      com.typesafe.config.ConfigException with a summary of all the
      com.typesafe.config.ConfigException's collected as it traverses
      the required config entries. Example of such summary:

            Invalid configuration:
                'service.poolSize': com.typesafe.config.ConfigException$Missing(No configuration setting found for key 'poolSize')
                'service.url': com.typesafe.config.ConfigException$Missing(No configuration setting found for key 'url')
                'service.debug': com.typesafe.config.ConfigException$WrongType(String: 5: debug has type NUMBER rather than BOOLEAN)
                'service.doLog': com.typesafe.config.ConfigException$WrongType(String: 6: doLog has type STRING rather than BOOLEAN)
                'service.factor': com.typesafe.config.ConfigException$WrongType(String: 7: factor has type BOOLEAN rather than NUMBER)

    - NOTE: option `scala:fp` removed: full paths are now always reported.
    - `$_reqConfig` now only output in the wrapper if actually called

- capture java and scala wrapper supporting methods in proper classes
  to facilitate validation at compile time.

2019-07-22 - 0.9.91

- set openjdk8 for travis ci

- resolve #47 about adding `--all-required` flag.
  The new `--all-required` flag strictly forces all entries (even objects)
  to be required (even the `@optional` annotation is ignored)

2019-02-01 - 0.9.9

- publish v0.9.9 with new `--durations` flag to generate
  `java.time.Duration` as base type for duration instead of `long`.
  Thanks @qux42!

2019-01-09 - 0.9.7

- publish v0.9.7 with new `--java:optionals` option to generate
  `Optional<T>` for optional fields in java. Thanks @qux42!

2018-12-20 - 0.9.6

- publish v0.9.6
- fix #40 "String-to-bytes conversion results in compilation error"

2018-08-06 - 0.9.5

- minor misc adjustments

2018-08-05 - 0.9.5

- \#36 "Report full path for missing required parameter":
    - Only implemented for scala at the moment.
      Also, this is an opt-in (new option `--scala:fp`) --
      perhaps later on make it the default behavior?
- for testing purposes now some options can be passed in the
  config spec file itself.

- internal: for consistency, `optional` in AnnType construction
  now also set to true if there's a default value

2018-08-03 - 0.9.4

- resolve #33 "Generate default config objects if non-existent".
  Thanks @JustinPihony !

2018-02-16 - 0.9.3

- resolve #31 "Option to generate getters".
  The option is `--java:getters`

2018-02-11 - 0.9.0

- resolve #30 "scala: option to use back ticks"
- Adjustments regarding keys with $ and quoted strings:
    - key containing $ is left alone (even if it's quoted).
      This mainly due to Config restrictions on keys involving $
    - otherwise, the key is unquoted (if quoted of course)

2018-02-11 - 0.8.4

- internal: use scala 2.12 (but cross compile to 2.11 too)
  and upgrade some dependencies

2017-09-05 - 0.8.3

- resolve #6: "generate conf template"

2017-07-13

- basic experimentation with static-config

2017-06-13 - 0.8.2

- use JavaConverters

2017-06-13 - 0.8.1

- use JavaConverters (instead of deprecated JavaConversions) in generated Scala code.
  Thanks @zishanbilal.

2017-02-24 - 0.8.0
- resolve #24 "Maven central"
  Note:
    - this uses the default artifact publication (jar, javadocs, etc), **not** the
      executable "fat" jar, which is basically the only artifact properly built.
  	- kept the current version

2017-02-03 - 0.8.0
- resolve #23 "support size-in-bytes type"
- renaming to better reflect "duration" elements, and in preparation for "size in bytes" support.

2016-12-17 - 0.7.2
- fix #22 "literal duration captured as string"
- generate code as part of test phase

2016-12-14 - 0.7.1

- add tests for class name or keys starting with `$_`
- fix #19 "quoted keys"
- use simpler, equivalent boolean access expression according to given default
- fixed: spec with literal values were not generating "primitive" types
  in java. There was a missing `&& a.default.isEmpty` in:
  `val typ = if (a.optional && a.default.isEmpty) toObjectType(memberType) else memberType`

2016-12-12 - 0.7.0
- capture model build warnings

2016-12-01 - 0.7.0
- another model revision

2016-12-01 - 0.5.1
- put "generated by" when running from main program

2016-11-30 - 0.5.0
- resolve #16 "rewrite code"
- resolve #15 "support list"
- resolve #10 "annotations" -- basically only the `#@optional` one
- NOTE: toString not generated anymore
- TODO revisit template generation (info moved to a wiki page)

2016-11-22 - 0.5.0
- advancing a rewrite...

2016-11-19 - 0.3.4
- re #15 "support list"
    - allow use of nodes.createAllNodes in object defining element of list
- fix #14 "section with numerical id generates invalid code"

2016-09-01 - 0.3.3
- fix #13 "scala: double definition"
  add `build` auxiliary method to avoid type erasure issue in case of optional member
- `--noToString` now processed for scala

2016-08-25 - 0.3.3
- add options `--toPropString` and `--noToString` (only processed for java):
 	- `--toPropString` to generate toPropString method (not generated by default)
 	- `--noToString`   to not generate toString method (generated by default)
- fix #12 "incorrect reference with standard types in path fragments"
- add tscfg.GenExamples for convenience
- java: factor out access to Config parameter for sub-section in static method __$config at the root class
- in java's toString, no need for conditional for string field if required or with default (non-null) value
- allow missing sections in given input configuration.
  This helps with sections whose all members are optional.
  Still to be done is proper handling of optional sections in general. Besides an explicit annotation
  to indicate that a section is optional, note that a section can also be implicitly optional
  if all its members are optional.

2016-08-16 - 0.3.2
- fix #11 "name collision with no-arg methods in scope"

2016-08-15 - 0.3.1
- update specs2-core to 3.8.4
- minor adj in Scala's toString

2016-03-03 - 0.3.1
- add scoverage

2016-03-01 - 0.3.0
- implement #8: "support durations" (for both java and scala)
- example-duration.all.conf generated with:

    ```
    > runMain tscfg.Main --spec example/example-duration.spec.conf --tpl all src/main/java/tscfg/example/example-duration.all.conf
    ```

2016-02-25 - 0.3.0
- initial work on #6: "generate conf templates."
- `--tpl` option with 3 possible types: base, local, all.
  Can be given multiple times.
  Running the example-4tpl.spec.conf:

    ```
    > runMain tscfg.Main --spec example/example-4tpl.spec.conf --tpl base  src/main/java/tscfg/example/example-4tpl.base.conf --tpl local src/main/java/tscfg/example/example-4tpl.local.conf --tpl all   src/main/java/tscfg/example/example-4tpl.all.conf
    ```
- As part of this, some refactoring, mainly around a new Type class that the various accessors now use.
- more complete TypeSpec (TODO simplify \*Accesor/Spec classes wrt types)
- entry with no explicit type is now considered optional (with given value as default)
- some preliminary annotation processing
  - @optional for sections - only generates comment in templates; no verification logic at all yet
  - @local to force inclusion of field in "local" template output

2016-02-07 - 0.2.1
- fix #5: incorrect reference with "config" fragment in path

2016-02-07 - 0.2.0
- fix #2: implement type inference

2016-01-24 - 0.1.5
- scala generation

2016-01-24 - 0.1.4
- include source info in preamble of generated file
- consider all java keywords and special literals to generate valid identifier

2016-01-11 - 0.1.3
- generate code for java 8 by default.
  Found out that Typesafe Config's `hasPath` method returns `false` for actually existing paths that just
  happen to be defined with `null`!  So, `hasPathOrNull` is now used in the generated code, and this method
  is only available in Typesafe Config 1.3.0+, which requires Java 8.
  The new flag `--j7` makes the tool generate `hasPath` checks instead of `hasPathOrNull`,
  so the generated code can be used with Typesafe Config 1.2.1 and Java <= 7.

