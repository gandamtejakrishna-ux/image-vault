package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.Files.TemporaryFile
import scala.concurrent.{ExecutionContext, Future}
import java.nio.file.{Files => JFiles, Paths}
import services.ImageService

/**
 * Controller for image upload, retrieval, and serving test HTML page.
 */
@Singleton
class ImageController @Inject()(
                                 cc: ControllerComponents,
                                 imageService: ImageService
                               )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  /**
   * Serves image_test.html from app/views/ directory.
   */
  def testPage: Action[AnyContent] = Action {
    val path = Paths.get("app/views/image_test.html")
    val bytes = JFiles.readAllBytes(path)
    Ok(new String(bytes, "UTF-8")).as(HTML)
  }

  /**
   * Uploads an image via multipart/form-data.
   * POST /image
   */
  def uploadImage: Action[MultipartFormData[TemporaryFile]] =
    Action.async(parse.multipartFormData) { request =>
      request.body.file("image") match {
        case Some(filePart) =>
          val temp: TemporaryFile = filePart.ref
          val file = temp.path.toFile
          val fileName = filePart.filename
          val mimeType = filePart.contentType.getOrElse("application/octet-stream")
          val bytes = JFiles.readAllBytes(file.toPath)

          imageService.saveImage(fileName, mimeType, bytes).map { id =>
            Ok(s"""{"image_id": $id}""").as("application/json")
          }

        case None =>
          Future.successful(
            BadRequest("""{"error":"missing file"}""").as("application/json")
          )
      }
    }

  /**
   * Fetch image by ID.
   * GET /image/:id
   */
  def getImage(id: Long): Action[AnyContent] = Action.async {
    imageService.fetchImage(id).map {
      case Some((bytes, mime)) => Ok(bytes).as(mime)
      case None => NotFound("""{"error":"Image not found"}""")
    }
  }

  /**
   * GET /image  → Show basic instructions when user directly opens /image
   */
  def imageForm: Action[AnyContent] = Action {
    Ok("<html><body><h3>Use POST /image to upload. Use GET /image/{id} to fetch.</h3></body></html>").as(HTML)
  }
}
