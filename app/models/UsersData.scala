package models

import play.api.libs.json.Json

/**
  * Created by zdoonio.
  */
case class UsersData (
  userId: Int,
  email: String,
  password: String,
  name: String,
  surname: String,
  role: String
) {

  def create: User = User.create(Some(email), Some(password), Some(name), Some(surname), Some(role))

  def update(id: Int): User = User(id, Some(email), Some(password), Some(name), Some(surname), Some(role))
}

object UsersData {

  implicit val usersDataReads = Json.reads[UsersData]

  implicit val usersDataWrites = Json.writes[UsersData]

  def fromUsers (user: User): UsersData = {
    UsersData(user.id, user.email.getOrElse(""), user.password.getOrElse(""), user.surname.getOrElse(""), user.surname.getOrElse(""), user.role.getOrElse(""))
  }

}
