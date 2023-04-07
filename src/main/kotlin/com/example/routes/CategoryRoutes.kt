package com.example.routes

import com.example.categoryStorage
import com.example.models.Gadget
import com.example.gadgetStorage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.categoryRouting() {
    route("/category") {
        get {
            if (categoryStorage.isNotEmpty()){
                call.respond(categoryStorage)
            } else {
                call.respondText("No categories found", status = HttpStatusCode.OK)
            }
        }
        get("{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing id",
                status = HttpStatusCode.BadRequest
            )
            val category =
                categoryStorage.find { it.id == id } ?: return@get call.respondText(
                    "No category with id $id",
                    status = HttpStatusCode.NotFound
                )
            val consoles = gadgetStorage.filter {it.category == category} ?: return@get call.respondText(
                "No consoles in ${category.name}",
                status = HttpStatusCode.NotFound
            )
            call.respond(consoles)
        }
    }
}