import com.aiuta.fashionsdk.androidApplication

plugins {
    id("com.android.application")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    // Creates nonMinifiedRelease / benchmarkRelease variants for Baseline
    // Profile generation; the real `release` build stays minified.
    id("androidx.baselineprofile.apptarget")
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
                getDefaultProguardFile("proguard-android-optimize.txt"),
            )
            signingConfig = signingConfigs["debug"]
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(projects.samples.tryonKmp)

    // Needed for the testTagsAsResourceId root wrapper that exposes Compose
    // testTags to the Baseline Profile generator (UiAutomator By.res(...)).
    implementation(compose.foundation)
    implementation(compose.ui)
}
