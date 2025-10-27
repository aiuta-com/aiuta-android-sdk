package com.aiuta.fashionsdk.tryon.compose.domain.internal.share.utils

import androidx.core.content.FileProvider

/**
 * Custom FileProvider for Aiuta SDK to avoid conflicts with app's FileProvider.
 * This class extends androidx.core.content.FileProvider with no additional functionality,
 * allowing the SDK to have its own FileProvider instance without conflicting
 * with the host application's FileProvider configuration.
 */
public class AiutaTryOnFileProvider : FileProvider()

internal const val AIUTA_TRYON_FILE_PROVIDER_AUTHORITY = "com.aiuta.fashionsdk.fileprovider"
