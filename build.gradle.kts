import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    kotlin("jvm") version "1.3.11"
}

group = "chat.tamtam.botsdk"
version = "0.1"

val ktorVersion = "1.1.1"
val compileKotlin: KotlinCompile by tasks

repositories {
    mavenCentral()
    maven { url = URI("http://dl.bintray.com/kotlin/kotlin-eap/") }
    mavenCentral()
    maven { url = URI("http://dl.bintray.com/kotlin/ktor") }
    maven { url = URI("https://dl.bintray.com/kotlin/kotlinx") }
    maven { url = URI("https://plugins.gradle.org/m2/") }
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("io.ktor:ktor-client-okhttp:$ktorVersion")
    compile("io.ktor:ktor-client-json-jvm:$ktorVersion")
//    compile("io.ktor:ktor-client-gson:$ktorVersion")
    compile("com.squareup.okhttp3:logging-interceptor:3.12.0")
}

compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
}