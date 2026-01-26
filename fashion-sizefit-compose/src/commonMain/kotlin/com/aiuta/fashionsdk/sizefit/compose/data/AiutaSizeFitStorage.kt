package com.aiuta.fashionsdk.sizefit.compose.data

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.internal.storage.AiutaStorage

private const val AIUTA_SIZEFIT_STORAGE_NAME = "aiuta_size_fit_storage"

internal fun buildAiutaSizeFitStorage(platformContext: AiutaPlatformContext): AiutaStorage = AiutaStorage.Factory().create(
    name = AIUTA_SIZEFIT_STORAGE_NAME,
    platformContext = platformContext,
)
