package controllers

import java.nio.ByteBuffer

import com.google.inject.Inject
import play.api.{Configuration, Environment}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global


class Application @Inject() (implicit val config: Configuration, env: Environment) extends Controller {

  def index = Action {
    Ok(views.html.index("Chirper"))
  }


  def logging = Action(parse.anyContent) {
    implicit request =>
      request.body.asJson.foreach { msg =>
        println(s"CLIENT - $msg")
      }
      Ok("")
  }


  def userStream(userId: String) = Action {
    Ok(views.html.index("Chirper"))
  }

  def circuitBreaker = Action {
    //Ok(views.html.circuitbreaker.render())
    Ok(views.html.index("Chirper"))
  }
}
