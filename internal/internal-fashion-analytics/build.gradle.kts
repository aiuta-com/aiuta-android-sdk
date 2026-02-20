import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.multiplatformAndroidLibrary
import com.aiuta.fashionsdk.versionName
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.kotlinx.serialization)
}

addAllMultiplatformTargets()
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk.internal.analytic")

buildkonfig {
    packageName = "com.aiuta.fashionsdk.internal.analytic"
    defaultConfigs {
        buildConfigField(STRING, "VERSION_NAME", versionName)
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.fashion)
                api(projects.fashionNetwork)
                api(projects.fashionAnalyticsEvents)

                implementation(libs.kotlinx.atomicfu)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization)
                implementation(libs.ktor.core)
            }
        }
        jsMain {
            dependencies {
                implementation(libs.kotlinx.io)
            }
        }
        wasmJsMain {
            dependencies {
                implementation(libs.kotlinx.browser)
                implementation(libs.kotlinx.io)
            }
        }
    }
}
