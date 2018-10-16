package shared


import upickle.default.{ReadWriter => RW, macroRW}


object Keys  {
  val userIdKey = "userId"
}


case class User(userId: String, name: String, friends: List[String])
object User{
  implicit def rw: RW[User] = macroRW
}

case class PostedMessage(userId: String, message: String)
object PostedMessage{
  implicit def rw: RW[PostedMessage] = macroRW
}

case class Chirp(userId: String, name: String, friends: List[String])
object Chirp{
  implicit def rw: RW[Chirp] = macroRW
}
