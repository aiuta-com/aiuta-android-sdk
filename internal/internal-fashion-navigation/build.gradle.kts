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
                api(projects.fashionTryonComposeUikit) // TODO Rename to more general uikit?

                // TODO Remove compose. deps
                implementation(compose.animation)
                implementation(compose.runtime)
                implementation(libs.jetbrains.compose.material)
                implementation(projects.internal.internalFashionAnalytics)
            }
        }
    }
}
