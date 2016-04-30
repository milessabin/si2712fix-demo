resolvers += Resolver.sonatypeRepo("releases")

lazy val commonSettings = Seq(
  organization := "org.typelevel",
  version := "0.1.0",
  resolvers += "scalatl" at "http://milessabin.com/scalatl",
  scalaVersion := "2.11.8", 
  addCompilerPlugin("com.milessabin" % "si2712fix-plugin" % "1.0.1" cross CrossVersion.full) //without plugin compile will fail 
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "si2712fix-demo",
    scalacOptions ++= Seq(
      "-feature",
      "-language:higherKinds"
    ),
    libraryDependencies += "com.milessabin" % "si2712fix-library" % "1.0.1" cross CrossVersion.full
  )
