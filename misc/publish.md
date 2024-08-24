2024-08
Trying to make this work again.

Created new PGP:
```
❯ gpg --list-keys

[keyboxd]
---------
pub   ed25519 2024-08-23 [SC] [expires: ....]
      .....
uid           [ultimate] Central Repo <carueda@gmail.com>
sub   cv25519 2024-08-23 [E] [expires: ...]
```
Uploaded it:
```
❯ gpg --keyserver keyserver.ubuntu.com --send-keys .....
```

Per https://central.sonatype.org/publish/generate-token/,
created a token, I captured in `~/.sbt/1.0/sonatype.sbt`:
```
credentials += Credentials("Sonatype Nexus Repository Manager",
                           "oss.sonatype.org",
                           "the-token-user",
                           "the-token-pw")
```
But no luck:
```
sbt clean
sbt +publishSigned
sbt +sonatypeBundleRelease
```
for not clear reason, getting "error [Sonatype] [STAGE_FAILURE] Failed to promote the repository")
with the sonatype UI showing:
```
typeId         RepositoryWritePolicy
failureMessage Artifact updating: Repository ='releases:Releases' does not allow updating artifact='/com/github/carueda/tscfg_3/1.1.1/tscfg_3-1.1.1.jar'
```
`:(`

Refs:

- https://community.sonatype.com/t/401-content-access-is-protected-by-token-authentication-failure-while-performing-maven-release/12741/2
- https://central.sonatype.org/publish/generate-token/
