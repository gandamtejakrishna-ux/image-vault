package services

import repository.ImageRepository
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

/**
 * Service layer for images.
 */
@Singleton
class ImageService @Inject()(
                              imageRepository: ImageRepository
                            )(implicit ec: ExecutionContext) {

  def saveImage(name: String, contentType: String, bytes: Array[Byte]): Future[Long] =
    imageRepository.insertImage(name, contentType, bytes)

  def fetchImage(id: Long): Future[Option[(Array[Byte], String)]] =
    imageRepository.getImage(id)
}
