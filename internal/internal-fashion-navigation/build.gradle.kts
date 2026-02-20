import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.multiplatformAndroidLibrary

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

addAllMultiplatformTargets()
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk.internal.navigation")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.fashionAnalyticsEvents)
                api(projects.fashionComposeUikit)

                implementation(libs.jetbrains.compose.animation)
                implementation(libs.jetbrains.compose.runtime)
                implementation(libs.jetbrains.compose.material)
                implementation(libs.jetbrains.lifecycle)
                implementation(libs.jetbrains.viewmodel)
                implementation(projects.internal.internalFashionAnalytics)
            }
        }
    }
}
