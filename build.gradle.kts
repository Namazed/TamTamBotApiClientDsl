import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    kotlin("jvm") version "1.3.21"
    id("kotlinx-serialization") version "1.3.21"
    id("org.jetbrains.dokka") version "0.9.17"
    `maven-publish`
}

group = "chat.tamtam.botsdk"
version = "0.0.1"

val compileKotlin: KotlinCompile by tasks
val dokka: DokkaTask by tasks

repositories {
    mavenCentral()
    maven { url = URI("http://dl.bintray.com/kotlin/kotlin-eap/") }
    maven { url = URI("https://dl.bintray.com/kotlin/kotlinx") }
    maven { url = URI("https://plugins.gradle.org/m2/") }
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("ch.qos.logback:logback-classic:1.2.1")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    compile("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.10.0")
    compile("com.squareup.okhttp3:logging-interceptor:3.12.0")
    compile("com.squareup.retrofit2:retrofit:2.5.0")
    compile("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.3.0")
    testCompile("org.junit.jupiter:junit-jupiter:5.4.0")
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.jvmTarget = "1.8"
}

compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-XXLanguage:+InlineClasses", "-Xuse-experimental=kotlin.Experimental")
}

tasks.withType<DokkaTask> {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    classifier = "javadoc"
    from(dokka)
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
    getByName("test").java.srcDirs("src/test/kotlin")
}