package models

import slick.jdbc.MySQLProfile.api._

/**
 * Represents an image stored in the database.
 */
case class DbImage(
                    id: Long,
                    fileName: String,
                    contentType: String,
                    imageData: Array[Byte]
                  )

/**
 * Slick table mapping for uploaded_images.
 */
class ImageTable(tag: Tag) extends Table[DbImage](tag, "uploaded_images") {

  def id          = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def fileName    = column[String]("file_name")
  def contentType = column[String]("content_type")
  def imageData   = column[Array[Byte]]("image_data")

  def * = (id, fileName, contentType, imageData) <> ((DbImage.apply _).tupled, DbImage.unapply)
}

object ImageTable {
  val query = TableQuery[ImageTable]
}
