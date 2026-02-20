import com.aiuta.fashionsdk.applyAiutaHierarchyTemplate
import com.aiuta.fashionsdk.desktopMain
import com.aiuta.fashionsdk.multiplatformAndroidLibrary
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    alias(libs.plugins.buildKonfig)
}

multiplatformAndroidLibrary(name = "sample.tryon.kmp")

buildkonfig {
    packageName = "sample.tryon.kmp"

    // Let's load api key from secrets.properties in root of project
    val props = Properties()
    try {
        props.load(file("${rootProject.projectDir.absolutePath}/secrets.properties").inputStream())
    } catch (e: Exception) {
        // keys are private and can not be committed to git
    }

    defaultConfigs {
        buildConfigField(STRING, "AIUTA_API_KEY", props["AIUTA_API_KEY"].toString())
        buildConfigField(STRING, "SIZEFIT_API_KEY", props["SIZEFIT_API_KEY"].toString())
        buildConfigField(STRING, "SIZEFIT_PARTITION", props["SIZEFIT_PARTITION"].toString())
        buildConfigField(STRING, "SIZEFIT_PRODUCT_CODE", props["SIZEFIT_PRODUCT_CODE"].toString())
    }
}

kotlin {
    applyAiutaHierarchyTemplate()

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(projects.fashionTryonCompose)
            implementation(projects.fashionSizefitCompose)
            implementation(projects.fashionConfigurationDefaults)

            implementation(libs.jetbrains.compose.foundation)
            implementation(libs.jetbrains.compose.material)

            implementation(libs.jetbrains.viewmodel)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "sample.tryon.kmp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "sample.tryon.kmp"
            packageVersion = "1.0.0"
        }
    }
}
