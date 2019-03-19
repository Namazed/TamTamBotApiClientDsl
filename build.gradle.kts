import java.net.URI

plugins {
    kotlin("jvm") version "1.3.21"
}

project(":library") {
    apply(plugin = "org.jetbrains.kotlin.jvm")
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