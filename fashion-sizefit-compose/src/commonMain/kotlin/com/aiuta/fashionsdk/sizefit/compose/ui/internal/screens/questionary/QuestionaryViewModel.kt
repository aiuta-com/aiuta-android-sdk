package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary

import androidx.lifecycle.ViewModel
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.QuestionaryStep
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class QuestionaryViewModel(
    private val onBack: () -> Unit,
) : ViewModel() {
    private val _currentStep = MutableStateFlow<QuestionaryStep>(QuestionaryStep.FindSizeStep)
    val currentStep = _currentStep.asStateFlow()

    private val _configState = MutableStateFlow(SizeFitConfigState())
    val configState = _configState.asStateFlow()

    // Navigation
    fun navigateTo(step: QuestionaryStep) {
        _currentStep.value = step
    }

    fun navigateBack() {
        _currentStep.value.previousStep?.let { prevStep ->
            _currentStep.value = prevStep
        } ?: onBack()
    }

    // Config
    fun updateConfig(newConfig: SizeFitConfigState) {
        _configState.value = newConfig
    }
}
