package components

import client.Main.Loc
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom
import services.StreamUtils

import scala.util.Random
import scala.language.existentials
import shared.{Keys, User}

object ActivityStream {

  case class Props(router: RouterCtl[Loc],user: User)
  case class State(users: Map[String, User])


  protected class Backend($: BackendScope[Props, State]) {
    def mounted(p: Props): japgolly.scalajs.react.Callback = {
      println("LoginPage mounted")
      Callback.empty
    }

    def render(props: Props, s: State): VdomElement = {
      val userId = dom.window.localStorage.getItem(Keys.userIdKey)
      ContentLayout("Chirps feed",
        Section(
          <.div(^.className := "small-12 columns",
            ChirpForm(),
            ChirpStream(props.router, StreamUtils.createActivityStream(userId), s.users)
          )
        )
      )
    }
  }
    // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("ActivityStream")
    .initialState(State(Map()))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(router: RouterCtl[Loc],user: User) = component(Props(router, user))
}
