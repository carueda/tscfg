2016-08-25 - 0.3.3
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
 
