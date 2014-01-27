name := "Uriza"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.6",
  "org.apache.commons" % "commons-email" % "1.3.2",
  "org.webjars" % "bootstrap" % "3.0.3",
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.webjars" % "jquery-ui" % "1.10.3",
  "org.webjars" % "jquery-ui-themes" % "1.10.3",
  "org.webjars" % "font-awesome" % "4.0.3"
)     

play.Project.playJavaSettings

