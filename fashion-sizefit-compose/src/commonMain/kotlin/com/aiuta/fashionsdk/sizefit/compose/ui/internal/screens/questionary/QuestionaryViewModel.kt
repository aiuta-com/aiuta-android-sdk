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

    // TODO Base init from storage
    private val _configState = MutableStateFlow(SizeFitConfigState())
    val configState = _configState.asStateFlow()

    private val _shouldShowErrorState = MutableStateFlow(false)
    val shouldShowErrorState = _shouldShowErrorState.asStateFlow()

    // Navigation
    fun navigateTo(step: QuestionaryStep) {
        _shouldShowErrorState.value = false
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

    fun updateErrorState(newState: Boolean) {
        _shouldShowErrorState.value = newState
    }
}
