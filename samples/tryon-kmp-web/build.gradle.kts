import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.buildKonfig)
}

buildkonfig {
    packageName = "sample.tryon.kmp.web"

    // Let's load api key from secrets.properties in root of project
    val props = Properties()
    try {
        props.load(file("${rootProject.projectDir.absolutePath}/secrets.properties").inputStream())
    } catch (e: Exception) {
        // keys are private and can not be committed to git
    }

    defaultConfigs {
        buildConfigField(STRING, "AIUTA_API_KEY", props["AIUTA_API_KEY"].toString())
    }
}

kotlin {
    js {
        outputModuleName = "aiutaWebSample"
        browser {
            commonWebpackConfig {
                outputFileName = "aiutaWebSample.js"
            }
        }
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "aiutaWebSample"
        browser {
            commonWebpackConfig {
                outputFileName = "aiutaWebSample.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
//            implementation(projects.fashionTryonCompose)
//            implementation(projects.fashionConfigurationDefaults)

            implementation(compose.foundation)
            implementation(compose.material3)

            implementation(libs.jetbrains.viewmodel)
        }
    }
}
