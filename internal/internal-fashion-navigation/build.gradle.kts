import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.androidLibrary

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

addAllMultiplatformTargets()
androidLibrary(name = "com.aiuta.fashionsdk.internal.navigation")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.fashionAnalyticsEvents)
                api(projects.fashionComposeUikit)

                implementation(libs.jetbrains.compose.animation)
                implementation(libs.jetbrains.compose.runtime)
                implementation(libs.jetbrains.compose.material)
                implementation(projects.internal.internalFashionAnalytics)
            }
        }
    }
}
