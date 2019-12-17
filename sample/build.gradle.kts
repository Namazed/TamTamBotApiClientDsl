import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.namazed.botsdk"
version = "0.3.0"

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":library"))
    testImplementation("junit", "junit", "4.12")
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.jvmTarget = "1.8"
}