package com.example

import com.example.listeners.EventListener
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import net.dv8tion.jda.api.OnlineStatus


lateinit var shardManager: ShardManager

fun buildBot(){
    val builder = DefaultShardManagerBuilder.createDefault("MTA4OTYyMDYyNTM3NjY3Mzg1Mg.GbFXso.caTuvTIGpOzLAW5qPJtX2TQpx35Sp50uInXIqE")
        .enableIntents(GatewayIntent.MESSAGE_CONTENT)
    builder.setStatus(OnlineStatus.ONLINE)

    builder.setActivity(Activity.playing("Gadget sale!!!"));

    shardManager = builder.build()
    val eventListener: EventListener = EventListener()
    shardManager.addEventListener(eventListener)

}

fun main() {


    buildBot()
    println("Chatbot initialized")
}