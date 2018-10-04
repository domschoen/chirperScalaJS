package components

import diode.data.Pot
import diode.react._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

import scala.util.Random
import scala.language.existentials

object LoginPage {

  case class Props()
  case class State(loginChecked: Boolean, user: Option[String])


  protected class Backend($: BackendScope[Props, State]) {
    def mounted(p: Props) = {
      println("mod state ")
      $.modState({sta:State => sta.copy(loginChecked = true)})
      Callback.empty
    }

    def render(props: Props, s: State): VdomElement = {
      println("render " + s)
      if (s.loginChecked) {
        s.user match {
          case Some(userId) => ActivityStream(userId)
          case None => SignUpPage()
        }
      } else {
        <.div(^.className :="loading")
      }
    }
  }
  // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("LoginPage")
    .initialState(State(false, None))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply() = {
    println("create Login Page")
    component(Props())
  }
}
