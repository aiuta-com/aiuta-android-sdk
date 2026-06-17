package com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.onboarding

import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.internal.storage.AiutaStorage
import com.aiuta.fashionsdk.tryon.compose.data.internal.database.buildAiutaTryOnStorage
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.serializer

/**
 * Persists the set of [com.aiuta.fashionsdk.configuration.mode.AiutaMode] names whose onboarding
 * has been completed. Backed by [AiutaStorage] (DataStore preferences), replacing the previous
 * Room-based onboarding storage.
 */
internal class OnboardingLocalDataSource(
    private val storage: AiutaStorage,
) {
    suspend fun getCompletedModes(): Set<String> = storage.get(
        key = COMPLETED_MODES_KEY,
        serializer = SET_SERIALIZER,
    ).orEmpty()

    suspend fun saveCompletedModes(value: Set<String>) {
        storage.save(
            key = COMPLETED_MODES_KEY,
            value = value,
            serializer = SET_SERIALIZER,
        )
    }

    companion object {
        private const val COMPLETED_MODES_KEY = "onboarding_completed_modes"
        private val SET_SERIALIZER = SetSerializer(String.serializer())

        fun getInstance(platformContext: AiutaPlatformContext): OnboardingLocalDataSource = OnboardingLocalDataSource(
            storage = buildAiutaTryOnStorage(platformContext),
        )
    }
}
