lazy val commonSettings = Seq(
  organization := "org.typelevel",
  version := "0.1.0",
	resolvers += "scalatl" at "https://milessabin.com/scalatl",
	//scalaVersion := "2.11.8" // compile will fail
	scalaVersion := "2.11.8-tl" // compile succeeds
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "si2712fix-demo",
		scalacOptions ++= Seq(
			"-feature"
		)
  )
