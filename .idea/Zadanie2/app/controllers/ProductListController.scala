package controllers

import javax.inject._
import models.{NewProductListItem, ProductListItem}
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import scala.collection.mutable

@Singleton
class ProductListController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  private val productList = new mutable.ListBuffer[ProductListItem]()
  productList += ProductListItem(1, "test", true)
  productList += ProductListItem(2, "some other value", false)

  implicit val productListJson = Json.format[ProductListItem]
  implicit val newProductListJson = Json.format[NewProductListItem]

  // curl localhost:9000/products
  def getAll(): Action[AnyContent] = Action {
    if (productList.isEmpty) NoContent else Ok(Json.toJson(productList))
  }

  // curl localhost:9000/products/1
  def getById(itemId: Long) = Action {
    val foundItem = productList.find(_.id == itemId)
    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
    }
  }

  // curl -X PUT -d '{"description": "updated product", "isItDone": true}' -H 'Content-Type: application/json' localhost:9000/products/1
  def updateProduct(itemId: Long) = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val updatedProduct: Option[ProductListItem] = jsonObject.flatMap(Json.fromJson[ProductListItem](_).asOpt)

    updatedProduct match {
      case Some(newProduct) =>
        val foundItem = productList.find(_.id == itemId)

        foundItem match {
          case Some(_) =>
            productList.dropWhileInPlace(_.id == itemId)
            productList += newProduct.copy(id = itemId)
            Accepted(Json.toJson(newProduct.copy(id = itemId)))
          case None => NotFound
        }

      case None => NotFound
    }
  }


  // curl -X DELETE localhost:9000/products/done
  def deleteAllDone() = Action {
    productList.filterInPlace(_.isItDone == false)
    Accepted
  }

  // curl -v -d '{"description": "some new item"}' -H 'Content-Type: application/json' -X POST localhost:9000/products
  def addNewItem() = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val productListItem: Option[NewProductListItem] = jsonObject.flatMap(Json.fromJson[NewProductListItem](_).asOpt)

    productListItem match {
      case Some(newItem) =>
        val nextId = productList.map(_.id).max + 1
        val toBeAdded = ProductListItem(nextId, newItem.description, false)
        productList += toBeAdded
        Created(Json.toJson(toBeAdded))
      case None =>
        BadRequest
    }
  }
}
