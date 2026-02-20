pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "aiuta-sdk"

// Need for generate project accessors in deps
// https://docs.gradle.org/7.4/userguide/declaring_dependencies.html#sec:type-safe-project-accessors
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// Private modules
include(
    ":samples:tryon-kmp",
    ":samples:tryon-kmp-android",
    ":samples:tryon-kmp-web",
)

// Public modules
include(
    ":internal:internal-fashion-analytics",
    ":internal:internal-fashion-navigation",
    ":internal:internal-fashion-storage",
    ":fashion",
    ":fashion-analytics",
    ":fashion-analytics-events",
    ":fashion-bom",
    ":fashion-compose-core",
    ":fashion-compose-resources",
    ":fashion-compose-uikit",
    ":fashion-configuration",
    ":fashion-configuration-defaults",
    ":fashion-configuration-defaults-icons",
    ":fashion-configuration-defaults-images",
    ":fashion-io",
    ":fashion-logger",
    ":fashion-network",
    ":fashion-network-paging",
    ":fashion-sizefit-compose",
    ":fashion-sizefit-core",
    ":fashion-tryon-compose",
    ":fashion-tryon-core",
    ":fashion-tryon-paging",
)
