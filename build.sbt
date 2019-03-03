name := "HW3_GraphVisualisation"

version := "0.1"

scalaVersion := "2.12.8"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

libraryDependencies ++= Seq("ch.qos.logback" % "logback-classic" % "1.2.3",
  "guru.nidi" % "graphviz-java" % "0.8.3", "info.leadinglight" % "jdot" % "1.0")