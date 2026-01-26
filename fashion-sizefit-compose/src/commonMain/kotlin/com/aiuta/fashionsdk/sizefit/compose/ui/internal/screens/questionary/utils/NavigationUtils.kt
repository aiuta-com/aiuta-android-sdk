package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.configuration.features.sizefit.AiutaSizeFitFeature
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.QuestionaryViewModel
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.QuestionaryStep
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigState
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigUiModel
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitConfig

internal fun QuestionaryViewModel.navigateNextStep(
    makeRecommendation: () -> Unit,
) {
    val currentStep = currentStep.value
    val config = (configState.value as? SizeFitConfigState.Success)?.config

    when (currentStep) {
        QuestionaryStep.FindSizeStep -> {
            // Check config first
            val isConfigValid = config?.run {
                age != null && height != null && weight != null
            } ?: false

            if (!isConfigValid) {
                updateErrorState(true)
                return
            }

            navigateTo(QuestionaryStep.BellyShapeStep)
        }

        QuestionaryStep.BellyShapeStep -> {
            when {
                config?.gender == AiutaSizeFitConfig.Gender.FEMALE -> navigateTo(
                    QuestionaryStep.BraStep,
                )

                else -> makeRecommendation()
            }
        }

        QuestionaryStep.BraStep -> makeRecommendation()
    }
}

@Composable
internal fun solvePrimaryButtonText(
    stepState: State<QuestionaryStep>,
    configState: State<SizeFitConfigUiModel>,
    sizeFitFeature: AiutaSizeFitFeature,
): String = remember(
    stepState.value,
    configState.value,
) {
    when (stepState.value) {
        QuestionaryStep.FindSizeStep -> sizeFitFeature.strings.nextButton
        QuestionaryStep.BellyShapeStep -> {
            val config = configState.value

            when (config.gender) {
                AiutaSizeFitConfig.Gender.FEMALE -> sizeFitFeature.strings.nextButton
                AiutaSizeFitConfig.Gender.MALE -> sizeFitFeature.strings.findYourSizeButton
            }
        }

        QuestionaryStep.BraStep -> sizeFitFeature.strings.findYourSizeButton
    }
}

@Composable
internal fun isPrimaryButtonEnabled(
    stepState: State<QuestionaryStep>,
    configState: State<SizeFitConfigUiModel>,
): State<Boolean> = remember(
    stepState.value,
    configState.value,
) {
    derivedStateOf {
        when (stepState.value) {
            QuestionaryStep.FindSizeStep -> {
                val config = configState.value

                config.run { age != null && height != null && weight != null }
            }
            else -> true
        }
    }
}
