import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.androidLibrary

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.kotlinx.serialization)
}

addAllMultiplatformTargets(enableExtendedTargets = false)
androidLibrary(name = "com.aiuta.fashionsdk.sizefit.compose")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.fashionConfiguration)
                api(projects.fashionSizefitCore)

                implementation(libs.kotlinx.serialization)
                implementation(projects.internal.internalFashionNavigation)
                implementation(projects.internal.internalFashionStorage)
            }
        }
    }
}
