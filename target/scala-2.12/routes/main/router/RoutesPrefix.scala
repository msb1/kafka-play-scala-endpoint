// @GENERATOR:play-routes-compiler
// @SOURCE:/home/bw/sbt/play-scala-endpoint/conf/routes
// @DATE:Sun Dec 01 12:28:19 EST 2019


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
