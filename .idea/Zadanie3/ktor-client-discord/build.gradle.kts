val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.20"
    id("io.ktor.plugin") version "2.2.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    dependencies {
        implementation("io.ktor:ktor-client-cio:$ktor_version")
        implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
        implementation("ch.qos.logback:logback-classic:$logback_version")
        testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
        implementation("net.dv8tion:JDA:5.0.0-alpha.18")
        implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
        implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
        implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
        implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
        implementation("io.ktor:ktor-client-core:$ktor_version")


    }
}