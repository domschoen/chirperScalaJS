# Chirper Scala JS

Chirper, the famous Lagom example from Lightbend, has a frontend in Javascript. This project has a translation of the Javascript in Scala.js

It uses:
 - Scala.js
 - ScalaReact
 - upickle (for case class to JSON and reverse)

## Current state
Working like original Chirper in Javascript.

## Usage
Compare to Chirper, this version takes more memory and I had to increase the memory with this command before running sbt:
```
> export JAVA_OPTS="-Xmx2g -XX:MaxMetaspaceSize=1g -Xss4m"
```

Go to the project directory and issue those commands:
```
> sbt
> runAll
```

open you browser with this url: http://localhost:9000

