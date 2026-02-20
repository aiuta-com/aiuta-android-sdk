import com.aiuta.fashionsdk.groupId
import com.aiuta.fashionsdk.publicModulePath
import com.aiuta.fashionsdk.publicModules
import com.aiuta.fashionsdk.versionName
import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationExtension
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation
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
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.spotless)
    // https://github.com/gradle/gradle/issues/20084#issuecomment-1060822638
    id("org.jetbrains.dokka")
}

dokka {
    dokkaGeneratorIsolation = ClassLoaderIsolation()
    dokkaPublications.configureEach {
        outputDirectory.set(layout.projectDirectory.dir("docs/api"))
    }
}

dependencies {
    for (module in publicModules) {
        dokka(project(publicModulePath(module)))
    }
}

tasks.register<Exec>("installGitHooks") {
    description = "Install local git hooks"
    group = "Build Setup"
    commandLine("chmod", "-R", "+x", "${rootProject.rootDir}/.githooks/")
    commandLine("git", "config", "--local", "core.hooksPath", "${rootProject.rootDir}/.githooks/")
}

val initialTaskNames: List<String> = project.gradle.startParameter.taskNames
project.gradle.startParameter.setTaskNames(initialTaskNames + listOf(":installGitHooks"))

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

    apply(plugin = "com.diffplug.spotless")

    extensions.configure<SpotlessExtension> {
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

    if (project.name in publicModules) {
        configureAbiValidation()
        configureAndroidLegacyAbiValidation()
    }
}

@OptIn(ExperimentalAbiValidation::class)
fun Project.configureAbiValidation() {
    afterEvaluate {
        val kotlinExtension = extensions.findByType<KotlinProjectExtension>() ?: return@afterEvaluate

        // Unfortunately the 'enabled' property doesn't share a common interface.
        kotlinExtension.extensions.findByType<AbiValidationExtension>()?.apply {
            enabled.set(true)
        }
        kotlinExtension.extensions.findByType<AbiValidationMultiplatformExtension>()?.apply {
            enabled.set(true)
        }
    }
}

fun Project.configureAndroidLegacyAbiValidation() {
    plugins.withId("com.android.kotlin.multiplatform.library") {
        afterEvaluate {
            tasks
                .matching { it.name == "dumpLegacyAbi" || it.name.startsWith("dumpLegacyAbi") }
                .configureEach {
                    tasks.findByName("compileAndroidMain")?.let { dependsOn(it) }
                    configureAndroidLegacyAbiDumpInput(this@configureAndroidLegacyAbiValidation)
                }
        }
    }
}

private fun Task.configureAndroidLegacyAbiDumpInput(project: Project) {
    val dumpTaskClass = javaClass
    val getJvm = runCatching { dumpTaskClass.getMethod("getJvm") }.getOrNull() ?: return
    val jvmProperty = getJvm.invoke(this)

    val propertyClass = jvmProperty.javaClass
    val get = propertyClass.getMethod("get")
    val set = propertyClass.getMethod("set", Iterable::class.java)

    val existingEntries = mutableListOf<Any>()
    val currentEntries = get.invoke(jvmProperty)
    if (currentEntries is Iterable<*>) {
        currentEntries.filterNotNullTo(existingEntries)
    } else {
        return
    }

    val entryClass = existingEntries.firstOrNull()?.javaClass
        ?: runCatching {
            dumpTaskClass.classLoader.loadClass(
                $$"org.jetbrains.kotlin.gradle.tasks.abi.KotlinLegacyAbiDumpTaskImpl$JvmTargetInfo",
            )
        }.getOrNull()
        ?: return

    val getSubdirectoryName = entryClass.getMethod("getSubdirectoryName")
    if (existingEntries.any { getSubdirectoryName.invoke(it) == "android" }) return

    val constructor = entryClass.getConstructor(String::class.java, FileCollection::class.java)
    val androidClasses = project.files(
        project.layout.buildDirectory.dir("classes/kotlin/android/main"),
        project.layout.buildDirectory.dir("classes/java/android/main"),
        project.layout.buildDirectory.dir("intermediates/javac/androidMain/classes"),
    )
    val androidEntry = constructor.newInstance("android", androidClasses)

    existingEntries.add(androidEntry)
    set.invoke(jvmProperty, existingEntries)
}

private val ktlintRules = buildMap {
    put("ktlint_code_style", "intellij_idea")

    put("ktlint_standard_max-line-length", "disabled")

    put("ktlint_standard_parameter-wrapping", "disabled")
    put("ktlint_standard_property-wrapping", "disabled")
}
