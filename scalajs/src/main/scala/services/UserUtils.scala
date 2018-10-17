package services

import japgolly.scalajs.react.Callback
import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import shared.User
import upickle.default.read

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import upickle.default._
import shared.User
import upickle.default.{macroRW, ReadWriter => RW}

object UserUtils {


  def getUser(userId: String, onSuccess: User => Callback, onFailure: Callback) : Future[Callback] = {
    val request = Ajax.get("/api/users/" + userId).recover {
      // Recover from a failed error code into a successful future
      case dom.ext.AjaxException(req) => req
    }.map( r =>
      r.status match {
        case 200 =>
          val user = read[User](r.responseText)
          onSuccess(user)
        case _ =>
          onFailure
      }
    )
    request
  }

}
