package components

import diode.data.Pot
import diode.react._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

import scala.util.Random
import scala.language.existentials

import shared.User

object ActivityStream {

  case class Props(user: User)
  case class State(users: List[User])


  protected class Backend($: BackendScope[Props, State]) {
    def mounted(p: Props): japgolly.scalajs.react.Callback = {
      println("LoginPage mounted")
      Callback.empty
    }

    def render(props: Props, s: State): VdomElement = {
      ContentLayout("Chirps feed",
        Section(
          <.div(^.className := "small-12 columns",
            ChirpForm()

          )
        )
      )
    }
  }
    // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("ActivityStream")
    .initialState(State(List()))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(user: User) = component(Props(user))
}
