import java.net.URI

repositories {
    mavenCentral()
    maven { url = URI("http://dl.bintray.com/kotlin/kotlin-eap/") }
    maven { url = URI("https://dl.bintray.com/kotlin/kotlinx") }
    maven { url = URI("https://plugins.gradle.org/m2/") }
    jcenter()
}
