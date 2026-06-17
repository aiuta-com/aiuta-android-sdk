package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.photo.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.feedback.AiutaTryOnFeedbackFeature
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.SessionImageUIModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenViewState
import dev.chrisbanes.haze.HazeState

@Composable
internal fun FeedbackBlock(
    state: GenerationResultScreenViewState,
    sessionImage: SessionImageUIModel,
    hazeState: HazeState,
    isInterfaceVisible: State<Boolean>,
    eventHandler: (GenerationResultScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val feedbackFeature = provideFeature<AiutaTryOnFeedbackFeature>()

    val isFeedbackVisible = remember(
        sessionImage,
        isInterfaceVisible.value,
        feedbackFeature,
        state.isFeedbackAvailable,
    ) {
        derivedStateOf {
            val isFeedbackNotProvided = !sessionImage.isFeedbackProvided
            val isFeedbackFeatureInit = state.isFeedbackAvailable && feedbackFeature != null

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
                    eventHandler(GenerationResultScreenEvent.DislikeClicked(sessionImage))
                },
                onLikeClick = {
                    eventHandler(GenerationResultScreenEvent.LikeClicked(sessionImage))
                },
            )
        }
    }
}

@Composable
private fun FeedbackBlockContent(
    hazeState: HazeState,
    feedbackFeature: AiutaTryOnFeedbackFeature,
    onDislikeClick: () -> Unit,
    onLikeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ReactionIcon(
            icon = feedbackFeature.icons.like36,
            hazeState = hazeState,
            onClick = onLikeClick,
        )

        Spacer(Modifier.height(8.dp))

        ReactionIcon(
            icon = feedbackFeature.icons.dislike36,
            hazeState = hazeState,
            onClick = onDislikeClick,
        )
    }
}
