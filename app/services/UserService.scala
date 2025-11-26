package services

import javax.inject._
import repository.UserRepository
import scala.concurrent.{Future, ExecutionContext}

@Singleton
class UserService @Inject()(repo: UserRepository)(implicit ec: ExecutionContext) {

  def validateUser(username: String, password: String): Future[Boolean] =
    repo.findByUsername(username).map {
      case Some((storedHash)) =>
        val hashed = java.security.MessageDigest.getInstance("SHA-256")
          .digest(password.getBytes("UTF-8"))
          .map("%02x".format(_)).mkString

        storedHash == hashed

      case None => false
    }
}

