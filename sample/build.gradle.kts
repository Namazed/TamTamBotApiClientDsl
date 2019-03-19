import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.github.Namazed"
version = "0.0.2"

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