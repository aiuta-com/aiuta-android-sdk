package com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.onboarding

import com.aiuta.fashionsdk.configuration.mode.AiutaMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class EmptyOnboardingInteractor : OnboardingInteractor {

    override val isOnboardingCompleted: Flow<Map<AiutaMode, Boolean>> = MutableStateFlow(emptyMap())

    override suspend fun completeOnboarding(mode: AiutaMode) {
        error(
            message = """
            You are trying to call onboarding data provider without initialization.
            Please, add AiutaOnboardingFeatureDataProvider in AiutaOnboardingFeature to use this functionality.
            """.trimIndent(),
        )
    }
}
