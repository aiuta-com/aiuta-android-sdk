import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.multiplatformAndroidLibrary

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

addAllMultiplatformTargets()
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk.compose.uikit") {
    androidResources {
        enable = true
    }
}

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.ktor.engine.okhttp)
            }
        }
        commonMain {
            dependencies {
                api(projects.fashionIo)
                api(projects.fashionComposeCore)
                api(projects.fashionConfiguration)

                implementation(libs.coil3.compose)
                implementation(libs.coil3.network.ktor3)
                implementation(libs.compose.placeholder)
                implementation(libs.jetbrains.compose.components.resources)
                implementation(libs.jetbrains.compose.material)
                implementation(libs.ksoup.html)
                implementation(libs.ksoup.entities)
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
