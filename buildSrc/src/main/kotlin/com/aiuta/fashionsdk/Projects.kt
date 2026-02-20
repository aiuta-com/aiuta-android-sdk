package com.aiuta.fashionsdk

import com.aiuta.fashionsdk.minSdk
import com.aiuta.fashionsdk.targetSdk
import com.aiuta.fashionsdk.versionName
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import com.android.build.api.dsl.Lint
import com.vanniktech.maven.publish.JavadocJar.Dokka
import com.vanniktech.maven.publish.KotlinMultiplatform
import kotlin.plus
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.dokka.gradle.DokkaExtension
import org.jetbrains.dokka.gradle.engine.parameters.KotlinPlatform
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private val DISABLED_LINT_RULES = listOf(
    "ComposableNaming",
    "UnknownIssueId",
    "VectorPath",
    "VectorRaster",
    "ObsoleteLintCustomCheck",
    "MonochromeLauncherIcon",
    "IconLocation",
    // Renovate bot checks deps updates
    "NewerVersionAvailable",
    "AndroidGradlePluginVersion",
    "GradleDependency",
)

private val RESOURCE_DUPLICATE_OVERRIDES = listOf(
    "META-INF/AL2.0",
    "META-INF/LGPL2.1",
    "META-INF/*kotlin_module",
)

private fun Project.configureKotlinMultiplatform() {
    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        extensions.configure<KotlinMultiplatformExtension> {
            compilerOptions {
                // https://youtrack.jetbrains.com/issue/KT-61573
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
}

private fun Project.configureKotlinCompile() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            val arguments = buildList {
                // https://kotlinlang.org/docs/compiler-reference.html#progressive
                add("-progressive")

                // Enable Java default method generation.
                add("-Xjvm-default=all")

                // Generate smaller bytecode by not generating runtime not-null assertions.
                add("-Xno-call-assertions")
                add("-Xno-param-assertions")
                add("-Xno-receiver-assertions")

                // For explicitApi strict mode - make as error
                if (project.name in publicModules) {
                    add("-Xexplicit-api=strict")
                }
            }

            freeCompilerArgs.addAll(arguments)
        }
    }
}

fun Project.multiplatformAndroidLibrary(
    name: String,
    action: KotlinMultiplatformAndroidLibraryTarget.() -> Unit = {},
) {
    configureKotlinMultiplatform()
    configureKotlinCompile()

    plugins.withId("org.jetbrains.kotlin.multiplatform") {
        extensions.configure<KotlinMultiplatformExtension> {
            targets.withType<KotlinMultiplatformAndroidLibraryTarget>().configureEach {
                namespace = name
                compileSdk = project.compileSdk
                minSdk = project.minSdk

                withHostTest {
                    isIncludeAndroidResources = true
                }

                withDeviceTest {
                    instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                lint {
                    warningsAsErrors = true
                    disable += DISABLED_LINT_RULES
                }

                packaging {
                    resources.pickFirsts += RESOURCE_DUPLICATE_OVERRIDES
                }

                action()
            }
        }
    }

    if (project.name in publicModules) {
        apply(plugin = "org.jetbrains.dokka")
        apply(plugin = "com.vanniktech.maven.publish.base")
        setupDokka()
        setupPublishing {
            configure(KotlinMultiplatform(Dokka("dokkaGenerate")))
        }
    }
}

fun Project.androidApplication(
    name: String,
    action: ApplicationExtension.() -> Unit = {},
) = androidBase<ApplicationExtension>(
    name = name,
) {
    defaultConfig {
        minSdk = project.minSdk
        targetSdk = project.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        applicationId = name
        versionCode = project.versionCode
        versionName = project.versionName
        androidResources.localeFilters += "en"
        vectorDrawables.useSupportLibrary = true
    }
    packaging {
        resources.pickFirsts += RESOURCE_DUPLICATE_OVERRIDES
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    action()
}

private fun <T : CommonExtension> Project.androidBase(
    name: String,
    action: T.() -> Unit,
) {
    configureKotlinMultiplatform()
    configureKotlinCompile()

    android<T> {
        namespace = name
        compileSdk {
            version = release(project.compileSdk)
        }
        lint {
            warningsAsErrors = true
            disable += DISABLED_LINT_RULES
        }
        action()
    }
}

internal fun <T : CommonExtension> Project.android(action: T.() -> Unit) {
    extensions.configure("android", action)
}

private fun CommonExtension.lint(action: Lint.() -> Unit) {
    this.lint.apply(action)
}

private fun Project.setupDokka(
    action: DokkaExtension.() -> Unit = {},
) {
    extensions.configure<DokkaExtension> {
        dokkaPublications.configureEach {
            failOnWarning.set(true)
            suppressInheritedMembers.set(true)
        }
        dokkaSourceSets.configureEach {
            jdkVersion.set(8)
            skipDeprecated.set(true)

            if (name == "jvmCommonMain") {
                analysisPlatform.set(KotlinPlatform.JVM)
            }

            // Suppress the 'main' source set for non-multiplatform Android
            // libraries to avoid conflicting with the 'release' source set.
            // https://github.com/Kotlin/dokka/issues/3701
            if (name == "main" && !project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                suppress.set(true)
            }

            externalDocumentationLinks.register("android") {
                url.set(uri("https://developer.android.com/reference/"))
            }
            externalDocumentationLinks.register("coroutines") {
                url.set(uri("https://kotlinlang.org/api/kotlinx.coroutines/"))
            }
            externalDocumentationLinks.register("ktor") {
                url.set(uri("https://api.ktor.io/"))
            }
            externalDocumentationLinks.register("datetime") {
                url.set(uri("https://kotlinlang.org/api/kotlinx-datetime/"))
            }
        }
        action()
    }
}
