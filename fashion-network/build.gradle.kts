import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.multiplatformAndroidLibrary

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
    alias(libs.plugins.kotlinx.serialization)
}

addAllMultiplatformTargets()
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk.network") {
    androidResources {
        enable = true
    }
}

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
