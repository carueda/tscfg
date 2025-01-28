# Run:  misc/publish.sh
set -eu

sbt clean
sbt +test
GPG_TTY=$(tty)
export GPG_TTY
sbt +publishSigned
sbt +sonatypeBundleRelease
