package com.aiuta.fashionsdk

val publicModules = setOf(
    "fashion",
    "fashion-analytics",
    "fashion-analytics-events",
    "fashion-compose-core",
    "fashion-compose-resources",
    "fashion-compose-uikit",
    "fashion-configuration",
    "fashion-configuration-defaults",
    "fashion-configuration-defaults-icons",
    "fashion-configuration-defaults-images",
    "fashion-io",
    "fashion-logger",
    "fashion-network",
    "fashion-network-paging",
    "fashion-sizefit-compose",
    "fashion-sizefit-core",
    "fashion-tryon-compose",
    "fashion-tryon-core",
    "fashion-tryon-paging",
    "internal-fashion-analytics",
    "internal-fashion-navigation",
    "internal-fashion-storage",
)

fun publicModulePath(moduleName: String): String {
    return buildString {
        append(":")
        if (moduleName.startsWith("internal")) {
            append("internal:")
        }
        append(moduleName)
    }
}
