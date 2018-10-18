package components

import client.Main.Loc
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom
import services.{StreamUtils, UserUtils}
import shared.{Keys, User}
import scala.concurrent.ExecutionContext.Implicits.global

object UserChirps {

  case class Props(router: RouterCtl[Loc],userId: String)

  // TODO :  notFound shoud be added because at the beginning is shouldn't be not found even if user is not existing
  case class State(user: Option[User])


  protected class Backend($: BackendScope[Props, State]) {
    def mounted(p: Props): japgolly.scalajs.react.Callback = {
      println("UserChirps mounted")

      val request = UserUtils.getUser(p.userId, { user => {
        $.modState(_.copy(user = Some(user)))
      }},
        $.modState(_.copy(user = None))
      )
      Callback.future(request)
    }

    def render(props: Props, s: State): VdomElement = {
      val userId = props.userId
      s.user match {
        case Some(user) =>
          val userId = dom.window.localStorage.getItem(Keys.userIdKey)

          ContentLayout("Chirps for " + user.name,
            Section(
              <.div(^.className := "small-12 columns",
                ChirpForm(),
                ChirpStream(props.router, StreamUtils.createActivityStream(userId), Map())
              )
            )
          )

        case None =>
          <.div(^.className :="userChirps",
            <.h1("User " + userId + " not found")
          )
      }

    }
  }
    // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("UserChirps")
    .initialState(State(None))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(router: RouterCtl[Loc],userId: String) = component(Props(router, userId))
}
