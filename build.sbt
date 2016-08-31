lazy val commonSettings = Seq(
  organization := "org.typelevel",
  version := "0.1.0",
  //scalaOrganization := "org.scala-lang", // compilation will fail
  scalaOrganization := "org.typelevel",    // compilation will succeed
  scalaVersion := "2.11.8"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "si2712fix-demo",
    scalacOptions ++= Seq(
      "-feature",
      "-language:higherKinds",
      "-Ypartial-unification"
    )
  )
