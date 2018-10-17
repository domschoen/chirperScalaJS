package components

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

object SignUpPage {



  case class Props(ctl: RouterCtl[Loc])
  case class State(userId: Option[String], name: Option[String], error: Option[String])


  protected class Backend($: BackendScope[Props, State]) {

    def handleSubmit(p: Props, s: State, e: ReactEventFromInput): Callback = {
      e.preventDefaultCB >> {
        s.userId match {
          case Some(id) =>
            val trimID: String =  id.trim()
            s.name match {
              case Some(name) =>
                val trimName: String =  name.trim()
                if (trimID.length > 0) {
                  val user = User(id,name,List())
                  val request = Ajax.post(
                    url = "/api/users",
                    data = write(user)
                  ).recover {
                    // Recover from a failed error code into a successful future
                    case dom.ext.AjaxException(req) => req
                  }.map( r =>
                      r.status match {
                        case 200 =>
                          dom.window.localStorage.setItem(Keys.userIdKey, trimID)
                          p.ctl.set(LoginLoc)

                        case _ =>
                          val errorMsg = "User " + trimID + " already exist."
                          println(errorMsg)
                         $.modState(_.copy(error = Some(errorMsg)))
                      }
                  )
                  Callback.future(request)
                } else Callback.empty

            }
          case _ =>
            Callback.empty
        }
      }
    }

    def handleUserIdChange(e: ReactEventFromInput) = {
      val newUserId = if (e.target.value == null) None else Some(e.target.value)
      $.modState(_.copy(userId = newUserId))
    }
    def handleNameChange(e: ReactEventFromInput) = {
      val newName = if (e.target.value == null) None else Some(e.target.value)
      $.modState(_.copy(name = newName))
    }

    def render(props: Props, s: State): VdomElement = {
      val valueString = if (s.userId.isDefined) s.userId.get else ""
      val nameString = if (s.name.isDefined) s.name.get else ""
      val errorMsg = if (s.error.isDefined) s.error.get else ""

      PageLayout(props.ctl, None, true, e => Callback.empty,
        ContentLayout("Sign up",
          Section(
            <.div(^.className := "small-12 large-4 columns",
              <.form(^.className := "signupForm", ^.onSubmit ==> { e: ReactEventFromInput => handleSubmit(props, s, e)},
                <.input.text(^.placeholder := "Username...", ^.value := valueString,
                  ^.onChange ==> { e: ReactEventFromInput => handleUserIdChange(e)}),
                <.input.text(^.placeholder := "Name...", ^.value := nameString,
                  ^.onChange ==> { e: ReactEventFromInput => handleNameChange(e)}),
                {
                  components.Error(errorMsg)
                }.when(s.error.isDefined),
                <.input.submit(^.value := "Sign up")
              )
            )
          )
        )
      )
    }
  }
    // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("SignUpPage")
    .initialState(State(None, None, None))
    .renderBackend[Backend]
    .build

  def apply(ctl: RouterCtl[Loc]) = component(Props(ctl))
}
