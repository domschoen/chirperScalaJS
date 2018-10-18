package components

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

import scala.util.Random
import scala.language.existentials

object Section {

  case class Props()

  // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("Section")
    .renderPC((_, p, c) =>
      <.section(^.className := "fw-wrapper feature",
        <.div(^.className := "row",
          c
        )
      )
    ).build


  def apply(children: VdomNode*) = component(Props())(children: _*)
}
