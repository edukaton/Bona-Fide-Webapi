package models

import play.api.libs.json.Json

/**
  * Created by zdoonio.
  */
case class UsersData (
  id: Int,
  email: String,
  password: String,
  name: String,
  role: String
) {

  def create: User = User.create(email, password, name, role)

  def update(id: Int): User = User(id, email, password, name, role)
}

object UsersData {

  implicit val usersDataReads = Json.reads[UsersData]

  implicit val usersDataWrites = Json.writes[UsersData]

  def fromUsers (user: User): UsersData = {
    UsersData(user.userId, user.email, user.password, user.name, user.role)
  }

}
