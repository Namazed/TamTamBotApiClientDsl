
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.github.johnrengelman.shadow")
    id("com.jfrog.bintray")
    id("com.jfrog.artifactory")
    `maven-publish`
    jacoco
}

group = "com.namazed.botsdk"
version = "0.3.0${getSnapshotSuffix()}"

val compileKotlin: KotlinCompile by tasks
val dokka: DokkaTask by tasks
val shadowJar: ShadowJar by tasks
val test: Test by tasks
val bintrayUser = "bintray.user"
val bintrayKey = "bintray.key"
val artifactID = project.name
val groupID = project.group as String
val currentVersion = project.version as String
val publicationName = "maven"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("ch.qos.logback:logback-classic:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.5.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.4.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:3.14.0")
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    val codeCoverageReport by creating(JacocoReport::class) {
        executionData(fileTree(project.rootDir.absolutePath).include("**/build/jacoco/*.exec"))

        subprojects.onEach {
            sourceSets(it.sourceSets["main"])
        }

        reports {
            sourceDirectories =  files(sourceSets["main"].allSource.srcDirs)
            classDirectories =  files(sourceSets["main"].output)
            xml.isEnabled = true
            xml.destination = File("${rootProject.buildDir}/reports/jacoco/report.xml")
            html.isEnabled = false
            csv.isEnabled = false
        }

        dependsOn("test")
    }
}

test.apply {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-XXLanguage:+InlineClasses", "-Xuse-experimental=kotlin.Experimental")
}

shadowJar.apply {
    baseName = artifactID
    classifier = "sources"
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

publishing {
    publications {
        register(publicationName, MavenPublication::class) {
            artifactId = artifactID
            groupId = groupID
            version = currentVersion
            from(components["java"])
            shadow.component(this)
            artifact(dokkaJar)
        }
    }
}

artifactory {
    setContextUrl("https://oss.jfrog.org/artifactory")
    publish(delegateClosureOf<org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig> {
        repository(delegateClosureOf<groovy.lang.GroovyObject> {
            setProperty("repoKey", "oss-snapshot-local")
            setProperty("username", bintrayUser.findProperty())
            setProperty("password", bintrayKey.findProperty())
            setProperty(publicationName, true)
        })
        defaults(delegateClosureOf<groovy.lang.GroovyObject> {
            invokeMethod("publications", publicationName)
            setProperty("publishArtifacts", true)
            setProperty("publishPom", true)
        })
    })
    clientConfig.info.buildNumber = System.getProperty("build.number")
}

fun String.findProperty() = project.findProperty(this) as String? ?: System.getenv(this)

fun getSnapshotSuffix(): String {
    return System.getenv("snapshot")?.let {
        if (it.toBoolean()) "-SNAPSHOT" else ""
    } ?: ""
}

bintray {
    user = bintrayUser.findProperty()
    key = bintrayKey.findProperty()
    publish = true
    setPublications(publicationName)
    with(pkg) {
        repo = publicationName
        name = artifactID
        userOrg = "namazed"
        vcsUrl = "https://github.com/Namazed/TamTamBotApiClientDsl.git"
        desc = "Kotlin DSL client for TamTamBotApi"
        websiteUrl = "https://github.com/Namazed/TamTamBotApiClientDsl"
        issueTrackerUrl = "https://github.com/Namazed/TamTamBotApiClientDsl/issues"
        setLabels("kotlin", "dsl", "bot", "tamtam")
        setLicenses("Apache 2.0")
        version = VersionConfig().apply {
            name = currentVersion
            vcsTag = currentVersion
        }
    }
}

sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
    getByName("test").java.srcDirs("src/test/kotlin")
}