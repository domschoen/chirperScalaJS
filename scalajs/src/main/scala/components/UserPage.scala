package components

import diode.data.Pot
import diode.react._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

import scala.util.Random
import scala.language.existentials

object UserPage {

  case class Props(userId: String)


  protected class Backend($: BackendScope[Props, Unit]) {

    def render(props: Props): VdomElement = {
      <.div(
        <.h2("UserPage")
      )
    }
  }
    // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("UserPage")
    .renderBackend[Backend]
    .build

  def apply(userId: String) = component(Props(userId))
}
