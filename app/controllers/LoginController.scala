package controllers

import javax.inject._
import play.api.mvc._
import services.UserService
import scala.concurrent.{Future, ExecutionContext}

/**
 * Handles login and session authentication.
 */
@Singleton
class LoginController @Inject()(
                                 cc: ControllerComponents,
                                 userService: UserService
                               )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  /** Render login page (GET /) */
  def loginPage: Action[AnyContent] = Action {
    Ok(views.html.login())
  }

  /** Process login form */
  def doLogin: Action[AnyContent] = Action.async { implicit request =>
    val formData = request.body.asFormUrlEncoded

    val username = formData.flatMap(_.get("username")).flatMap(_.headOption).getOrElse("")
    val password = formData.flatMap(_.get("password")).flatMap(_.headOption).getOrElse("")

    userService.validateUser(username, password).map {
      case true =>
        Redirect("/test-page").withSession("user" -> username)

      case false =>
        Unauthorized("""
          <h3>Invalid username or password</h3>
          <a href="/">Try again</a>
        """).as(HTML)
    }
  }

  /** Logout */
  def logout: Action[AnyContent] = Action {
    Redirect("/").withNewSession
  }
}
