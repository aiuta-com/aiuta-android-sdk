package com.aiuta.fashionsdk

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.Lint
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import com.vanniktech.maven.publish.JavadocJar.Dokka
import com.vanniktech.maven.publish.KotlinMultiplatform
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.androidLibrary(
    name: String,
    config: Boolean = false,
    composeLibrary: Boolean = false,
    action: LibraryExtension.() -> Unit = {},
) = androidBase<LibraryExtension>(
    name = name,
    config = config,
) {
    buildFeatures {
        buildConfig = config
    }
    if (project.name in publicModules) {
        apply(plugin = "org.jetbrains.dokka")
        apply(plugin = "com.vanniktech.maven.publish.base")
        setupPublishing {
            val platform = if (project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                KotlinMultiplatform(Dokka("dokkaHtml"))
            } else {
                AndroidSingleVariantLibrary()
            }
            configure(platform)
        }
    }
    if (composeLibrary) {
        apply(plugin = "org.jetbrains.kotlin.plugin.compose")
        extensions.configure<ComposeCompilerGradlePluginExtension> {
            enableStrongSkippingMode = true
        }
    }
    if (config) {
        defaultConfig {
            buildConfigField("String", "VERSION_NAME", "\"${versionName}\"")
        }
    }
    action()
}

fun Project.androidApplication(
    name: String,
    shouldBePublic: Boolean = true,
    composeApp: Boolean = false,
    action: BaseAppModuleExtension.() -> Unit = {},
) = androidBase<BaseAppModuleExtension>(
    name = name,
    shouldBePublic = shouldBePublic,
) {
    defaultConfig {
        applicationId = name
        versionCode = project.versionCode
        versionName = project.versionName
        resourceConfigurations += "en"
        vectorDrawables.useSupportLibrary = true
    }
    buildFeatures {
        compose = composeApp
    }
    if (composeApp) {
        apply(plugin = "org.jetbrains.kotlin.plugin.compose")
        extensions.configure<ComposeCompilerGradlePluginExtension> {
            enableStrongSkippingMode = true
        }
    }
    action()
}

fun Project.androidTest(
    name: String,
    config: Boolean = false,
    shouldBePublic: Boolean = false,
    action: TestExtension.() -> Unit = {},
) = androidBase<TestExtension>(
    name = name,
    config = config,
    shouldBePublic = shouldBePublic,
) {
    buildFeatures {
        buildConfig = config
    }
    defaultConfig {
        resourceConfigurations += "en"
        vectorDrawables.useSupportLibrary = true
    }
    action()
}

private fun <T : BaseExtension> Project.androidBase(
    name: String,
    config: Boolean = false,
    shouldBePublic: Boolean = true,
    action: T.() -> Unit,
) {
    android<T> {
        namespace = name
        compileSdkVersion(project.compileSdk)
        defaultConfig {
            minSdk = project.minSdk
            targetSdk = project.targetSdk
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            if (config) {
                buildConfigField("String", "VERSION_NAME", "\"${versionName}\"")
            }
        }
        packagingOptions {
            resources.pickFirsts +=
                listOf(
                    "META-INF/AL2.0",
                    "META-INF/LGPL2.1",
                    "META-INF/*kotlin_module",
                )
        }
        testOptions {
            unitTests.isIncludeAndroidResources = true
        }
        lint {
            warningsAsErrors = true
            disable +=
                listOf(
                    "ComposableNaming",
                    "UnknownIssueId",
                    "VectorPath",
                    "VectorRaster",
                    "ObsoleteLintCustomCheck",
                    "MonochromeLauncherIcon",
                    "IconLocation",
                )
        }
        action()
    }
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            val arguments = mutableListOf<String>()

            // https://kotlinlang.org/docs/compiler-reference.html#progressive
            arguments += "-progressive"

            // Enable Java default method generation.
            arguments += "-Xjvm-default=all"

            // Generate smaller bytecode by not generating runtime not-null assertions.
            arguments += "-Xno-call-assertions"
            arguments += "-Xno-param-assertions"
            arguments += "-Xno-receiver-assertions"

            // For explicitApi strict mode - make as error
            if (shouldBePublic) {
                arguments += "-Xexplicit-api=strict"
            }

            freeCompilerArgs.addAll(arguments)
        }
    }
}

internal fun <T : BaseExtension> Project.android(action: T.() -> Unit) {
    extensions.configure("android", action)
}

private fun BaseExtension.lint(action: Lint.() -> Unit) {
    (this as CommonExtension<*, *, *, *, *, *>).lint(action)
}
