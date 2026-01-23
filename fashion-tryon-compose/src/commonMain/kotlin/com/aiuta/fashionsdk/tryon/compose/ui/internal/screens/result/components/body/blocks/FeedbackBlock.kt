package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.body.blocks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.feedback.AiutaTryOnFeedbackFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.SessionImageUIModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.analytic.sendLikeGenerationFeedback
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller.GenerationResultController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller.showThanksFeedbackBlock
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@Composable
internal fun FeedbackBlock(
    modifier: Modifier = Modifier,
    sessionImage: SessionImageUIModel,
    hazeState: HazeState,
    generationResultController: GenerationResultController,
    isInterfaceVisible: State<Boolean>,
) {
    val controller = LocalController.current
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current

    val feedbackFeature = provideFeature<AiutaTryOnFeedbackFeature>()

    val isFeedbackVisible = remember(
        sessionImage,
        isInterfaceVisible.value,
        feedbackFeature,
    ) {
        derivedStateOf {
            val isFeedbackNotProvided = !sessionImage.isFeedbackProvided
            val isFeedbackFeatureInit = feedbackFeature != null

            isFeedbackNotProvided && isFeedbackFeatureInit && isInterfaceVisible.value
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isFeedbackVisible.value,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        feedbackFeature?.let {
            FeedbackBlockContent(
                hazeState = hazeState,
                feedbackFeature = feedbackFeature,
                onDislikeClick = {
                    bottomSheetNavigator.show(
                        newSheetScreen = TryOnBottomSheetScreen.Feedback(
                            productIds = sessionImage.productIds,
                        ),
                    )
                    controller.sessionGenerationInteractor.setFeedbackAsProvided(sessionImage)
                },
                onLikeClick = {
                    controller.sendLikeGenerationFeedback(sessionImage)
                    generationResultController.showThanksFeedbackBlock()
                    controller.sessionGenerationInteractor.setFeedbackAsProvided(sessionImage)
                },
            )
        }
    }
}

@Composable
private fun FeedbackBlockContent(
    modifier: Modifier = Modifier,
    hazeState: HazeState,
    feedbackFeature: AiutaTryOnFeedbackFeature,
    onDislikeClick: () -> Unit,
    onLikeClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ReactionIcon(
            icon = feedbackFeature.icons.like36,
            hazeState = hazeState,
            onClick = {
                onLikeClick()
            },
        )

        Spacer(Modifier.width(8.dp))

        ReactionIcon(
            icon = feedbackFeature.icons.dislike36,
            hazeState = hazeState,
            onClick = {
                onDislikeClick()
            },
        )
    }
}

@Composable
private fun ReactionIcon(
    modifier: Modifier = Modifier,
    icon: AiutaIcon,
    hazeState: HazeState,
    onClick: () -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val theme = LocalTheme.current

    Box(
        modifier = modifier
            .size(38.dp)
            .border(
                width = 1.dp,
                color = theme.color.border,
                shape = CircleShape,
            )
            .clip(CircleShape)
            .hazeEffect(hazeState) {
                val sharedColor = theme.color.background.copy(alpha = 0.4f)

                blurRadius = 10.dp
                backgroundColor = sharedColor
                tints = listOf(HazeTint(sharedColor))
                fallbackTint = HazeTint(sharedColor)
            },
        contentAlignment = Alignment.Center,
    ) {
        AiutaIcon(
            modifier =
            Modifier
                .size(24.dp)
                .clickableUnindicated {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                },
            icon = icon,
            contentDescription = null,
            tint = theme.color.background.copy(alpha = 0.7f),
        )
    }
}
