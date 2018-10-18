package components

import java.util.UUID

import client.Main.{Loc, UserChirpLoc}
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._

import scala.language.existentials

object Chirp {

  case class Props(router: RouterCtl[Loc],userId: String, userName: String, uuid: UUID)

  // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("Chirp")
    .renderPC((_, p, c) =>
      <.div(^.className := "chirp",
        <.h3(^.className := "fw-chirpUser",
          p.router.link(UserChirpLoc(p.userId))(p.userName)
        ),
        c,
        <.hr()
      )
    ).build


  def apply(router: RouterCtl[Loc],userId: String, userName: String, uuid: UUID, children: VdomNode*) =
    component(Props(router,userId, userName, uuid: UUID))(children: _*)
}
//             Chirp(chirp.userId, userName, chirp.uuid, chirp.message)
// case class Chirp(userId: String, message: String, uuid: UUID)