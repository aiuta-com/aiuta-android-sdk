package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaErrorSnackbarController
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.internal.navigation.snackbar.AiutaErrorSnackbarState
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.navigation.SizeFitScreen
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.QuestionaryViewModel
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.RecommendationState
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigState

@Immutable
internal class RecommendationLoadError(
    retryRecommendation: () -> Unit,
) : AiutaErrorSnackbarState {
    override val message: String? = null
    override val onRetry: () -> Unit = retryRecommendation
    override val onClose: (() -> Unit)? = null
}

@Composable
internal fun RecommendationStateListener(
    configState: State<SizeFitConfigState>,
    recommendationState: State<RecommendationState>,
    viewModel: QuestionaryViewModel,
) {
    val navigationController = LocalAiutaNavigationController.current
    val errorSnackbarController = LocalAiutaErrorSnackbarController.current

    LaunchedEffect(recommendationState.value) {
        when (val state = recommendationState.value) {
            is RecommendationState.Error -> {
                errorSnackbarController.showErrorState(
                    newErrorState = RecommendationLoadError(
                        retryRecommendation = viewModel::makeRecommendation,
                    ),
                )
            }
            is RecommendationState.Success -> {
                navigationController.navigateTo(
                    newScreen = SizeFitScreen.RecommendationResult(
                        config = configState.value,
                        recommendation = state.recommendation,
                    ),
                )
                viewModel.reset()
            }
            else -> Unit
        }
    }
}
