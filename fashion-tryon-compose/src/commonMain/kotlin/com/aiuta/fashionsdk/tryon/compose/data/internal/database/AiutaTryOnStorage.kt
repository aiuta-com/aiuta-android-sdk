package com.aiuta.fashionsdk.tryon.compose.data.internal.database

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.internal.storage.AiutaStorage

private const val AIUTA_TRY_ON_STORAGE_NAME = "aiuta_try_on_storage"

internal fun buildAiutaTryOnStorage(platformContext: AiutaPlatformContext): AiutaStorage = AiutaStorage.Factory().create(
    name = AIUTA_TRY_ON_STORAGE_NAME,
    platformContext = platformContext,
)
