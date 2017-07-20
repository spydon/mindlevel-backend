name := "mindlevel-backend"

version := "1.0"

scalaVersion := "2.12.1"
val slickVersion = "3.2.0"
val akkaVersion = "10.0.9"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "com.typesafe.akka" %% "akka-http" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaVersion,
  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion,
  "com.github.t3hnar" %% "scala-bcrypt" % "3.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "mysql" % "mysql-connector-java" % "6.0.6"
)

slick := slickCodeGenTask.value

sourceGenerators in Compile += slickCodeGenTask

lazy val slick = TaskKey[Seq[File]]("gen-tables")
// TODO: Solve the manual add of O.AutoInc for CURRENT_TIMESTAMP fields
lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
  val pkg = "models"
  val outputDir = (dir / "slick").getPath
  val username = "root"
  val password = "password"
  val url = "jdbc:mysql://localhost/mindlevel?nullNamePatternMatchesAll=true"
  val jdbcDriver = "com.mysql.cj.jdbc.Driver"
  val slickDriver = "slick.jdbc.MySQLProfile"
  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg, username, password), s.log))
  val fname = outputDir + "/" + pkg + "/Tables.scala"
  Seq(file(fname))
}
