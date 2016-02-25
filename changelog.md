2016-02-25 - 0.3.0
- initial work on #6: "generate conf templates."
  As part of this, some refactoring, mainly around a new Type class that the various accessors now use.
- more complete TypeSpec (TODO simplify \*Accesor/Spec classes wrt types)
- entry with no explicit type is now considered optional (with given value as default)
- `--tpl` option with 3 possible types: base, local, all.
  Running the example-4tpl.spec.conf: 

    ```
    > runMain tscfg.Main --spec example/example-4tpl.spec.conf --tpl base  src/main/java/tscfg/example/example-4tpl.base.conf
    > runMain tscfg.Main --spec example/example-4tpl.spec.conf --tpl local src/main/java/tscfg/example/example-4tpl.local.conf
    > runMain tscfg.Main --spec example/example-4tpl.spec.conf --tpl all   src/main/java/tscfg/example/example-4tpl.all.conf
    ```
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
 
