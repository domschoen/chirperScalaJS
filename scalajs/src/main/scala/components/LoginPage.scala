package components

import diode.data.Pot
import diode.react._
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
import util._

object LoginPage {

  case class Props()
  case class State(loginChecked: Boolean, user: Option[User])


  protected class Backend($: BackendScope[Props, State]) {


    def mounted(p: Props) = {
      println("LoginPage mounted")
      val userId = dom.window.localStorage.getItem(Keys.userIdKey)
      if (userId != null) {
        Callback.future(
          AjaxClient.getUser(userId).map( u =>
            u match {
              case Some (fUser) =>
                $.modState({sta:State => sta.copy(loginChecked = true, user = Some(fUser))})
              case None =>
                dom.window.localStorage.removeItem(Keys.userIdKey)
                $.modState({sta:State => sta.copy(loginChecked = true)})
            }
          )
        )
        } else {
        $.modState({sta:State => sta.copy(loginChecked = true)})
      }
    }

    def render(props: Props, s: State): VdomElement = {
      println("render " + s)
      if (s.loginChecked) {
        s.user match {
          case Some(user) => {
            // <PageLayout showSignup={true}>
            // <ContentLayout subtitle="Login">
            //                            <LoginForm onLogin={this.handleLogin}/>
            //                        </ContentLayout>
            ActivityStream(user)
          }
          case None =>  {
            // <PageLayout user={this.state.user} logout={this.logout}>
            //                        {this.props.children}
            //                    </PageLayout>
            println("Install SignUpPage")
            SignUpPage()
          }
        }
      } else {
        <.div(^.className :="loading")
      }
    }
  }
  // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("LoginPage")
    .initialState(State(false, None))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply() = {
    println("create Login Page")
    component(Props())
  }
}
