package components

import diode.data.Pot
import diode.react._
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.Event
import services.AjaxClient

import scala.util.Random
import scala.language.existentials
import shared.{Keys, User}
import org.scalajs.dom
import scala.concurrent.ExecutionContext.Implicits.global

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
              Callback.future(
                AjaxClient.getUser(id).map(u =>
                  u match {
                    case Some(fUser) =>
                      dom.window.localStorage.setItem(Keys.userIdKey, trimID)
                      p.onLogin(fUser)
                    case None =>
                      val errorMsg = "User " + trimID + " does not exist."
                      $.modState(_.copy(error = Some(errorMsg)))

                  }
                )
              )
            } else Callback.empty

          case None =>
            Callback.empty
        }
      }
    }


    def render(props: Props, s: State): VdomElement = {
      <.div(
        <.h2("LoginForm")
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
