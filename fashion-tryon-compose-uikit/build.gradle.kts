import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.androidLibrary

plugins {
    id("com.android.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

addAllMultiplatformTargets(
    // Compose placeholders not supported macos
    enableExtendedTargets = false,
)
androidLibrary(name = "com.aiuta.fashionsdk.tryon.compose.uikit")

kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.ktor.engine.okhttp)
            }
        }
        commonMain {
            dependencies {
                api(projects.fashionIo)
                api(projects.fashionComposeCore)
                api(projects.fashionConfiguration)

                implementation(compose.components.resources)
                implementation(compose.material)

                implementation(libs.coil3.compose)
                implementation(libs.coil3.network.ktor3)
                implementation(libs.compose.placeholder)
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.ktor.engine.okhttp)
            }
        }
        nativeMain {
            dependencies {
                implementation(libs.ktor.engine.darwin)
            }
        }
    }
}
