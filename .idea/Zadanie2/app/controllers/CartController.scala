// Cart Controller
package controllers

import javax.inject._
import models.{NewCartItem, CartItem}
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import scala.collection.mutable

@Singleton
class CartController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  private val cartList = new mutable.ListBuffer[CartItem]()
  cartList += CartItem(1, 1, 2)
  cartList += CartItem(2, 3, 1)

  implicit val cartListJson = Json.format[CartItem]
  implicit val newCartListJson = Json.format[NewCartItem]

  // curl localhost:9000/cart
  def getAll(): Action[AnyContent] = Action {
    if (cartList.isEmpty) NoContent else Ok(Json.toJson(cartList))
  }

  // curl localhost:9000/cart/1
  def getById(cartId: Long) = Action {
    val foundCartItem = cartList.find(_.id == cartId)
    foundCartItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
    }
  }

  // curl -v -d '{"productId": 4, "quantity": 3}' -H 'Content-Type: application/json' -X POST localhost:9000/cart
  def addCartItem() = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val cartItem: Option[NewCartItem] = jsonObject.flatMap(Json.fromJson[NewCartItem](_).asOpt)

    cartItem match {
      case Some(newItem) =>
        val nextId = cartList.map(_.id).max + 1
        val toBeAdded = CartItem(nextId, newItem.productId, newItem.quantity)
        cartList += toBeAdded
        Created(Json.toJson(toBeAdded))
      case None =>
        BadRequest
    }
  }

  // curl -X DELETE localhost:9000/cart/1
  def deleteCartItem(cartId: Long) = Action {
    val foundCartItem = cartList.find(_.id == cartId)
    foundCartItem match {
      case Some(_) =>
        cartList.filterInPlace(_.id != cartId)
        Accepted
      case None => NotFound
    }
  }
}