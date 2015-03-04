name := """fias-frontend"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.scalikejdbc"      %% "scalikejdbc"                     % scalikejdbcVersion,
  "org.scalikejdbc"      %% "scalikejdbc-config"              % scalikejdbcVersion,
  "org.scalikejdbc"      %% "scalikejdbc-play-plugin"         % scalikejdbcPlayVersion,
  "org.scalikejdbc"      %% "scalikejdbc-play-fixture-plugin" % scalikejdbcPlayVersion,
  "org.postgresql"        % "postgresql"                      % "9.3-1100-jdbc41"
)

lazy val scalikejdbcVersion = "2.2.+"
lazy val scalikejdbcPlayVersion = "2.3.+"