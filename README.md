# sbt meta-build resources

This sbt project defines a resource `testfile.txt` in the meta-build that should be loadable by the classloader of the build in order to demonstrate [sbt issue #3498](https://github.com/sbt/sbt/issues/3498). (This is useful, for example, when a dependency of the build uses a library like slf4j for logging, and control over the log levels should be delegated to the build definition.)

A task using the classloader of the class representing `build.sbt` works:

```scala
lazy val buildClassLoader = taskKey[Option[String]]("tries to load the meta-build resource using the classloader of the class represented by build.sbt")

buildClassLoader := {
  Option(getClass.getClassLoader.getResourceAsStream("testfile.txt")).map(scala.io.Source.fromInputStream(_).mkString)
//       ^^^^^^^^^^^^^^^^^^^^^^^
}
```

```
sbt:sbt-classloader> show buildClassLoader
[info] Some(Resource in meta-build)
[success] Total time: 0 s, completed Sep 5, 2017 2:45:35 PM
```

However, libraries loaded via sbt plugins can't load the meta-build resource.

```scala
lazy val jgitClassLoader = taskKey[Option[String]]("tries to load the meta-build resource using the classloader of the a class in a JAR included by an sbt plugin")

jgitClassLoader := {
  Option(classOf[org.eclipse.jgit.api.Git].getClassLoader.getResourceAsStream("testfile.txt")).map(scala.io.Source.fromInputStream(_).mkString)
//       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
}
```

```
sbt:sbt-classloader> show jgitClassLoader
[info] None
[success] Total time: 0 s, completed Sep 5, 2017 2:45:38 PM
```

I've reproduced this issue in sbt 0.13.16 and 1.0.1.
