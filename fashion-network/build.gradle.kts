import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.androidLibrary

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
    alias(libs.plugins.kotlinx.serialization)
}

addAllMultiplatformTargets()
androidLibrary(name = "com.aiuta.fashionsdk.network")

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.ktor.engine.okhttp)
                implementation(libs.kotlinx.coroutines.android)
            }
        }
        commonMain {
            dependencies {
                api(projects.fashion)

                api(libs.ktor.core)

                implementation(libs.kotlinx.atomicfu)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.ktor.auth)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.negotiation)
                implementation(libs.ktor.serialization)
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.ktor.engine.okhttp)
            }
        }
        nativeMain {
            dependencies {
                implementation(libs.ktor.engine.darwin)
            }
        }
        jsMain {
            dependencies {
                implementation(libs.ktor.engine.js)
            }
        }
        wasmJsMain {
            dependencies {
                implementation(libs.ktor.engine.js)
            }
        }
    }
}
