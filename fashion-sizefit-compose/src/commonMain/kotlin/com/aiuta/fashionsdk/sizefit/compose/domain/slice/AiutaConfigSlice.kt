package com.aiuta.fashionsdk.sizefit.compose.domain.slice

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.internal.storage.AiutaStorage
import com.aiuta.fashionsdk.sizefit.compose.data.buildAiutaSizeFitStorage
import com.aiuta.fashionsdk.sizefit.compose.domain.models.AiutaSizeFitConfigDTO
import com.aiuta.fashionsdk.sizefit.compose.domain.models.toDTO
import com.aiuta.fashionsdk.sizefit.compose.domain.models.toUiState
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigState

internal class AiutaConfigSlice(
    private val storage: AiutaStorage,
) {
    suspend fun provideConfigState(): SizeFitConfigState? = storage.get(
        key = SIZEFIT_CONFIG_KEY,
        serializer = AiutaSizeFitConfigDTO.serializer(),
    )?.toUiState()

    suspend fun saveConfigState(uiConfig: SizeFitConfigState) {
        storage.save(
            key = SIZEFIT_CONFIG_KEY,
            value = uiConfig.toDTO(),
            serializer = AiutaSizeFitConfigDTO.serializer(),
        )
    }

    companion object {
        private const val SIZEFIT_CONFIG_KEY = "sizefit_config_key"

        fun create(platformContext: AiutaPlatformContext): AiutaConfigSlice = AiutaConfigSlice(
            storage = buildAiutaSizeFitStorage(
                platformContext = platformContext,
            ),
        )
    }
}
