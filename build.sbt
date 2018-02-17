name := "fakealert"
organization := "bonafide"

version := "0.1"

lazy val `fakealert` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

packageDescription := """FakeAlert API"""

maintainer := """BonaFide <contact@fakealert.pl>"""

resolvers ++= Seq(
  "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/",
  "Typesafe" at "http://repo.typesafe.com/typesafe/releases",
  "spray repo" at "http://repo.spray.io",
  "Central repo" at "https://repo1.maven.org/maven2"
)

libraryDependencies ++= Seq(
  jdbc,
  ehcache,
  ws,
  specs2 % Test,
  guice,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "org.scalikejdbc" %% "scalikejdbc"       % "3.2.1",
  "org.scalikejdbc" %% "scalikejdbc"                  % "3.2.0",
  "org.scalikejdbc" %% "scalikejdbc-config"           % "3.2.0",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.6.0-scalikejdbc-3.2"
)

scalikejdbcSettings

serverLoading in Debian := Some(ServerLoader.Systemd)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
