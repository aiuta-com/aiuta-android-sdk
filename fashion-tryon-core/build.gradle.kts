import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.multiplatformAndroidLibrary

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.kotlinx.serialization)
}

addAllMultiplatformTargets()
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk.tryon.core")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.fashion)
                api(projects.fashionIo)
                api(projects.fashionNetwork)
                api(projects.fashionNetworkPaging)

                implementation(libs.jetbrains.compose.ui)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization)

                implementation(projects.internal.internalFashionAnalytics)
            }
        }
        // Not commonTest: JS/Wasm tests would pull the npm test runners
        // into the shared kotlin-js-store lock files
        getByName("nonJsCommonTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}
