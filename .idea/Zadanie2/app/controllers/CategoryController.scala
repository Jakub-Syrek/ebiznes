// Category Controller
package controllers

import javax.inject._
import models.{NewCategoryItem, CategoryItem}
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import scala.collection.mutable

@Singleton
class CategoryController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  private val categoryList = new mutable.ListBuffer[CategoryItem]()
  categoryList += CategoryItem(1, "Electronics")
  categoryList += CategoryItem(2, "Books")

  implicit val categoryListJson = Json.format[CategoryItem]
  implicit val newCategoryListJson = Json.format[NewCategoryItem]

  // curl localhost:9000/categories
  def getAll(): Action[AnyContent] = Action {
    if (categoryList.isEmpty) NoContent else Ok(Json.toJson(categoryList))
  }

  // curl localhost:9000/categories/1
  def getById(categoryId: Long) = Action {
    val foundCategory = categoryList.find(_.id == categoryId)
    foundCategory match {
      case Some(category) => Ok(Json.toJson(category))
      case None => NotFound
    }
  }

  // curl -v -d '{"name": "Toys"}' -H 'Content-Type: application/json' -X POST localhost:9000/categories
  def addNewCategory() = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val categoryItem: Option[NewCategoryItem] = jsonObject.flatMap(Json.fromJson[NewCategoryItem](_).asOpt)

    categoryItem match {
      case Some(newCategory) =>
        val nextId = categoryList.map(_.id).max + 1
        val toBeAdded = CategoryItem(nextId, newCategory.name)
        categoryList += toBeAdded
        Created(Json.toJson(toBeAdded))
      case None =>
        BadRequest
    }
  }

  // curl -X DELETE localhost:9000/categories/1
  def deleteCategory(categoryId: Long) = Action {
    val foundCategory = categoryList.find(_.id == categoryId)
    foundCategory match {
      case Some(_) =>
        categoryList.filterInPlace(_.id != categoryId)
        Accepted
      case None => NotFound
    }
  }

  // curl -X PUT -d '{"name": "Updated Category"}' -H 'Content-Type: application/json' localhost:9000/categories/1
  def updateCategory(categoryId: Long) = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val updatedCategory: Option[CategoryItem] = jsonObject.flatMap(Json.fromJson[CategoryItem](_).asOpt)

    updatedCategory match {
      case Some(newCategory) =>
        val foundCategory = categoryList.find(_.id == categoryId)

        foundCategory match {
          case Some(_) =>
            categoryList.dropWhileInPlace(_.id == categoryId)
            categoryList += newCategory.copy(id = categoryId)
            Accepted(Json.toJson(newCategory.copy(id = categoryId)))
          case None => NotFound
        }

      case None => BadRequest
    }
  }

  // curl localhost:9000/categories/count
  def getCategoryCount(): Action[AnyContent] = Action {
    Ok(Json.toJson(categoryList.length))
  }

  // curl localhost:9000/categories/search?name=Books
  def getCategoriesByName(name: String) = Action {
    val matchingCategories = categoryList.filter(_.name.toLowerCase == name.toLowerCase)
    if (matchingCategories.isEmpty) NoContent else Ok(Json.toJson(matchingCategories))
  }
}

