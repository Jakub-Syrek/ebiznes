package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test._

class CartControllerUnitTest extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "CartController getAll" should {
    "return all cart items" in {
      val controller = new CartController(stubControllerComponents())
      val getAll = controller.getAll().apply(FakeRequest(GET, "/cart"))

      status(getAll) mustBe OK
      contentType(getAll) mustBe Some("application/json")
    }
  }

  "CartController getById" should {
    "return a cart item by id" in {
      val controller = new CartController(stubControllerComponents())
      val getCartItem = controller.getById(1).apply(FakeRequest(GET, "/cart/1"))

      status(getCartItem) mustBe OK
      contentType(getCartItem) mustBe Some("application/json")
    }

    "return NotFound when cart item is not found" in {
      val controller = new CartController(stubControllerComponents())
      val getCartItem = controller.getById(999).apply(FakeRequest(GET, "/cart/999"))

      status(getCartItem) mustBe NOT_FOUND
    }
  }

  "CartController addCartItem" should {
    "add a new cart item" in {
      val newCartItemJson = Json.obj("productId" -> 5, "quantity" -> 2)

      val controller = new CartController(stubControllerComponents())
      val addCartItem = controller.addCartItem().apply(
        FakeRequest(POST, "/cart")
          .withJsonBody(newCartItemJson)
          .withHeaders("Content-Type" -> "application/json")
      )

      status(addCartItem) mustBe CREATED
      contentType(addCartItem) mustBe Some("application/json")
    }

    "return BadRequest when adding an invalid cart item" in {
      val invalidCartItemJson = Json.obj("invalid_field" -> "invalid value")

      val controller = new CartController(stubControllerComponents())
      val addCartItem = controller.addCartItem().apply(
        FakeRequest(POST, "/cart")
          .withJsonBody(invalidCartItemJson)
          .withHeaders("Content-Type" -> "application/json")
      )

      status(addCartItem) mustBe BAD_REQUEST
    }
  }

  "CartController deleteCartItem" should {
    "delete a cart item" in {
      val controller = new CartController(stubControllerComponents())
      val deleteCartItem = controller.deleteCartItem(1).apply(FakeRequest(DELETE, "/cart/1"))

      status(deleteCartItem) mustBe ACCEPTED
    }

    "return NotFound when trying to delete a non-existent cart item" in {
      val controller = new CartController(stubControllerComponents())
      val deleteCartItem = controller.deleteCartItem(999).apply(FakeRequest(DELETE, "/cart/999"))

      status(deleteCartItem) mustBe NOT_FOUND
    }
  }
}

