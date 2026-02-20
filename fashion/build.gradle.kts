import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.multiplatformAndroidLibrary

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
}

addAllMultiplatformTargets()
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.fashionLogger)
            }
        }
    }
}
