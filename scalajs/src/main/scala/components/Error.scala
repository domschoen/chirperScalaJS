package components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scala.language.existentials

object Error {

  case class Props(message: String)

  // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("Error")
    .render_P(p =>
        <.div(
          <.span(^.className := "error",p.message)
        )

    ).build


  def apply(message: String) = component(Props(message))
}
