package controllers

import javax.inject._

import models.{User, UsersData}
import play.api.libs.json.Json
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class UserController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def getUsers = Action { implicit request =>
    val users = User.findAll()
    val json = Json.obj("users" -> users.map { user =>
      val userData = UsersData.fromUsers(user)
      Json.toJson(userData)
    })
    Ok(json)
  }

  def getUser(id: Int) = Action { implicit request =>
    User.find(id) match {
      case Some(users) =>
        val usersData = UsersData.fromUsers(users)
        val json = Json.toJson(usersData)
        Ok(json)

      case None => NotFound(Json.obj("error" -> "Not found!"))
    }
  }

  def createUser = Action(parse.json) { implicit request =>
    request.body.validate[UsersData].fold (
      error => BadRequest(Json.obj("error" -> "Json was not correct")),
      usersData => {
        val usersCreateData = usersData.create
        Created.withHeaders(LOCATION -> "Created")
      }
    )
  }

  def editUser(id: Int) = Action(parse.json) { implicit request =>
    request.body.validate[UsersData].fold(
      error => BadRequest(Json.obj("error" -> "Json was not correct")),
      usersData => {
        User.find(id) match {
          case Some(user) => usersData.update(usersData.userId); NoContent

          case None => NotFound(Json.obj("error" -> "Not found!"))
        }
      })
  }


    def deleteUser(id: Int) = Action { implicit request =>
      User.find(id) map { user =>
        user.destroy()
        NoContent
      } getOrElse NotFound(Json.obj("error" -> "Not found!"))
    }
}
