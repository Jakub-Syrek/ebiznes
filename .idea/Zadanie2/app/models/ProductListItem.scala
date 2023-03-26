package models

case class ProductListItem(id: Long, description: String, isItDone: Boolean)
case class NewProductListItem(description: String)