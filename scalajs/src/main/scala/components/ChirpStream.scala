package components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import services.StreamUtils.Socket
import services.{StreamUtils, UserUtils}
import shared.{Keys, PostedMessage, User}
import upickle.default._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import shared.User

import scala.language.existentials
import scala.scalajs.js.JSON
import client.Main.Loc
import client.Main.LoginLoc
import org.scalajs.dom
//import components.{ContentLayout, PageLayout}
import dom.ext.Ajax
import upickle.default._
import shared.User
import upickle.default.{macroRW, ReadWriter => RW}
import scala.concurrent.ExecutionContext.Implicits.global
import shared.Keys

import scala.concurrent.ExecutionContext.Implicits.global

object ChirpStream{

  case class Props(stream: Socket, users: Map[String, User])
  case class State(message: Option[String], users: Map[String, User], chirps: List[shared.Chirp])


  protected class Backend($: BackendScope[Props, State]) {
    var loadingUsers = Map[String, Boolean]()
    //var stream: Socket = null

    def primitiveF (chirp: shared.Chirp) = {
        $.modState(s => s.copy(chirps = chirp :: s.chirps))
     }

    def mounted(p: Props, s: State): japgolly.scalajs.react.Callback = {
      println("ChirpStream mounted")
      loadingUsers = Map()
      p.stream.connect(primitiveF)
      Callback.empty
    }

    def unmount(p: Props): japgolly.scalajs.react.Callback = {
      println("ChirpStream unmount")
      p.stream.close()
      Callback.empty
    }

    // user has entered the user Id and submit it
    def handleSubmit(p: Props, s: State, e: ReactEventFromInput): Callback = {
      e.preventDefaultCB >> {
        s.message match {
          case Some(text) =>
            val trimText: String =  text.trim()
            if (trimText.length > 0) {
              val userId = dom.window.localStorage.getItem(Keys.userIdKey)
              val postedMessage = PostedMessage(userId,trimText)
              val postUrl = "/api/chirps/live/" + userId
              val request = Ajax.post(
                url = postUrl,
                data = write(postedMessage)
              ).recover {
                // Recover from a failed error code into a successful future
                case dom.ext.AjaxException(req) => req
              }.map( r =>
                r.status match {
                  case 200 =>
                    $.modState(_.copy(message = None))
                  case _ =>
                    println("ChirpForm | url " +  postUrl + " error: " + r.status + " with message: " + r.responseText)
                    Callback.empty
                }
              )
              Callback.future(request)
            } else Callback.empty

          case None =>
            Callback.empty
        }
      }
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
            Chirp(chirp.userId, userName, chirp.uuid, chirp.message)
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

  def apply(stream: Socket, users: Map[String, User]) = component(Props(stream, users))
}
