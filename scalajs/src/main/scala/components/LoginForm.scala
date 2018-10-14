package components

import org.scalajs.dom.Event

import scala.util.{Failure, Random, Success}
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

object LoginForm {

  case class Props(onLogin: User => Callback)
  case class State(userId: Option[String], error: Option[String])


  protected class Backend($: BackendScope[Props, State]) {

    // user has entered the user Id and submit it
    def handleSubmit(p: Props, s: State, e: ReactEventFromInput): Callback = {
      e.preventDefaultCB >> {
        s.userId match {
          case Some(id) =>
            val trimID: String =  id.trim()
            if (trimID.length > 0) {
              val f = Ajax.get("/api/users/" + trimID)
              f.onComplete{
                case Success(r) =>
                  val user = read[User](r.responseText)
                  dom.window.localStorage.setItem(Keys.userIdKey, trimID)
                  p.onLogin(user)
                case Failure(e) => {
                  val errorMsg = "User " + trimID + " does not exist."
                  $.modState(_.copy(error = Some(errorMsg)))
                }
              }
              CallbackTo(f)
            } else Callback.empty

          case None =>
            Callback.empty
        }
      }
    }

    def handleUserIdChange(e: ReactEventFromInput) = {
      val newUserId = if (e.target.value == null) None else Some(e.target.value)
      $.modState(_.copy(userId = newUserId))
    }

    def render(props: Props, s: State): VdomElement = {
      val valueString = if (s.userId.isDefined) s.userId.get else ""
      Section(
        <.div(^.className := "small-12 large-4 columns",
          <.form(^.className := "loginForm", ^.onSubmit ==> { e: ReactEventFromInput => handleSubmit(props, s, e)},
            <.input.text(^.placeholder := "Username...", ^.value := valueString,
              ^.onChange ==> { e: ReactEventFromInput => handleUserIdChange(e)}),
            (s.error match {
              case Some(errorMsg) => errorMsg
              case None => ""
            }),
            <.input.submit(^.value := "Login")
          )
        )
      )
    }
  }
    // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("LoginForm")
    .initialState(State(None, None))
    .renderBackend[Backend]
    .build

  def apply(onLogin: User => Callback) = component(Props(onLogin))
}
