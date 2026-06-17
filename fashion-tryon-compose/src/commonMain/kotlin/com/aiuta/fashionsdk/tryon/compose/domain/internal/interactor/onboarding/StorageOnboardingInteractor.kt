package com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.onboarding

import com.aiuta.fashionsdk.configuration.mode.AiutaMode
import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.onboarding.OnboardingLocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Built-in onboarding state provider backed by [OnboardingLocalDataSource] ([com.aiuta.fashionsdk.internal.storage.AiutaStorage]).
 *
 * Tracks completion per [AiutaMode]. The completion map is loaded from storage once on creation and
 * kept in memory via a [MutableStateFlow]; [completeOnboarding] persists the change and updates the flow.
 */
internal class StorageOnboardingInteractor(
    private val scope: CoroutineScope,
    private val onboardingLocalDataSource: OnboardingLocalDataSource,
) : OnboardingInteractor {

    private val _isOnboardingCompleted = MutableStateFlow<Map<AiutaMode, Boolean>>(emptyMap())
    override val isOnboardingCompleted: Flow<Map<AiutaMode, Boolean>> = _isOnboardingCompleted.asStateFlow()

    init {
        scope.launch {
            _isOnboardingCompleted.value = onboardingLocalDataSource.getCompletedModes().toCompletionMap()
        }
    }

    override suspend fun completeOnboarding(mode: AiutaMode) {
        val updatedNames = onboardingLocalDataSource.getCompletedModes() + mode.name
        onboardingLocalDataSource.saveCompletedModes(updatedNames)
        _isOnboardingCompleted.update { updatedNames.toCompletionMap() }
    }

    private fun Set<String>.toCompletionMap(): Map<AiutaMode, Boolean> = mapNotNull { name ->
        runCatching { AiutaMode.valueOf(name) }.getOrNull()
    }.associateWith { true }

    companion object {
        fun getInstance(
            scope: CoroutineScope,
            platformContext: AiutaPlatformContext,
        ): StorageOnboardingInteractor = StorageOnboardingInteractor(
            scope = scope,
            onboardingLocalDataSource = OnboardingLocalDataSource.getInstance(platformContext),
        )
    }
}
