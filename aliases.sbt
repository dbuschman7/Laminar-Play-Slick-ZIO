import Util._

addCommandAlias("l", "projects")
addCommandAlias("ll", "projects")
addCommandAlias("ls", "projects")
addCommandAlias("cd", "project")
addCommandAlias("root", "cd laminar-play-slick-zio")
addCommandAlias("fullClean", ";clean ;cleanFiles")
addCommandAlias("cc", ";root ;fullClean   ;+tc")
addCommandAlias("tc", "Test / compile")
addCommandAlias("ctc", ";clean ;cleanFiles ;tc")
addCommandAlias("to", "testOnly")
addCommandAlias("rcc", ";reload ;cc")

addCommandAlias("t", "test")
addCommandAlias("r", "run")
addCommandAlias("rs", "reStart")
addCommandAlias("s", "reStop")
addCommandAlias(
  "styleCheck",
  "scalafmtSbtCheck; scalafmtCheckAll; Test / compile; scalafixAll --check",
)
addCommandAlias(
  "styleFix",
  "Test / compile; scalafixAll; scalafmtSbt; scalafmtAll",
)
addCommandAlias(
  "up2date",
  "reload plugins; dependencyUpdates; reload return; dependencyUpdates",
)
