name := "fakealert"
organization := "bonafide"

version := "0.1"

lazy val `fakealert` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice ,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc41",
  "org.scalikejdbc" %% "scalikejdbc"       % "3.2.1",
  "org.scalikejdbc" %% "scalikejdbc"                  % "3.2.0",
  "org.scalikejdbc" %% "scalikejdbc-config"           % "3.2.0",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.6.0-scalikejdbc-3.2")

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

scalikejdbcSettings

serverLoading in Debian := Some(ServerLoader.Systemd)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )