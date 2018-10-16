package components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import shared.{Keys, PostedMessage, User}
import upickle.default._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.existentials

object ChirpForm {

  case class Props()
  case class State(message: Option[String])


  protected class Backend($: BackendScope[Props, State]) {

    // user has entered the user Id and submit it
    def handleSubmit(p: Props, s: State, e: ReactEventFromInput): Callback = {
      e.preventDefaultCB >> {
        s.message match {
          case Some(text) =>
            val trimText: String =  text.trim()
            if (trimText.length > 0) {
              val userId = dom.window.localStorage.getItem(Keys.userIdKey)
              val postedMessage = PostedMessage(userId,trimText)
              val postUrl = "/api/chirps/live/" + userId
              val request = Ajax.post(
                url = postUrl,
                data = write(postedMessage)
              ).recover {
                // Recover from a failed error code into a successful future
                case dom.ext.AjaxException(req) => req
              }.map( r =>
                r.status match {
                  case 200 =>
                    $.modState(_.copy(message = None))
                  case _ =>
                    println("ChirpForm | url " +  postUrl + " error: " + r.status + " with message: " + r.responseText)
                    Callback.empty
                }
              )
              Callback.future(request)
            } else Callback.empty

          case None =>
            Callback.empty
        }
      }
    }

    def handleMessageChange(e: ReactEventFromInput) = {
      val newMessage = if (e.target.value == null) None else Some(e.target.value)
      $.modState(_.copy(message = newMessage))
    }

    def render(props: Props, s: State): VdomElement = {
      val valueString = if (s.message.isDefined) s.message.get else ""
      println("ChirpForm | render with value " + valueString)
      <.form(^.className := "chirpForm", ^.onSubmit ==> { e: ReactEventFromInput => handleSubmit(props, s, e)},
        <.input.text(^.placeholder := "Say something...",^.maxLength := 140,  ^.value := valueString,
          ^.onChange ==> { e: ReactEventFromInput => handleMessageChange(e)}),
        <.input.submit(^.value := "Post")
      )

    }
  }
    // create the React component for Dashboard
  private val component = ScalaComponent.builder[Props]("ChirpForm")
    .initialState(State(None))
    .renderBackend[Backend]
    .build

  def apply() = component(Props())
}
