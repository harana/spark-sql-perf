name := "spark-sql-perf"

organization := "com.databricks"

scalaVersion := "2.13.8"

crossScalaVersions := Seq("2.13.8")

// All Spark Packages need a license
licenses := Seq("Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0"))

initialCommands in console :=
  """
    |import org.apache.spark.sql._
    |import org.apache.spark.sql.functions._
    |import org.apache.spark.sql.types._
    |import org.apache.spark.sql.hive.test.TestHive
    |import TestHive.implicits
    |import TestHive.sql
    |
    |val sqlContext = TestHive
    |import sqlContext.implicits._
  """.stripMargin

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-mllib" % "3.3.0",
  "org.apache.spark" %% "spark-sql" % "3.3.0",
  "com.github.scopt" %% "scopt" % "4.1.0",
  "com.twitter" %% "util-jvm" % "22.7.0" % "provided",
  "org.scalatest" %% "scalatest" % "3.2.13" % "test",
  "org.yaml" % "snakeyaml" % "1.23"
)

fork := true

val runBenchmark = inputKey[Unit]("runs a benchmark")

runBenchmark := {
  import complete.DefaultParsers._
  val args = spaceDelimited("[args]").parsed
  val scalaRun = (runner in run).value
  val classpath = (fullClasspath in Compile).value
  scalaRun.run("com.databricks.spark.sql.perf.RunBenchmark", classpath.map(_.data), args,
    streams.value.log)
}


val runMLBenchmark = inputKey[Unit]("runs an ML benchmark")

runMLBenchmark := {
  import complete.DefaultParsers._
  val args = spaceDelimited("[args]").parsed
  val scalaRun = (runner in run).value
  val classpath = (fullClasspath in Compile).value
  scalaRun.run("com.databricks.spark.sql.perf.mllib.MLLib", classpath.map(_.data), args,
    streams.value.log)
}
