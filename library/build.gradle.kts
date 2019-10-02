
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.lang.GroovyObject
import groovy.util.Node
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.dsl.ResolverConfig

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.github.johnrengelman.shadow")
    id("com.jfrog.bintray")
    id("com.jfrog.artifactory")
    `maven-publish`
}

group = "com.namazed"
version = "0.3.0"

val compileKotlin: KotlinCompile by tasks
val dokka: DokkaTask by tasks
val shadowJar: ShadowJar by tasks
val test: Test by tasks
val artifactID = "botsdk"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("ch.qos.logback:logback-classic:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.5.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:3.14.0")
}

shadowJar.apply {
    baseName = artifactID
    classifier = ""
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.jvmTarget = "1.8"
}

compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-XXLanguage:+InlineClasses", "-Xuse-experimental=kotlin.Experimental")
}

dokka.apply {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    classifier = "javadoc"
    from(dokka)
}

test.apply {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

artifactory {
    setContextUrl("http://oss.jfrog.org")
    publish(delegateClosureOf<PublisherConfig> {
        repository(delegateClosureOf<GroovyObject> {
            val targetRepoKey = "oss-snapshot-local"
            setProperty("repoKey", targetRepoKey)
            setProperty("username", project.findProperty("bintray.user") ?: "nouser")
            setProperty("password", project.findProperty("bintray.key") ?: "nopass")
            setProperty("maven", true)
        })
        defaults(delegateClosureOf<GroovyObject> {
            invokeMethod("publications", "mavenPublication")
        })
    })
    resolve(delegateClosureOf<ResolverConfig> {
        setProperty("repoKey", "jcenter")
    })
}

configure<PublishingExtension> {
    publications.withType(MavenPublication::class.java).forEach {
        with(it) {
            artifactId = artifactID
            artifact(shadowJar)
            addNodeToPom(this)
        }
    }
}

fun addNodeToPom(mavenPublication: MavenPublication) {
    mavenPublication.pom.withXml {
        asNode().appendNode("dependencies").let { depNode ->
            configurations.compile.allDependencies.forEach { appendDependencyToNode(depNode, it) }
        }
    }
}

fun appendDependencyToNode(depNode: Node, dep: Dependency) {
    depNode.appendNode("dependency").apply {
        appendNode("groupId", dep.group)
        appendNode("artifactId", dep.name)
        appendNode("version", dep.version)
    }
}

fun String.findProperty() = project.findProperty(this) as String?
bintray {
    user = "bintray.user".findProperty()
    key = "bintray.key".findProperty()
    publish = true
    setPublications("mavenPublication")
    with(pkg) {
        repo = "tamtam_bot_dsl_client"
        name = "TamTamBotApiClientDsl"
        userOrg = "namazed"
        vcsUrl = "https://github.com/Namazed/TamTamBotApiClientDsl"
        setLabels("kotlin")
        setLicenses("Apache 2.0")
    }
}

sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
    getByName("test").java.srcDirs("src/test/kotlin")
}