package components

import diode.data.Pot
import diode.react._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

import scala.util.Random
import scala.language.existentials

object SignUpPage {

  case class Props()


  protected class Backend($: BackendScope[Props, Unit]) {

    def render(props: Props): VdomElement = {
      <.div(
        <.h2("SignUpPage")
      )
    }
  }
    // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("SignUpPage")
    .renderBackend[Backend]
    .build

  def apply() = component(Props())
}
