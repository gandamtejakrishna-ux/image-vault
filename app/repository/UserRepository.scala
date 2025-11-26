package repository

import javax.inject._
import slick.jdbc.MySQLProfile.api._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{Future, ExecutionContext}

class UserRepository @Inject()(
                                dbConfigProvider: DatabaseConfigProvider
                              )(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val db = dbConfig.db

  class Users(tag: Tag) extends Table[(Long, String, String)](tag, "users") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def passwordHash = column[String]("password_hash")

    def * = (id, username, passwordHash)
  }

  private val users = TableQuery[Users]

  def findByUsername(username: String): Future[Option[String]] =
    db.run(users.filter(_.username === username).map(_.passwordHash).result.headOption)
}

