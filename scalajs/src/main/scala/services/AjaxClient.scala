package services


import org.scalajs.dom

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.typedarray._
import upickle.default._
import shared.User
import upickle.default.{macroRW, ReadWriter => RW}

object AjaxClient {


  // When called with an non existing userId:
  // {"name":"NotFound","detail":"user domi not found"}

  def getUser(userId: String): Future[Option[User]] = {
    dom.ext.Ajax.get("/api/users/" + userId).map(r => {
      if (r == null) {
        None
      } else {
        val user = read[User](r.responseText)
        Some(user)
      }
    })
  }


}
