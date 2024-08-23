2024-08
Making this work again.

Created new PGP:
```
❯ gpg --list-keys

[keyboxd]
---------
pub   ed25519 2024-08-23 [SC] [expires: 2027-08-23]
      61E2C83F241206B722B7A09E348B4CEE2DC47A1B
uid           [ultimate] Central Repo <carueda@gmail.com>
sub   cv25519 2024-08-23 [E] [expires: 2027-08-23]
```

Uploaded the key to the keyserver:
```
❯ gpg --keyserver keyserver.ubuntu.com --send-keys 348B4CEE2DC47A1B
```
Then, after a few moments, checked
https://keyserver.ubuntu.com/pks/lookup?search=348B4CEE2DC47A1B&fingerprint=on&op=index
and the key was there.

Now, for the account, per https://central.sonatype.org/publish/generate-token/, I created a token,
which I captured in `~/.sbt/1.0/sonatype.sbt`:

```scala
credentials += Credentials("Sonatype Nexus Repository Manager",
                           "oss.sonatype.org",
                           "the-token-user",
                           "the-token-pw")
```

Then:
```
> +publishSigned
> +sonatypeBundleRelease
```

Refs:

- https://community.sonatype.com/t/401-content-access-is-protected-by-token-authentication-failure-while-performing-maven-release/12741/2
- https://central.sonatype.org/publish/generate-token/


----
(old notes)

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
