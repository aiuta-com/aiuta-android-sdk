import com.aiuta.fashionsdk.groupId
import com.aiuta.fashionsdk.publicModules
import com.aiuta.fashionsdk.versionName
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessExtensionPredeclare
import kotlinx.validation.ApiValidationExtension
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
    dependencies {
        classpath(libs.gradlePlugin.android)
        classpath(libs.gradlePlugin.kotlin)
        classpath(libs.gradlePlugin.vanniktech.publish)
    }
}

plugins {
    alias(libs.plugins.baselineProfile) apply false
    alias(libs.plugins.binaryCompatibility)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.spotless)
}

extensions.configure<ApiValidationExtension> {
    ignoredProjects +=
        project.subprojects.mapNotNull { project ->
            if (project.name in publicModules) null else project.name
        }
}

tasks.withType<DokkaMultiModuleTask>().configureEach {
    outputDirectory = layout.projectDirectory.dir("docs/api")
}

tasks.register<Exec>("installGitHooks") {
    description = "Install local git hooks"
    group = "Build Setup"
    commandLine("chmod", "-R", "+x", "${rootProject.rootDir}/.githooks/")
    commandLine("git", "config", "--local", "core.hooksPath", "${rootProject.rootDir}/.githooks/")
}

val initialTaskNames: List<String> = project.gradle.startParameter.taskNames
project.gradle.startParameter.setTaskNames(initialTaskNames + listOf("installGitHooks"))


allprojects {
    repositories {
        google()
        mavenCentral()
    }

    // Necessary to publish to Maven.
    group = groupId
    version = versionName

    // Target JVM 8.
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
        options.compilerArgs = options.compilerArgs + "-Xlint:-options"
    }
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions.jvmTarget = JvmTarget.JVM_1_8
    }

    tasks.withType<DokkaTaskPartial>().configureEach {
        dokkaSourceSets.configureEach {
            jdkVersion = 8
            failOnWarning = true
            skipDeprecated = true
            suppressInheritedMembers = true
        }
    }

    apply(plugin = "com.diffplug.spotless")

    val configureSpotless: SpotlessExtension.() -> Unit = {
        kotlin {
            target("**/*.kt", "**/*.kts")
            ktlint(
                libs.versions.ktlint.get(),
            ).setEditorConfigPath("${project.rootDir}/.editorconfig")
            endWithNewline()
            indentWithSpaces()
            trimTrailingWhitespace()
        }
    }

    if (project === rootProject) {
        spotless { predeclareDeps() }
        extensions.configure<SpotlessExtensionPredeclare>(configureSpotless)
    } else {
        extensions.configure<SpotlessExtension>(configureSpotless)
    }
}
