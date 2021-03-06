name := "Uriza"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.webjars" % "webjars-locator" % "0.13",
  "mysql" % "mysql-connector-java" % "5.1.6",
  "org.apache.commons" % "commons-email" % "1.3.2",
  "org.webjars" % "bootstrap" % "3.1.1",
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.webjars" % "jquery-ui" % "1.10.3",
  "org.webjars" % "jquery-ui-themes" % "1.10.3",
  "org.webjars" % "font-awesome" % "4.0.3",
  "org.webjars" % "modernizr" % "2.6.2-1",
  "commons-io" % "commons-io" % "2.4",
  "org.webjars" % "jquery-validation" % "1.11.1"
)     

play.Project.playJavaSettings

