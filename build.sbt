ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.4"

ThisBuild / semanticdbEnabled := true

ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

lazy val root = (project in file("."))
  .settings(
    name := "zio-backend-example",
    scalacOptions ++= Seq(
      "-Wunused:imports"
    ),
    libraryDependencies ++= Seq(
      // ZIO HTTP
      "dev.zio"            %% "zio-http"               % "3.2.0",
      // Database
      "com.augustnagro"    %% "magnumzio"              % "2.0.0-M1",
      "org.postgresql"      % "postgresql"             % "42.7.5",
      "org.testcontainers"  % "testcontainers"         % "1.20.4",
      "org.testcontainers"  % "postgresql"             % "1.20.4",
      "com.zaxxer"          % "HikariCP"               % "6.2.1",
      // Iron
      "io.github.iltotore" %% "iron"                   % "3.0.0",
      // Logging
      "dev.zio"            %% "zio-logging-jul-bridge" % "2.4.0"
    )
  )
