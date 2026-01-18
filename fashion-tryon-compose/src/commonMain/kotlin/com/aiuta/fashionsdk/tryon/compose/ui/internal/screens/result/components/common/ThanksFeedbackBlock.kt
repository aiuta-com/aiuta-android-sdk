package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.configuration.features.tryon.feedback.AiutaTryOnFeedbackFeature
import com.aiuta.fashionsdk.internal.navigation.bottomsheet.AiutaNavigationBottomSheetScreen
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller.GenerationResultController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.controller.showThanksFeedbackBlock
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.provideFeature
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaIcon

@Composable
internal fun ThanksFeedbackBlock(
    modifier: Modifier = Modifier,
    generationResultController: GenerationResultController,
) {
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
    val feedbackFeature = provideFeature<AiutaTryOnFeedbackFeature>()

    val isVisible = remember(
        generationResultController.isThanksFeedbackBlockVisible.value,
    ) {
        derivedStateOf {
            val isThanksFeedbackBlockVisible =
                generationResultController.isThanksFeedbackBlockVisible.value
            val isFeedbackFeatureAvailable = feedbackFeature != null

            isThanksFeedbackBlockVisible && isFeedbackFeatureAvailable
        }
    }

    LaunchedEffect(bottomSheetNavigator.lastBottomSheetScreen.value) {
        val lastBottomSheetScreen = bottomSheetNavigator.lastBottomSheetScreen.value

        if (
            lastBottomSheetScreen is TryOnBottomSheetScreen.Feedback ||
            lastBottomSheetScreen is TryOnBottomSheetScreen.ExtraFeedback
        ) {
            generationResultController.showThanksFeedbackBlock()
            bottomSheetNavigator.lastBottomSheetScreen.value = AiutaNavigationBottomSheetScreen.IDLE
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible.value,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        feedbackFeature?.let {
            ThanksFeedbackBlockContent(feedbackFeature = feedbackFeature)
        }
    }
}

@Composable
private fun ThanksFeedbackBlockContent(
    modifier: Modifier = Modifier,
    feedbackFeature: AiutaTryOnFeedbackFeature,
) {
    val theme = LocalTheme.current

    Column(
        modifier =
        modifier
            .width(168.dp)
            .background(
                color = Color.Black.copy(alpha = 0.8f),
                shape = RoundedCornerShape(24.dp),
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(45.dp))

        AiutaIcon(
            modifier = Modifier.size(40.dp),
            icon = feedbackFeature.icons.gratitude40,
            contentDescription = null,
            tint = Color.Unspecified,
        )

        Spacer(Modifier.height(24.dp))

        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = feedbackFeature.strings.tryOnFeedbackGratitudeText,
            style = theme.label.typography.regular,
            color = theme.color.onDark,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(20.dp))
    }
}
