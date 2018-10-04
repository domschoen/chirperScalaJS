package controllers

import java.nio.ByteBuffer

import boopickle.Default._
import com.google.inject.Inject
import play.api.{Configuration, Environment}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global


class Application @Inject() (implicit val config: Configuration, env: Environment) extends Controller {

  def index = Action {
    Ok(views.html.index("Lagom Scala.js react"))
  }


  def logging = Action(parse.anyContent) {
    implicit request =>
      request.body.asJson.foreach { msg =>
        println(s"CLIENT - $msg")
      }
      Ok("")
  }
}
