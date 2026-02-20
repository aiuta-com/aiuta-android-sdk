import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.multiplatformAndroidLibrary

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

addAllMultiplatformTargets()
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk.compose.core")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.fashion)

                implementation(libs.jetbrains.compose.foundation)
            }
        }
    }
}
