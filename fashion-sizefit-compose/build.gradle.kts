import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.multiplatformAndroidLibrary

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.kotlinx.serialization)
}

addAllMultiplatformTargets(enableExtendedTargets = false)
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk.sizefit.compose")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.fashionConfiguration)
                api(projects.fashionSizefitCore)

                implementation(libs.kotlinx.serialization)
                implementation(libs.jetbrains.compose.material)
                implementation(libs.jetbrains.compose.ui.backhandler)
                implementation(libs.jetbrains.lifecycle)
                implementation(libs.jetbrains.viewmodel)
                implementation(projects.internal.internalFashionNavigation)
                implementation(projects.internal.internalFashionStorage)
            }
        }
    }
}
