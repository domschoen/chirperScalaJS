package client

import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}


import shared.SharedMessages
import components.Dashboard

@JSExportTopLevel("Hello")
object Hello extends js.JSApp {
  sealed trait Loc

  case object DashboardLoc extends Loc



  // configure the router
  val routerConfig = RouterConfigDsl[Loc].buildConfig { dsl =>
    import dsl._

    (staticRoute(root, DashboardLoc) ~> render(Dashboard())
      ).notFound(redirectToPage(DashboardLoc)(Redirect.Replace))
  }



  @JSExport
  def main(): Unit = {
    print("Welcome to your Play application's JavaScript!");

    val router = Router(BaseUrl.until_#, routerConfig)

    router().renderIntoDOM(dom.document.getElementById("root"))
  }


}
