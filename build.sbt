ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

ThisBuild / semanticdbEnabled := true

ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val root = (project in file("."))
  .settings(
    name := "zio-backend-example",
    scalacOptions ++= Seq(
      "-Wunused:imports"
    ),
    libraryDependencies ++= Seq(
      "dev.zio"            %% "zio-http"       % "3.0.1",
      "com.augustnagro"    %% "magnumzio"      % "2.0.0-M1",
      "io.github.iltotore" %% "iron"           % "2.6.0",
      "org.postgresql"      % "postgresql"     % "42.7.5",
      "org.testcontainers"  % "testcontainers" % "1.20.4",
      "org.testcontainers"  % "postgresql"     % "1.20.4",
      "com.zaxxer"          % "HikariCP"       % "6.2.1"
    )
  )
