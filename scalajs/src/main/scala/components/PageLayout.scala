package components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

import scala.language.existentials

import shared.User
import  client.Main.{Loc, LoginLoc, SignupLoc, AddFriendLoc}
import japgolly.scalajs.react.extra.router.RouterCtl

object PageLayout {

  case class Props(router: RouterCtl[Loc], user: Option[User], showSignup: Boolean, logout: Callback)


    // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("PageLayout")
    .renderPC((_, props, c) => {
      val button: VdomElement = props.user match  {
        case Some(user) => <.a(^.className := "btn", ^.href :="#", ^.onClick --> props.logout, "Logout")
        case None => if (props.showSignup) {
          props.router.link(SignupLoc)("Sign up", ^.className := "btn")
        } else {
          props.router.link(LoginLoc)("Login", ^.className := "btn")
        }
      }
      val links: VdomElement = props.user match  {
        case Some(user) => <.div(^.className := "tertiary-nav",
          props.router.link(AddFriendLoc)("Add Friend")
          //<Link to="/addFriend">Add Friend</Link>,
          //<Link to="/">Feed</Link>,
          //<Link to={"/users/" + this.props.user.userId }>{this.props.user.name}</Link>
      )

      }

      <.div(^.id := "clipped",
        <.div(^.id := "site-header",
          <.div(^.className := "row",
            <.div(^.className := "small-3 columns",props.router.link(LoginLoc)("Chirper", ^.id := "logo")),
            <.div(^.className := "small-9 columns",
              <.nav(
                <.div(^.className := "tertiary-nav",
                  <.div(^.className := "tertiary-nav")
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



  def apply(router: RouterCtl[Loc], user: Option[User], showSignup: Boolean, logout: Callback,  children: VdomNode*) =
    component(Props(router,user,showSignup,logout))(children: _*)
}
