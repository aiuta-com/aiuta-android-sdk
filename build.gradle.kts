import com.aiuta.fashionsdk.groupId
import com.aiuta.fashionsdk.publicModules
import com.aiuta.fashionsdk.versionName
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessExtensionPredeclare
import kotlinx.validation.ApiValidationExtension
import kotlinx.validation.ExperimentalBCVApi
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
        classpath(libs.gradlePlugin.jetbrains.compose)
        classpath(libs.gradlePlugin.vanniktech.publish)
    }
}

plugins {
    alias(libs.plugins.baselineProfile) apply false
    alias(libs.plugins.binaryCompatibility)
    alias(libs.plugins.buildKonfig) apply false
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.jetbrains.compose.hotreload) apply false
    alias(libs.plugins.spotless)
}

extensions.configure<ApiValidationExtension> {
    ignoredProjects +=
        project.subprojects.mapNotNull { project ->
            if (project.name in publicModules) null else project.name
        }
    @OptIn(ExperimentalBCVApi::class)
    klib {
        enabled = true
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

    // Target JVM 11.
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_11.toString()
        targetCompatibility = JavaVersion.VERSION_11.toString()
        options.compilerArgs = options.compilerArgs + "-Xlint:-options"
    }
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions.jvmTarget = JvmTarget.JVM_11
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
            targetExclude(
                "**/build/**/*.kt",
                "**/MainViewController.kt",
            )
            ktlint(libs.versions.ktlint.get()).editorConfigOverride(ktlintRules)
            endWithNewline()
            leadingTabsToSpaces()
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

private val ktlintRules = buildMap {
    put("ktlint_code_style", "intellij_idea")

    put("ktlint_standard_max-line-length", "disabled")

    put("ktlint_standard_parameter-wrapping", "disabled")
    put("ktlint_standard_property-wrapping", "disabled")
}
