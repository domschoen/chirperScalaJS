package components

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

import scala.util.Random
import scala.language.existentials

object ContentLayout {

  case class Props(subtitle: String)

  // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("ContentLayout")
    .renderPC((_, p, c) =>
      <.div(^.id := "page-content",
        <.section(^.id := "top",
          <.div(^.className := "row",
            <.header(^.className := "large-12 columns",
              <.h1(p.subtitle)
            )
          )
        ),
        c
      )
    ).build


  def apply(subtitle: String, children: VdomNode*) = component(Props(subtitle))(children: _*)
}
