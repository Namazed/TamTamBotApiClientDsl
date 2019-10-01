import java.net.URI

plugins {
    kotlin("jvm") version "1.3.50"
    id("org.jetbrains.dokka") version "0.9.18"
}

project(":library") {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.dokka")
}

project(":sample") {
    apply(plugin = "org.jetbrains.kotlin.jvm")
}

allprojects {
    repositories {
        mavenCentral()
        maven { url = URI("http://dl.bintray.com/kotlin/kotlin-eap/") }
        maven { url = URI("https://dl.bintray.com/kotlin/kotlinx") }
        maven { url = URI("https://plugins.gradle.org/m2/") }
        jcenter()
    }
}

tasks.withType<Delete> {
    rootProject.buildDir
}