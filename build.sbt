lazy val stratShipGame =
  (project in file("."))
    .enablePlugins(ScalaJSPlugin, SbtIndigo) // Enable the Scala.js and Indigo plugins
    .settings( // Standard SBT settings
      name := "strat-ship",
      version := "0.0.1",
      scalaVersion := "2.13.3",
      organization := "com.engsner"
    )
    .settings( // Indigo specific settings
      showCursor := true,
      title := "Strategy Ships",
      gameAssetsDirectory := "assets",
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "indigo" % "0.2.0",
        "io.indigoengine" %%% "indigo-json-circe" % "0.2.0",
        "io.indigoengine" %%% "indigo-extras"     % "0.2.0"
      )
    )

addCommandAlias("buildGame", ";compile;fastOptJS;indigoBuildJS")
addCommandAlias("publishGame", ";compile;fullOptJS;indigoPublishJS")
