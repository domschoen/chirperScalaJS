package components

import services.StreamUtils.Socket
import services.{UserUtils}
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

import scala.language.existentials
import client.Main.Loc
import shared.User

import scala.concurrent.ExecutionContext.Implicits.global

object ChirpStream{

  case class Props(router: RouterCtl[Loc], stream: Socket, users: Map[String, User])
  case class State(message: Option[String], users: Map[String, User], chirps: List[shared.Chirp])


  protected class Backend($: BackendScope[Props, State]) {
    var loadingUsers = Map[String, Boolean]()

    // Function passed to the socket in order to update ourself in case of new chirp receive from the websocket
    def addChirp (chirp: shared.Chirp) = {
      //println("Add chirp " + chirp)
      $.modState(s => s.copy(chirps = chirp :: s.chirps)).runNow()
      Callback.empty
    }

    def mounted(p: Props, s: State): japgolly.scalajs.react.Callback = {
      //println("ChirpStream mounted")
      loadingUsers = Map()
      p.stream.connect(addChirp)
      Callback.empty
    }

    def unmount(p: Props): japgolly.scalajs.react.Callback = {
      //println("ChirpStream unmount")
      p.stream.close()
      Callback.empty
    }

    def handleMessageChange(e: ReactEventFromInput) = {
      val newMessage = if (e.target.value == null) None else Some(e.target.value)
      $.modState(_.copy(message = newMessage))
    }

    def loadUser(userId: String, s: State) = {
      if (!loadingUsers.contains(userId)) {
        loadingUsers + (userId -> true)
        val request = UserUtils.getUser(userId, { user => {
          val newUsers = s.users + (userId -> user);
          $.modState(_.copy(users = newUsers))
        }}, Callback.empty)
        Callback.future(request)
      }
    }

    def render(props: Props, s: State): VdomElement = {
      //props.users
      //println("ChirpStream | render with " + s.chirps)
      <.div(^.className := "chirpStream",
        <.hr(),
        s.chirps toTagMod (
          chirp => {
            val userName = if(s.users.contains(chirp.userId)) {
              s.users(chirp.userId).name
            } else {
              loadUser(chirp.userId, s)
              chirp.userId
            }
            Chirp(props.router, chirp.userId, userName, chirp.uuid, chirp.message)
          }
        )
      )
    }
  }


  // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("ChirpStream")
    .initialStateFromProps(p => State(None, p.users, List()))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props, scope.state))
    .componentWillUnmount(scope => scope.backend.unmount(scope.props))
    .build

  def apply(router: RouterCtl[Loc],stream: Socket, users: Map[String, User]) = component(Props(router, stream, users))
}
