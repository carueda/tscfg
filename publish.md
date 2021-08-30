Refs:

- https://www.scala-sbt.org/release/docs/Using-Sonatype.html
- https://github.com/xerial/sbt-sonatype

One-time steps:

        > set pgpReadOnly := false
        > pgp-cmd gen-key
        > pgp-cmd send-key me@example.net hkp://pool.sks-keyservers.net

`~/.sbt/version/sonatype.sbt`:

    credentials += Credentials("Sonatype Nexus Repository Manager",
                               "oss.sonatype.org",
                               "<your username>",
                               "<your password>")

Then:

        > reload
        > clean
        > assembly
        > +package
        > +publishSigned
        > +sonatypeBundleRelease

Argh, all good upon general upgrade to sbt 1.5.5 and build/release dependencies...
... but `+sonatypeBundleRelease` is failing, and it's unclear why `:(`.
Same problem whether with `publishTo := sonatypePublishToBundle.value` or with the traditional `publishTo := {...}`.

After a couple of attempts, I got `sonatypeBundleRelease` to complete, but only by itself
(not with `+`, that is for scala 2.12, which btw seemed to always be associated with the error).
