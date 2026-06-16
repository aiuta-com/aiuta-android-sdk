package com.aiuta.fashionsdk.configuration.features.onboarding.dataprovider

import com.aiuta.fashionsdk.configuration.mode.AiutaMode
import kotlinx.coroutines.flow.Flow

/**
 * Custom implementation of the `AiutaOnboardingFeatureDataProvider` interface.
 * Provides logic for managing the onboarding process state per [AiutaMode].
 */
public interface AiutaOnboardingFeatureDataProviderCustom : AiutaOnboardingFeatureDataProvider {
    /**
     * A [Flow] that emits the completion status of the onboarding process per [AiutaMode].
     *
     * The emitted map associates each [AiutaMode] with `true` when its onboarding has been
     * completed. A mode that is absent from the map (or mapped to `false`) is treated as
     * not completed.
     */
    public val isOnboardingCompleted: Flow<Map<AiutaMode, Boolean>>

    /**
     * Marks the onboarding process as completed for the given [mode].
     * This method should be called when the onboarding process is successfully finished.
     *
     * @param mode The [AiutaMode] whose onboarding has been completed.
     */
    public suspend fun completeOnboarding(mode: AiutaMode)
}
