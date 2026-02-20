import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.multiplatformAndroidLibrary

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

addAllMultiplatformTargets()
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk.compose.resources") {
    androidResources {
        enable = true
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.jetbrains.compose.components.resources)
                implementation(libs.jetbrains.compose.foundation)
            }
        }
    }
}
