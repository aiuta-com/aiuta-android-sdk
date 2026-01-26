package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiuta.fashionsdk.compose.core.context.LocalAiutaPlatformContext
import com.aiuta.fashionsdk.compose.core.size.rememberScreenSize
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.sizefit.AiutaSizeFitFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaLogger
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.internal.navigation.internal.utils.leftToRightTransition
import com.aiuta.fashionsdk.internal.navigation.internal.utils.rightToLeftTransition
import com.aiuta.fashionsdk.sizefit.compose.domain.slice.AiutaConfigSlice
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.composition.LocalAiutaSizeFit
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.blocks.BellyShapeScreen
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.blocks.BraScreen
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.blocks.FindSizeBlock
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.components.QuestionaryAppBar
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.QuestionaryStep
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.RecommendationState
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigState
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.SizeFitConfigUiModel
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.state.questionarySteps
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.utils.RecommendationStateListener
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.utils.isPrimaryButtonEnabled
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.utils.navigateNextStep
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.questionary.utils.solvePrimaryButtonText

@Composable
internal fun QuestionaryScreen(
    productCode: String,
    modifier: Modifier = Modifier,
) {
    val aiutaSizeFit = LocalAiutaSizeFit.current
    val platformContext = LocalAiutaPlatformContext.current
    val navigationController = LocalAiutaNavigationController.current
    val theme = LocalTheme.current
    val logger = LocalAiutaLogger.current

    val sizeFitFeature = strictProvideFeature<AiutaSizeFitFeature>()
    val viewModel: QuestionaryViewModel = viewModel {
        QuestionaryViewModel(
            aiutaSizeFit = aiutaSizeFit,
            configSlice = AiutaConfigSlice.create(
                platformContext = platformContext,
            ),
            productCode = productCode,
            logger = logger,
            sizeFitDataProvider = sizeFitFeature.dataProvider,
            onBack = navigationController::navigateBack,
        )
    }
    val stepState = viewModel.currentStep.collectAsState()
    val configState = viewModel.configState.collectAsState()
    val shouldShowQuestionaryErrorState = viewModel.shouldShowQuestionaryErrorState.collectAsState()
    val recommendationState = viewModel.recommendationState.collectAsState()

    val isRecommendationLoading = remember {
        derivedStateOf { recommendationState.value is RecommendationState.Loading }
    }

    val screenSize = rememberScreenSize()
    val topPadding = screenSize.heightDp * 0.07f

    val shouldShowContent = remember {
        derivedStateOf { configState.value is SizeFitConfigState.Success }
    }

    AnimatedVisibility(
        visible = shouldShowContent.value,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        val config = remember {
            derivedStateOf {
                (configState.value as SizeFitConfigState.Success).config
            }
        }

        RecommendationStateListener(
            configState = config,
            recommendationState = recommendationState,
            viewModel = viewModel,
        )

        Column(
            modifier = modifier
                .background(theme.color.background)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            QuestionaryAppBar(
                stepState = stepState,
                navigateBack = viewModel::navigateBack,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(topPadding))

            QuestionaryScreenContent(
                configState = config,
                stepState = stepState,
                shouldShowQuestionaryErrorState = shouldShowQuestionaryErrorState,
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth().weight(1f),
            )

            FashionButton(
                modifier = Modifier.fillMaxWidth(),
                text = solvePrimaryButtonText(
                    stepState = stepState,
                    configState = config,
                    sizeFitFeature = sizeFitFeature,
                ),
                style = FashionButtonStyles.primaryStyle(theme),
                size = FashionButtonSizes.lSize(),
                isLoading = isRecommendationLoading.value,
                isEnable = isPrimaryButtonEnabled(
                    stepState = stepState,
                    configState = config,
                ).value,
                onClick = {
                    viewModel.navigateNextStep(
                        makeRecommendation = viewModel::makeRecommendation,
                    )
                },
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
internal fun QuestionaryScreenContent(
    configState: State<SizeFitConfigUiModel>,
    stepState: State<QuestionaryStep>,
    shouldShowQuestionaryErrorState: State<Boolean>,
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
                    shouldShowErrorState = shouldShowQuestionaryErrorState,
                    updateConfig = viewModel::updateConfig,
                    modifier = sharedModifier,
                )
            }

            is QuestionaryStep.BellyShapeStep -> {
                BellyShapeScreen(
                    configState = configState,
                    updateConfig = viewModel::updateConfig,
                    modifier = sharedModifier,
                )
            }

            is QuestionaryStep.BraStep -> {
                BraScreen(
                    configState = configState,
                    updateConfig = viewModel::updateConfig,
                    modifier = sharedModifier,
                )
            }
        }
    }
}
