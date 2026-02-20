import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.multiplatformAndroidLibrary

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
    alias(libs.plugins.kotlinx.serialization)
}

// DataStore doesn't support JS/WASM yet, so we disable extended targets
addAllMultiplatformTargets(enableExtendedTargets = false)
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk.internal.storage")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.fashion)

                implementation(libs.androidx.datastore.preferences)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization)
                implementation(libs.kotlinx.atomicfu)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}
