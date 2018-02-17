logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.11")

// Don't forget adding your JDBC driver
libraryDependencies += "org.postgresql" % "postgresql" % "9.3-1102-jdbc41"

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.2.1")