import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.androidLibrary

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

addAllMultiplatformTargets()
androidLibrary(name = "com.aiuta.fashionsdk.configuration.defaults.icons")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.fashionConfiguration)

                implementation(compose.foundation)
                implementation(compose.components.resources)
            }
        }
    }
}
