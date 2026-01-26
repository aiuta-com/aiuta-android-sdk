package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.sizefit.compose.domain.models.toCore
import com.aiuta.fashionsdk.sizefit.compose.domain.slice.AiutaConfigSlice
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.QuestionaryStep
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.RecommendationState
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigState
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigUiModel
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class QuestionaryViewModel(
    private val aiutaSizeFit: AiutaSizeFit,
    private val configSlice: AiutaConfigSlice,
    private val productCode: String,
    private val logger: AiutaLogger?,
    private val onBack: () -> Unit,
) : ViewModel() {
    private val _currentStep = MutableStateFlow<QuestionaryStep>(QuestionaryStep.FindSizeStep)
    val currentStep = _currentStep.asStateFlow()

    private val _configState = MutableStateFlow<SizeFitConfigState>(SizeFitConfigState.Loading)
    val configState = _configState.asStateFlow()

    // Error states
    private val _shouldShowQuestionaryErrorState = MutableStateFlow(false)
    val shouldShowQuestionaryErrorState = _shouldShowQuestionaryErrorState.asStateFlow()

    private val _recommendationState: MutableStateFlow<RecommendationState> =
        MutableStateFlow(RecommendationState.Idle)
    val recommendationState = _recommendationState.asStateFlow()

    init {
        initConfig()
    }

    private fun initConfig() {
        viewModelScope.launch {
            logger?.d("QuestionaryViewModel: initConfig()")

            _configState.value = SizeFitConfigState.Success(
                config = configSlice.provideConfigState() ?: SizeFitConfigUiModel(),
            )
        }
    }

    // Navigation
    fun navigateTo(step: QuestionaryStep) {
        logger?.d("QuestionaryViewModel: navigateTo() - $step")

        _shouldShowQuestionaryErrorState.value = false
        _currentStep.value = step
    }

    fun navigateBack() {
        logger?.d("QuestionaryViewModel: navigateBack()")

        _currentStep.value.previousStep?.let { prevStep ->
            _currentStep.value = prevStep
        } ?: onBack()
    }

    // Config
    fun updateConfig(newConfig: SizeFitConfigUiModel) {
        logger?.d("QuestionaryViewModel: updateConfig() - $newConfig")

        _configState.value = SizeFitConfigState.Success(newConfig)
    }

    fun updateErrorState(newState: Boolean) {
        logger?.d("QuestionaryViewModel: updateErrorState() - $newState")

        _shouldShowQuestionaryErrorState.value = newState
    }

    fun makeRecommendation() {
        viewModelScope.launch {
            runCatching {
                logger?.d("QuestionaryViewModel: makeRecommendation()")
                val configState = _configState.value
                check(configState is SizeFitConfigState.Success)

                _recommendationState.value = RecommendationState.Loading

                // Let's save config first
                configSlice.saveConfigState(configState.config)

                // Try to make recommendation
                val recommendation = aiutaSizeFit.recommendSize(
                    code = productCode,
                    config = configState.config.toCore(),
                )

                // Then move with result
                _recommendationState.value = RecommendationState.Success(recommendation)
            }.onFailure {
                _recommendationState.value = RecommendationState.Error
            }
        }
    }

    fun reset() {
        logger?.d("QuestionaryViewModel: reset()")

        _currentStep.value = QuestionaryStep.FindSizeStep
        _shouldShowQuestionaryErrorState.value = false
        _recommendationState.value = RecommendationState.Idle
    }
}
