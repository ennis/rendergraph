import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.intellij.tasks.PublishTask
import de.undercouch.gradle.tasks.download.Download
import org.gradle.api.internal.HasConvention
import org.gradle.api.tasks.SourceSet
import org.jetbrains.grammarkit.GrammarKitPluginExtension
import org.jetbrains.grammarkit.tasks.GenerateLexer
import org.jetbrains.grammarkit.tasks.GenerateParser
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.gradle.api.JavaVersion.VERSION_1_8
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.jvm.tasks.Jar
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.intellij.tasks.RunIdeTask
import org.jetbrains.intellij.IntelliJPlugin
import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.intellij.jbre.JbreResolver
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.concurrent.thread

buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.21"

    repositories {
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        maven { setUrl( "https://plugins.gradle.org/m2/") }
        maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots/") }
        maven { setUrl("http://dl.bintray.com/jetbrains/intellij-plugin-service") }
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlin_version))
        classpath("com.github.hurricup:gradle-grammar-kit-plugin:2017.1.1")
        classpath("org.jetbrains.intellij.plugins:gradle-intellij-plugin:0.3.0-SNAPSHOT")
    }
}

group = "patapon"
version = "1.0-SNAPSHOT"

plugins {
    idea
    java
    id("de.undercouch.download") version "3.2.0"
    //id("org.jetbrains.intellij")
}

apply {
    plugin("idea")
    //plugin("java")
    plugin("kotlin")
    plugin("org.jetbrains.grammarkit")
    plugin("org.jetbrains.intellij")
}


val kotlin_version: String by extra
val sandboxDir = project.rootDir.canonicalPath + "/.sandbox"

configure<IntelliJPluginExtension> {
    version = "IC-2017.3.4"
    //downloadSources = !CI
    updateSinceUntilBuild = false
    instrumentCode = false
    //ideaDependencyCachePath = file("deps").absolutePath
    pluginName = "RenderGraph"
    sandboxDirectory = sandboxDir
}


the<JavaPluginConvention>().sourceSets {
    "main" {
        java {
            srcDirs("src/generated/java")
        }
    }
}

repositories {
    mavenCentral()
}

allprojects {
    dependencies {
    }
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

fun platform(): String {
    val current = OperatingSystem.current()
    if (current.isWindows()) return "windows"
    if (current.isMacOsX()) return "osx"
    return "linux"
}

fun arch(): String {
    val arch = System.getProperty("os.arch")
    return if ("x86" == arch) { "x86" } else { "x64" }
}

fun findJavaExecutable(javaHome: File): String? {
    val os = OperatingSystem.current()
    val suffix = if (os.isMacOsX) {
        "jdk/Contents/Home/jre/bin/java"
    } else if (os.isWindows) {
        "jre/bin/java.exe"
    } else {
        "jre/bin/java"
    }
    val j = File(javaHome,suffix)
    return  if (j.exists()) {j.absolutePath } else {null}
}

tasks.withType<RunIdeTask> {
    setJbreVersion("jbrex8u152b1136.11")
}

val generateLexer = task<GenerateLexer>("generateLexer") {
    source = "src/main/grammars/RenderGraph.flex"
    targetDir = "src/generated/java/patapon/rendergraph/lang/lexer"
    targetClass = "RenderGraphLexer"
    purgeOldFiles = true
}

val generateParser = task<GenerateParser>("generateParser") {
    source = "src/main/grammars/RenderGraph.bnf"
    targetRoot = "src/generated/java"
    pathToParser = "/patapon/rendergraph/lang/parser/RenderGraphParser.java"
    pathToPsiRoot = "/patapon/rendergraph/lang/psi"
    purgeOldFiles = true
}

tasks.withType<KotlinCompile> {
    dependsOn(generateLexer, generateParser)
}
