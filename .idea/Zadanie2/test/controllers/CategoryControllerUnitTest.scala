import controllers.{CategoryController}
import models.{CategoryItem, NewCategoryItem}
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test._

class CategoryControllerUnitTest extends PlaySpec with GuiceOneAppPerTest with Injecting {
  implicit val categoryItemJson = Json.format[CategoryItem]
  implicit val newCategoryItemJson = Json.format[NewCategoryItem]

  "CategoryController" should {
    "return all categories" in {
      val controller = new CategoryController(stubControllerComponents())
      val getAll = controller.getAll().apply(FakeRequest(GET, "/categories"))

      status(getAll) mustBe OK
      contentType(getAll) mustBe Some("application/json")
    }

    "return a category by id" in {
      val controller = new CategoryController(stubControllerComponents())
      val getById = controller.getById(1).apply(FakeRequest(GET, "/categories/1"))

      status(getById) mustBe OK
      contentType(getById) mustBe Some("application/json")
    }

    "return NotFound when category is not found" in {
      val controller = new CategoryController(stubControllerComponents())
      val getById = controller.getById(999).apply(FakeRequest(GET, "/categories/999"))

      status(getById) mustBe NOT_FOUND
    }

    "add a new category" in {
      val newCategoryJson = Json.obj("name" -> "new category")

      val controller = new CategoryController(stubControllerComponents())
      val addNewCategory = controller.addNewCategory().apply(
        FakeRequest(POST, "/categories")
          .withJsonBody(newCategoryJson)
          .withHeaders("Content-Type" -> "application/json")
      )

      status(addNewCategory) mustBe CREATED
      contentType(addNewCategory) mustBe Some("application/json")
    }

    "return BadRequest when adding an invalid category" in {
      val invalidCategoryJson = Json.obj("invalid_field" -> "invalid value")

      val controller = new CategoryController(stubControllerComponents())
      val addNewCategory = controller.addNewCategory().apply(
        FakeRequest(POST, "/categories")
          .withJsonBody(invalidCategoryJson)
          .withHeaders("Content-Type" -> "application/json")
      )

      status(addNewCategory) mustBe BAD_REQUEST
    }

    "delete a category" in {
      val controller = new CategoryController(stubControllerComponents())
      val deleteCategory = controller.deleteCategory(1).apply(FakeRequest(DELETE, "/categories/1"))

      status(deleteCategory) mustBe ACCEPTED
    }

    "return NotFound when trying to delete a non-existent category" in {
      val controller = new CategoryController(stubControllerComponents())
      val deleteCategory = controller.deleteCategory(999).apply(FakeRequest(DELETE, "/categories/999"))

      status(deleteCategory) mustBe NOT_FOUND
    }
  }
}
