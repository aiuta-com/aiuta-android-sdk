import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.multiplatformAndroidLibrary

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
}

addAllMultiplatformTargets()
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk.io")

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.androidx.exifinterface)
            }
        }
        commonMain {
            dependencies {
                api(projects.fashion)

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        jsMain {
            dependencies {
                implementation(libs.kotlinx.io)
            }
        }
        wasmJsMain {
            dependencies {
                implementation(libs.kotlinx.browser)
                implementation(libs.kotlinx.io)
            }
        }
    }
}
