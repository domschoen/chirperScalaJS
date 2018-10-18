package components

import client.Main.Loc
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

import scala.util.Random
import scala.language.existentials
import org.scalajs.dom
import services.AjaxClient

import scala.concurrent.ExecutionContext.Implicits.global
import shared.Keys
import shared.User
import dom.ext._
import org.scalajs.dom.Event

import scala.util.{Random, Success, Failure}
import scala.language.existentials
import org.scalajs.dom

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.typedarray._
import upickle.default._
import shared.User
import upickle.default.{macroRW, ReadWriter => RW}
import org.scalajs.dom.ext.AjaxException
import dom.ext.Ajax
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import shared.Keys

import util._


// Translation of App
object AppPage {

  case class Props(ctl: RouterCtl[Loc], userId: Option[String], showAddFriends: Boolean)
  case class State(loginChecked: Boolean, user: Option[User])


  protected class Backend($: BackendScope[Props, State]) {


    def mounted(p: Props): japgolly.scalajs.react.Callback = {
      println("LoginPage mounted")
      val userId = dom.window.localStorage.getItem(Keys.userIdKey)
      println("LoginPage mounted | user ID:" + userId)

      if (userId != null) {
        val request = Ajax.get("/api/users/" + userId).recover {
          // Recover from a failed error code into a successful future
          case dom.ext.AjaxException(req) => req
        }.map( r =>
          r.status match {
            case 200 =>
              val user = read[User](r.responseText)
              $.modState({sta:State => sta.copy(loginChecked = true, user = Some(user))})
            case _ =>
              println("Failure processing")

              dom.window.localStorage.removeItem(Keys.userIdKey)
              $.modState({sta:State => sta.copy(loginChecked = true)})
          }
        )
        Callback.future(request)
      } else {
        $.modState({sta:State => sta.copy(loginChecked = true)})
      }
    }

    def handleLogin(user: User): Callback = {
      $.modState({sta:State => sta.copy(user = Some(user))})
    }

    def logout(e: ReactEventFromInput): Callback = {
      e.preventDefaultCB >> {
        dom.window.localStorage.removeItem(Keys.userIdKey)
        $.modState({sta:State => sta.copy(user = None)})
      }
    }


    def render(props: Props, s: State): VdomElement = {
      println("render " + s)
      if (s.loginChecked) {
        s.user match {
          case Some(user) => {
            // set UserChirps if userID
            // set AddFriendPage if showAddFriends
            val subComponent = if (props.showAddFriends) {
              AddFriendPage(props.ctl)
            } else {
              props.userId match {
                case Some(uid) =>
                  UserChirps(props.ctl, uid)
                case None =>
                  ActivityStream(props.ctl, user)
              }
            }
            PageLayout(props.ctl, Some(user), false, logout,
              subComponent
            )
          }
          case None =>  {
            PageLayout(props.ctl, None, true, e => Callback.empty,
              ContentLayout("Login",
                LoginForm(handleLogin)
              )
            )
          }
        }
      } else {
        <.div(^.className :="loading")
      }
    }
  }
  // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("AppPage")
    .initialState(State(false, None))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(ctl: RouterCtl[Loc], userId: Option[String], showAddFriends: Boolean) = {
    println("create Login Page")
    component(Props(ctl, userId, showAddFriends))
  }
}
