package shared


import java.util.UUID

import upickle.default.{macroRW, ReadWriter => RW}


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


// Chirp message receive with WS:
// {"userId":"Jean","message":"This is my new message","timestamp":1539760786.932000000,"uuid":"138036c1-97ab-4dac-a6b2-3fa8f3572c57"}	1539760894.5932655
case class Chirp(userId: String, message: String, uuid: UUID)
object Chirp{
  implicit def rw: RW[Chirp] = macroRW
}


case class StreamForUsers(userIds: List[String])
object StreamForUsers{
  implicit def rw: RW[StreamForUsers] = macroRW
}
