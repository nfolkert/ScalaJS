import sbt._

class ScalaJSProject(info: ProjectInfo) extends DefaultProject(info) {
  override def managedStyle = ManagedStyle.Maven

  val specs = "org.scala-tools.testing" %% "specs" % "1.6.5" % "test" withSources()
  val rhino = "rhino" % "js" % "1.7R2" % "compile" withSources()
  val junit = "junit" % "junit" % "4.8.2" % "test" withSources()

  val bryanjswift = "Bryan J Swift Repository" at "http://repos.bryanjswift.com/maven2/"
  val junitInterface = "com.novocode" % "junit-interface" % "0.6" % "test"
  override def testFrameworks = super.testFrameworks ++ List(new TestFramework("com.novocode.junit.JUnitFrameworkNoMarker"))
}
