
name := "soc_network"

version := "0.1"

scalaVersion := "2.12.10"


val http4sV = "0.20.15"
val doobieV = "0.8.8"
val circeV = "0.11.2"
val tsecV = "0.0.1-M11"
val pureConfigVersion = "0.12.2"
val logbackVersion = "1.2.3"

libraryDependencies ++= Seq(
  "eu.timepit" %% "refined" % "0.9.10",
  "io.estatico" %% "newtype" % "0.4.3",
  "org.http4s" %% "http4s-dsl" % http4sV,
  "org.http4s" %% "http4s-blaze-server" % http4sV,
  "org.http4s" %% "http4s-blaze-client" % http4sV,
  "org.http4s" %% "http4s-twirl" % http4sV,
  "org.http4s" %% "http4s-circe" % http4sV,
  "io.circe" %% "circe-generic" % circeV,
  "io.circe" %% "circe-refined" % circeV,
  "io.circe" %% "circe-literal" % circeV,
  "org.tpolecat" %% "doobie-core"   % doobieV,
  "org.tpolecat" %% "doobie-hikari" % doobieV,
  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion,
  "com.github.pureconfig" %% "pureconfig-cats-effect" % pureConfigVersion,
  "mysql"  % "mysql-connector-java"    % "5.1.34",
  "ch.qos.logback" % "logback-classic" % logbackVersion,

  "io.github.jmcardon" %% "tsec-http4s" % tsecV,
  "io.github.jmcardon" %% "tsec-common" % tsecV,
  "io.github.jmcardon" %% "tsec-password" % tsecV,
  "io.github.jmcardon" %% "tsec-mac" % tsecV,
  "io.github.jmcardon" %% "tsec-signatures" % tsecV,
  "io.github.jmcardon" %% "tsec-jwt-mac" % tsecV,
  "io.github.jmcardon" %% "tsec-jwt-sig" % tsecV
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

enablePlugins(JavaAppPackaging)

enablePlugins(SbtTwirl)

mainClass in assembly := Some("com.andrew_ya.soc_net.ServerApp")

scalacOptions ++= Seq(
  "-Ypartial-unification"
)