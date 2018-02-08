import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.intellij.tasks.PublishTask
//import de.undercouch.gradle.tasks.download.Download
import org.gradle.api.internal.HasConvention
import org.gradle.api.tasks.SourceSet
import org.jetbrains.grammarkit.GrammarKitPluginExtension
import org.jetbrains.grammarkit.tasks.GenerateLexer
import org.jetbrains.grammarkit.tasks.GenerateParser
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.gradle.api.JavaVersion.VERSION_1_8
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.jvm.tasks.Jar
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Path
import kotlin.concurrent.thread

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.21"

    repositories {
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        maven { setUrl( "https://plugins.gradle.org/m2/") }
        maven {
            setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service")
        }
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlin_version))
        classpath("com.github.hurricup:gradle-grammar-kit-plugin:2017.1.1")
        classpath("gradle.plugin.org.jetbrains:gradle-intellij-plugin:0.1.10")
    }
}

plugins {
    idea
    id("org.jetbrains.intellij") version "0.2.17"
}

group = "patapon"
version = "1.0-SNAPSHOT"

apply {
    plugin("idea")
    plugin("java")
    plugin("kotlin")
    plugin("org.jetbrains.grammarkit")
    plugin("org.jetbrains.intellij")
}

intellij {
    version = "IC-2017.3"
    //downloadSources = !CI
    updateSinceUntilBuild = false
    instrumentCode = false
    //ideaDependencyCachePath = file("deps").absolutePath
    pluginName = "RenderGraph"
}

val kotlin_version: String by extra

allprojects {
    java.sourceSets {
        getByName("main").java.srcDirs("src/generated/java")
    }

}

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlinModule("stdlib-jdk8", kotlin_version))
    compile("de.jensd:fontawesomefx-fontawesome:4.7.0-9.1.2")
    compile("no.tornado:tornadofx:1.7.14")
    compile("org.controlsfx:controlsfx:8.40.14")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:0.21.2")
    compile("io.reactivex.rxjava2:rxjavafx:2.2.2")
    compile("io.reactivex.rxjava2:rxkotlin:2.2.0")
    compile("com.github.thomasnield:rxkotlinfx:2.2.2")
    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val generateLexer = task<GenerateLexer>("generateLexer") {
    source = "src/main/grammars/patapon/rendergraph/RenderGraph.flex"
    targetDir = "src/generated/java/patapon/rendergraph"
    targetClass = "RenderGraphLexer"
    purgeOldFiles = true
}

val generateParser = task<GenerateParser>("generateParser") {
    source = "src/main/grammars/patapon/rendergraph/RenderGraph.bnf"
    targetRoot = "src/generated/java"
    pathToParser = "/patapon/rendergraph/parser/RenderGraphParser.java"
    pathToPsiRoot = "/patapon/rendergraph/psi"
    purgeOldFiles = true
}

tasks.withType<KotlinCompile> {
    dependsOn(generateLexer, generateParser)
}
