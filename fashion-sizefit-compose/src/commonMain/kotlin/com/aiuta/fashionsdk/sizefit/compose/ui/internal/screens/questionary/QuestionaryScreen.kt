package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.sizefit.AiutaSizeFitFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.internal.navigation.internal.utils.leftToRightTransition
import com.aiuta.fashionsdk.internal.navigation.internal.utils.rightToLeftTransition
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.blocks.BellyShapeScreen
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.blocks.BraScreen
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.blocks.FindSizeBlock
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.components.QuestionaryAppBar
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.QuestionaryStep
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigState
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.questionarySteps
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.utils.isPrimaryButtonEnabled
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.utils.navigateNextStep
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.utils.solvePrimaryButtonText

@Composable
internal fun QuestionaryScreen(
    modifier: Modifier = Modifier,
) {
    val navigationController = LocalAiutaNavigationController.current
    val theme = LocalTheme.current

    val viewModel: QuestionaryViewModel = viewModel {
        QuestionaryViewModel(
            onBack = navigationController::navigateBack,
        )
    }
    val stepState = viewModel.currentStep.collectAsState()
    val configState = viewModel.configState.collectAsState()
    val shouldShowErrorState = viewModel.shouldShowErrorState.collectAsState()
    val sizeFitFeature = strictProvideFeature<AiutaSizeFitFeature>()

    Column(
        modifier = modifier
            .background(theme.color.background)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        QuestionaryAppBar(
            stepState = stepState,
            navigateBack = viewModel::navigateBack,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(64.dp))

        QuestionaryScreenContent(
            configState = configState,
            stepState = stepState,
            shouldShowErrorState = shouldShowErrorState,
            viewModel = viewModel,
            modifier = Modifier.fillMaxWidth().weight(1f),
        )

        FashionButton(
            modifier = Modifier.fillMaxWidth(),
            text = solvePrimaryButtonText(
                stepState = stepState,
                configState = configState,
                sizeFitFeature = sizeFitFeature,
            ),
            style = FashionButtonStyles.primaryStyle(theme),
            size = FashionButtonSizes.lSize(),
            isLoading = false, // TODO
            isEnable = isPrimaryButtonEnabled(
                stepState = stepState,
                configState = configState,
            ).value,
            onClick = {
                viewModel.navigateNextStep(sizeFitFeature)
            },
        )
    }
}

@Composable
internal fun QuestionaryScreenContent(
    configState: State<SizeFitConfigState>,
    stepState: State<QuestionaryStep>,
    shouldShowErrorState: State<Boolean>,
    viewModel: QuestionaryViewModel,
    modifier: Modifier = Modifier,
) {
    val sharedModifier = Modifier.fillMaxSize()

    AnimatedContent(
        targetState = stepState.value,
        modifier = modifier,
        transitionSpec = {
            val initialIndex = questionarySteps.indexOf(initialState)
            val targetIndex = questionarySteps.indexOf(targetState)

            if (initialIndex < targetIndex) {
                rightToLeftTransition
            } else {
                leftToRightTransition
            }
        },
    ) { state ->
        when (state) {
            is QuestionaryStep.FindSizeStep -> {
                FindSizeBlock(
                    configState = configState,
                    shouldShowErrorState = shouldShowErrorState,
                    updateConfig = viewModel::updateConfig,
                    modifier = sharedModifier,
                )
            }

            is QuestionaryStep.BellyShapeStep -> {
                BellyShapeScreen(
                    modifier = sharedModifier,
                )
            }

            is QuestionaryStep.BraStep -> {
                BraScreen(
                    modifier = sharedModifier,
                )
            }
        }
    }
}
