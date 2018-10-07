package components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import shared.User

import scala.language.existentials

object AddFriendPage {

  case class Props(user: User)


  protected class Backend($: BackendScope[Props, Unit]) {

    def render(props: Props): VdomElement = {
      <.div(
        <.h2("AddFriendPage")
      )
    }
  }
    // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("AddFriendPage")
    .renderBackend[Backend]
    .build

  def apply(user: User) = component(Props(user))
}
