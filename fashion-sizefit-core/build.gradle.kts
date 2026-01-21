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
        commonMain {
            dependencies {
                api(projects.fashion)

                implementation(projects.fashionNetwork)
                implementation(libs.kotlinx.serialization)
                implementation(libs.ktor.core)
                implementation(libs.ktor.negotiation)
                implementation(libs.ktor.serialization)
                implementation(libs.ktor.logging)
            }
        }
    }
}
