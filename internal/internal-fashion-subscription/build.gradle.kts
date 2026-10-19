import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.multiplatformAndroidLibrary

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
    alias(libs.plugins.kotlinx.serialization)
}

addAllMultiplatformTargets()
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk.internal.subscription")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.fashion)

                implementation(projects.fashionNetwork)
                implementation(libs.kotlinx.atomicfu)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization)
            }
        }
        // The details are persisted with DataStore, which doesn't support
        // JS/Wasm: there they are kept in memory only
        getByName("nonJsCommonMain") {
            dependencies {
                implementation(projects.internal.internalFashionStorage)
            }
        }
        // Not commonTest: JS/Wasm tests would pull the npm test runners
        // into the shared kotlin-js-store lock files
        getByName("nonJsCommonTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.ktor.mock)
                implementation(libs.ktor.negotiation)
                implementation(libs.ktor.serialization)
            }
        }
        getByName("androidHostTest") {
            dependencies {
                implementation(libs.mockk)
            }
        }
    }
}
