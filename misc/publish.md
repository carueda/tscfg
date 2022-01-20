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
