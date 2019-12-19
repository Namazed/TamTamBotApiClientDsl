import java.net.URI

plugins {
    kotlin("jvm") version "1.3.50" apply false
    id("org.jetbrains.dokka") version "0.9.18"  apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.3.50" apply false
    id("com.github.johnrengelman.shadow") version "2.0.4" apply false
    id("com.jfrog.bintray") version "1.8.4" apply false
    id("com.jfrog.artifactory") version "4.9.8" apply false
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