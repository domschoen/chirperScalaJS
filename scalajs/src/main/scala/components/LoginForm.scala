package components

import org.scalajs.dom.Event

import scala.util.{Failure, Random, Success}
import scala.language.existentials
import org.scalajs.dom

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import upickle.default._
import shared.User
import upickle.default.{macroRW, ReadWriter => RW}
import org.scalajs.dom.ext.AjaxException
import dom.ext.Ajax
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import services.UserUtils
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
              val request = UserUtils.getUser(trimID, { user => {
                dom.window.localStorage.setItem(Keys.userIdKey, trimID)
                p.onLogin(user)
              }},
              {
                println("User not found " + s.error)
                val errorMsg = "User " + trimID + " does not exist."
                $.modState(_.copy(error = Some(errorMsg)))
              })
              Callback.future(request)
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
      val errorMsg = if (s.error.isDefined) s.error.get else ""
      println("Render with error " + s.error)
      Section(
        <.div(^.className := "small-12 large-4 columns",
          <.form(^.className := "loginForm", ^.onSubmit ==> { e: ReactEventFromInput => handleSubmit(props, s, e)},
            <.input.text(^.placeholder := "Username...", ^.value := valueString,
              ^.onChange ==> { e: ReactEventFromInput => handleUserIdChange(e)}),
            {
              components.Error(errorMsg)
            }.when(s.error.isDefined),
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
