package shared


import upickle.default.{ReadWriter => RW, macroRW}


object Keys  {
  val userIdKey = "userId"
}


case class User(userId: String, name: String, friends: List[String])
object User{
  implicit def rw: RW[User] = macroRW
}


