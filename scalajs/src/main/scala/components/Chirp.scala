package components

import java.util.UUID

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scala.language.existentials

object Chirp {

  case class Props(userId: String, userName: String, uuid: UUID)

  // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("Chirp")
    .renderPC((_, p, c) =>
      <.section(^.className := "fw-wrapper feature",
        <.div(^.className := "row",
          c
        )
      )
    ).build


  def apply(userId: String, userName: String, uuid: UUID, children: VdomNode*) = component(Props(userId, userName, uuid: UUID))(children: _*)
}
//             Chirp(chirp.userId, userName, chirp.uuid, chirp.message)
// case class Chirp(userId: String, message: String, uuid: UUID)