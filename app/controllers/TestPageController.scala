package controllers

import javax.inject._
import play.api.mvc._
import java.nio.file.{Files => JFiles, Paths, Path}
import scala.jdk.CollectionConverters._

/**
 * Serves a static HTML file located under one of several candidate paths.
 *
 * This is resilient for single-project or multi-module layouts.
 */
@Singleton
class TestPageController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  private val candidatePaths: Seq[Path] = Seq(
    // common module layout (your earlier path)
    Paths.get("app/views/image_test.html"),
    // app-level views
    Paths.get("app/views/image_test.html"),
    // public (if you later move it)
    //Paths.get("public/image_test.html"),
    // alternate module layout
    //Paths.get("modules/images/views/image_test.html"),
    //Paths.get("modules/images/app/views/image_test.html")
  )

  /**
   * Serves the test page. Tries several filesystem locations and returns the first file found.
   */
  def testPage: Action[AnyContent] = Action {
    candidatePaths.find(p => JFiles.exists(p) && JFiles.isRegularFile(p)) match {
      case Some(found) =>
        val bytes = JFiles.readAllBytes(found)
        Ok(new String(bytes, "UTF-8")).as(HTML)

      case None =>
        val attempted = candidatePaths.map(_.toString).mkString("\n - ")
        NotFound(
          s"""
             |<html><body>
             |<h3>Test page not found</h3>
             |<p>Tried the following paths (none exist):</p>
             |<pre> - $attempted</pre>
             |<p>Please create your <code>image_test.html</code> file in one of these locations, or update the controller.</p>
             |</body></html>
          """.stripMargin
        ).as(HTML)
    }
  }
}
