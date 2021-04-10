version in Global := "0.1"
scalaVersion in Global := "2.13.5"

lazy val root = project.in(file("."))
    .settings(
        name := "CurrencyStatistics",
        Compile / run / mainClass := Some("ru.ideaseeker.currency.Main")
    )
    .settings( // dependencies
        libraryDependencies += "org.apache.poi" % "poi" % "5.0.0",
        libraryDependencies += "org.apache.poi" % "poi-ooxml" % "5.0.0",
        libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.4.2",
        libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.7" % "test"
    )
    .settings( // sbt-assembly
        assembly / assemblyJarName  := "CurrencyStatistics.jar",
        assembly / test := {},
        assembly / mainClass := Some("ru.ideaseeker.currency.Main")
    )
    .settings( // scalafix
        semanticdbEnabled := true,
        semanticdbVersion := scalafixSemanticdb.revision,
        scalacOptions += "-Ywarn-unused"
    )