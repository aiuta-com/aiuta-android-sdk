import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.androidLibrary

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
    alias(libs.plugins.kotlinx.serialization)
}

addAllMultiplatformTargets()
androidLibrary(name = "com.aiuta.fashionsdk.sizefit.core")

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.ktor.engine.okhttp)
            }
        }
        commonMain {
            dependencies {
                api(projects.fashion)

                implementation(libs.kotlinx.serialization)
                implementation(libs.ktor.core)
                implementation(libs.ktor.negotiation)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.logging)
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
