import com.aiuta.fashionsdk.androidApplication

plugins {
    id("com.android.application")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

androidApplication(name = "sample.tryon.kmp.android") {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                "./shrinker-rules.pro",
                "./shrinker-rules-android.pro",
            )
            signingConfig = signingConfigs["debug"]
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(projects.samples.tryonKmp)
}
