package repository

import models.{DbImage, ImageTable}
import javax.inject._
import slick.jdbc.MySQLProfile.api._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}

/**
 * Repository for DB operations.
 */
@Singleton
class ImageRepository @Inject()(
                                 dbConfigProvider: DatabaseConfigProvider
                               )(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  private val db = dbConfig.db

  private val images = ImageTable.query

  /** Save an image to DB. */
  def insertImage(fileName: String, contentType: String, bytes: Array[Byte]): Future[Long] = {
    val entity = DbImage(0, fileName, contentType, bytes)
    db.run((images returning images.map(_.id)) += entity)
  }

  /** Fetch an image by ID. */
  def getImage(id: Long): Future[Option[(Array[Byte], String)]] = {
    db.run(images.filter(_.id === id).result.headOption)
      .map(_.map(img => (img.imageData, img.contentType)))
  }
}
