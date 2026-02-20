@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.mobileMain
import com.aiuta.fashionsdk.multiplatformAndroidLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.room)
}

addAllMultiplatformTargets(enableExtendedTargets = false)
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk.tryon.compose") {
    androidResources {
        enable = true
    }
    optimization {
        consumerKeepRules.publish = true
        consumerKeepRules.files += project.file("shrinker-rules.pro")
    }
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.androidx.core)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.ui.unit.android)
                implementation(libs.ktor.engine.okhttp)
            }
        }
        androidUnitTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.mockk)
            }
        }
        commonMain {
            dependencies {
                api(projects.fashionComposeCore)
                api(projects.fashionConfiguration)
                api(projects.fashionTryonCore)

                implementation(libs.androidx.paging.common)
                implementation(libs.coil3.compose)
                implementation(libs.coil3.network.ktor3)
                implementation(libs.compose.placeholder)
                implementation(libs.haze)
                implementation(libs.kotlinx.atomicfu)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization)
                implementation(libs.room.runtime)
                implementation(libs.room.paging)
                implementation(libs.jetbrains.lifecycle)
                implementation(libs.jetbrains.compose.material)
                implementation(libs.jetbrains.compose.ui.backhandler)
                implementation(libs.sqlite.bundled)

                implementation(projects.fashionAnalyticsEvents)
                implementation(projects.fashionComposeUikit)
                implementation(projects.internal.internalFashionAnalytics)
                implementation(projects.internal.internalFashionNavigation)
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.calf.picker)
                implementation(libs.ktor.engine.okhttp)
            }
        }
        mobileMain {
            dependencies {
                implementation(libs.moko.permissions.camera)
                implementation(libs.moko.permissions.gallery)
                implementation(libs.moko.compose)
            }
        }
        nativeMain {
            dependencies {
                implementation(libs.ktor.engine.darwin)
            }
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    listOf(
        "kspAndroid",
        "kspJvm",
        "kspIosSimulatorArm64",
        "kspIosX64",
        "kspIosArm64",
    ).forEach { configurationName ->
        add(configurationName, libs.room.compiler)
    }
}
