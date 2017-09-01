name := "mindlevel-backend"

version := "1.0"

logLevel := Level.Warn
scalaVersion := "2.12.3"
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
  "ch.megard" %% "akka-http-cors" % "0.2.1",
  "jp.co.bizreach" %% "aws-s3-scala" % "0.0.11",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "mysql" % "mysql-connector-java" % "6.0.6"
)

slick := slickCodeGenTask.value

sourceGenerators in Compile += slickCodeGenTask

lazy val slick = TaskKey[Seq[File]]("gen-tables")
// Don't forget to add the src_managed folder in intellij if net.mindlevel.models is not found
lazy val slickCodeGenTask = Def.task {
  val dir = sourceManaged.value
  val cp = (dependencyClasspath in Compile).value
  val r = (runner in Compile).value
  val s = streams.value
  val pkg = "net.mindlevel.models"
  val outputDir = (dir / "slick").getPath
  val username = "root"
  val password = "password"
  //val url = "jdbc:mysql://mindlevel.cxkevz1h137d.eu-central-1.rds.amazonaws.com:3306/mindlevel?nullNamePatternMatchesAll=true"
  val url = "jdbc:mysql://localhost/mindlevel?nullNamePatternMatchesAll=true"
  val jdbcDriver = "com.mysql.cj.jdbc.Driver"
  val slickDriver = "slick.jdbc.MySQLProfile"
  // TODO: add foreach to check errors
  r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg, username, password), s.log)
  val fname = outputDir + "/" + pkg.replace(".", "/") + "/Tables.scala"
  Seq(file(fname))
}
