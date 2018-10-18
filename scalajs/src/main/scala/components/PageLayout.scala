package components
import client.Main._
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
import org.scalajs.dom.Event

import scala.util.{Failure, Random, Success}
import scala.language.existentials
import org.scalajs.dom

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.typedarray._
import upickle.default._
import shared.User
import upickle.default.{macroRW, ReadWriter => RW}
import org.scalajs.dom.ext.AjaxException
import dom.ext.Ajax
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import shared.Keys

import util._

object PageLayout {

  case class Props(router: RouterCtl[Loc], user: Option[User], showSignup: Boolean, logout: ReactEventFromInput => Callback)


    // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("PageLayout")
    .renderPC((_, props, c) => {
      val button: VdomElement = props.user match  {
        case Some(user) => <.a(^.className := "btn", ^.href :="#", ^.onClick ==> props.logout, "Logout")
        case None => if (props.showSignup) {
          props.router.link(SignupLoc)("Sign up", ^.className := "btn")
        } else {
          props.router.link(LoginLoc)("Login", ^.className := "btn")
        }
      }
      val links: VdomElement = props.user match  {
        case Some(user) => <.div(^.className := "tertiary-nav",
          props.router.link(AddFriendLoc)("Add Friend"),
          props.router.link(LoginLoc)("Feed"),
          props.router.link(UserChirpLoc(user.userId))(user.name)
        )
        case None =>
          <.div("")

      }

      <.div(^.id := "clipped",
        <.div(^.id := "site-header",
          <.div(^.className := "row",
            <.div(^.className := "small-3 columns",props.router.link(LoginLoc)("Chirper", ^.id := "logo")),
            <.div(^.className := "small-9 columns",
              <.nav(
                <.div(^.className := "tertiary-nav",
                  links
                ),
                <.div(^.className := "primary-nav",
                  button
                )
              )
            )
          )
        ),
        c
      )
    }
    ).build



  def apply(router: RouterCtl[Loc], user: Option[User], showSignup: Boolean, logout: ReactEventFromInput => Callback,  children: VdomNode*) =
    component(Props(router,user,showSignup,logout))(children: _*)
}
