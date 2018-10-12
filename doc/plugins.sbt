// repository for Typesafe plugins
resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"

// In lagom scalajs example
// In d2spa
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.10")

// In lagom scalajs example
// In d2spa
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.0")

// In d2spa
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.0")

///------------
// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.4")


// in d2spa
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.5")


// In lagom scalajs example
// In d2spa
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")

// In lagom scalajs example
//addSbtPlugin("com.vmunier" % "sbt-play-scalajs" % "0.3.1")



addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.0.0")
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "3.0.0")
addSbtPlugin("com.lightbend.conductr" % "sbt-conductr" % "2.1.7")
