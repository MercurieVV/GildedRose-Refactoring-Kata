name := "GildedRose"

version := "1.0"

scalaVersion := "2.13.1"

resolvers += DefaultMavenRepository
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.1" % Test
libraryDependencies += "org.scalatestplus" %% "scalatestplus-scalacheck" % "3.1.0.0-RC2" % Test
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.3" % Test