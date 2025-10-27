package com.aiuta.fashionsdk.tryon.compose.ui.internal.utils

import androidx.compose.ui.platform.UriHandler

internal fun UriHandler.safeOpenUri(uri: String) {
    try {
        openUri(uri)
    } catch (e: Exception) {
        // Failed to open URI
    }
}
