lazy val buildClassLoader = taskKey[Option[String]]("tries to load the meta-build resource using the classloader of the class represented by build.sbt")

buildClassLoader := {
  Option(getClass.getClassLoader.getResourceAsStream("testfile.txt")).map(scala.io.Source.fromInputStream(_).mkString)
}

lazy val jgitClassLoader = taskKey[Option[String]]("tries to load the meta-build resource using the classloader of the a class in a JAR included by an sbt plugin")

jgitClassLoader := {
  Option(classOf[org.eclipse.jgit.api.Git].getClassLoader.getResourceAsStream("testfile.txt")).map(scala.io.Source.fromInputStream(_).mkString)
}
