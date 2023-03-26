package controllers

import controllers.{ProductListController}
import models.{NewProductListItem, ProductListItem}
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test._
import play.api.libs.json.{Json, Reads, Writes}

class ProductListControllerUnitTest extends PlaySpec with GuiceOneAppPerTest with Injecting {

  implicit val productListReads: Reads[ProductListItem] = Json.reads[ProductListItem]

  "ProductListController getAll" should {
    "return all products" in {
      val controller = new ProductListController(stubControllerComponents())
      val getAll = controller.getAll().apply(FakeRequest(GET, "/products"))

      status(getAll) mustBe OK
      contentType(getAll) mustBe Some("application/json")
    }
  }

  "ProductListController getById" should {
    "return a product by id" in {
      val controller = new ProductListController(stubControllerComponents())
      val getProduct = controller.getById(1).apply(FakeRequest(GET, "/products/1"))

      status(getProduct) mustBe OK
      contentType(getProduct) mustBe Some("application/json")
    }

    "return NotFound when product is not found" in {
      val controller = new ProductListController(stubControllerComponents())
      val getProduct = controller.getById(999).apply(FakeRequest(GET, "/products/999"))

      status(getProduct) mustBe NOT_FOUND
    }
  }

  "ProductListController updateProduct" should {
    "updateProduct" should {
      "return NotFound if the product with the given itemId does not exist" in {
        val nonExistentItemId = 999 // Make sure this ID doesn't exist in the productList
        val updatedProductJson = Json.obj(
          "id" -> nonExistentItemId,
          "description" -> "updated product",
          "isItDone" -> true
        )

        val controller = new ProductListController(stubControllerComponents())
        val updateItem = controller.updateProduct(nonExistentItemId).apply(
          FakeRequest(PUT, s"/products/$nonExistentItemId")
            .withJsonBody(updatedProductJson)
        )

        status(updateItem) mustBe NOT_FOUND
      }

      "update the product with the given itemId" in {
        val itemId = 1 // Assuming there's a product with ID 1 in the productList
        val updatedProductJson = Json.obj(
          "id" -> itemId,
          "description" -> "updated product",
          "isItDone" -> true
        )

        val controller = new ProductListController(stubControllerComponents())
        val updateItem = controller.updateProduct(itemId).apply(
          FakeRequest(PUT, s"/products/$itemId")
            .withJsonBody(updatedProductJson)
        )

        status(updateItem) mustBe ACCEPTED

        val updatedProduct = contentAsJson(updateItem).as[ProductListItem]
        updatedProduct.id mustBe itemId
        updatedProduct.description mustBe "updated product"
        updatedProduct.isItDone mustBe true
      }
    }

  }

  "ProductListController addNewItem" should {
    "add a new product" in {
      val newProductJson = Json.obj("description" -> "new product")

      val controller = new ProductListController(stubControllerComponents())
      val addNewItem = controller.addNewItem().apply(
        FakeRequest(POST, "/products")
          .withJsonBody(newProductJson)
          .withHeaders("Content-Type" -> "application/json")
      )

      status(addNewItem) mustBe CREATED
      contentType(addNewItem) mustBe Some("application/json")
    }

    "return BadRequest when adding an invalid product" in {
      val invalidProductJson = Json.obj("invalid_field" -> "invalid value")

      val controller = new ProductListController(stubControllerComponents())
      val addNewItem = controller.addNewItem().apply(
        FakeRequest(POST, "/products")
          .withJsonBody(invalidProductJson)
          .withHeaders("Content-Type" -> "application/json")
      )

      status(addNewItem) mustBe BAD_REQUEST
    }
  }
}
