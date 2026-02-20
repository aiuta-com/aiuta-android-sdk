import com.aiuta.fashionsdk.addAllMultiplatformTargets
import com.aiuta.fashionsdk.multiplatformAndroidLibrary

plugins {
    id("com.android.kotlin.multiplatform.library")
    id("kotlin-multiplatform")
}

addAllMultiplatformTargets(
    // TODO Add targets with paging v3.4.0
    enableExtendedTargets = false,
)
multiplatformAndroidLibrary(name = "com.aiuta.fashionsdk.tryon.paging")

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.fashionTryonCore)

                api(libs.androidx.paging.common)
            }
        }
    }
}
