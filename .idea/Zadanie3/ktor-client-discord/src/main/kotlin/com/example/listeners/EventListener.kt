package com.example.listeners

import com.example.models.Category
import com.example.models.Gadget
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

suspend fun makeCall(urlToCall: String): String {
    return HttpClient(CIO).use { client ->
        val response: HttpResponse = client.get(urlToCall)
        response.body()
    }
}

class EventListener : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        super.onMessageReceived(event)
        val message = event.message.contentRaw
        println("Message: $message")

        GlobalScope.launch {
            try {
                when {
                    message == "Show categories" -> {

                        val messageToDecode = makeCall("http://0.0.0.0:8080/category")

                        println(messageToDecode)

                        val categories = Json.decodeFromString<List<Category>>(messageToDecode)
                        var messageFinal = ""

                        for (category in categories) {
                            messageFinal += "${category.name}\n"
                        }

                        event.channel.sendMessage(messageFinal).queue()
                    }


                    message.startsWith("Show category ") -> {

                        val categoriesString = makeCall("http://0.0.0.0:8080/category")
                        val categories = Json.decodeFromString<List<Category>>(categoriesString)
                        val categoryName = message.removePrefix("Show category ")
                        val category = categories.find { it.name == categoryName }


                        if (category == null) {
                            event.channel.sendMessage("No category with this name.").queue()
                            return@launch
                        }

                        val messageToDecode = makeCall("http://0.0.0.0:8080/category/${category.id}")
                        val gadgets = Json.decodeFromString<List<Gadget>>(messageToDecode)
                        var messageFinal = ""
                        for (gadget in gadgets) {
                            messageFinal += "${gadget.name}\n"
                        }
                        event.channel.sendMessage(messageFinal).queue()
                    }
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
                event.channel.sendMessage("Error: ${e.message}").queue()
            }
        }
    }
}
