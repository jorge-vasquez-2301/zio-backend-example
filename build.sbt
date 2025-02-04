ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

lazy val root = (project in file("."))
  .settings(
    name := "zio-backend-example",
    libraryDependencies ++= Seq(
      "dev.zio"            %% "zio-http" % "3.0.1",
      "com.augustnagro"    %% "magnum"   % "1.3.0",
      "io.github.iltotore" %% "iron"     % "2.6.0"
    )
  )
