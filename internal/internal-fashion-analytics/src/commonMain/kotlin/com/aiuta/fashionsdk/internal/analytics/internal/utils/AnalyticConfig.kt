package com.aiuta.fashionsdk.internal.analytics.internal.utils

internal object AnalyticConfig {
    const val PLATFORM_ANDROID = "android"
    const val PLATFORM_IOS = "ios"
    const val PLATFORM_MACOS = "macos"
    const val PLATFORM_WINDOWS = "windows"
    const val PLATFORM_LINUX = "linux"

    const val PLATFORM_WEB = "web"

    const val DEFAULT_ENDPOINT = "api.aiuta.com"
    const val DEFAULT_ENCODED_PATH = "analytics/v1/android-sdk-analytics"
    const val DEFAULT_SDK_VERSION = "NOT_RESOLVED"
    const val DEFAULT_HOST_ID = "NOT_RESOLVED"
    const val DEFAULT_HOST_VERSION = "NOT_RESOLVED"
    const val DEFAULT_PLATFORM = PLATFORM_ANDROID
}
