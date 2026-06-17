package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.aiuta.fashionsdk.internal.navigation.bottomsheet.AiutaNavigationBottomSheetScreen
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.models.GenerationResultScreenEvent

/**
 * Ports the side-effect that used to live inside `ThanksFeedbackBlock`: once a feedback sheet closes,
 * show the "thanks" confirmation overlay and reset the navigator marker so it fires only once.
 */
@Composable
internal fun ResultFeedbackSheetListener(
    eventHandler: (GenerationResultScreenEvent) -> Unit,
) {
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current

    LaunchedEffect(bottomSheetNavigator.lastBottomSheetScreen.value) {
        val lastBottomSheetScreen = bottomSheetNavigator.lastBottomSheetScreen.value

        if (
            lastBottomSheetScreen is TryOnBottomSheetScreen.Feedback ||
            lastBottomSheetScreen is TryOnBottomSheetScreen.ExtraFeedback
        ) {
            eventHandler(GenerationResultScreenEvent.FeedbackProvidedViaSheet)
            bottomSheetNavigator.lastBottomSheetScreen.value = AiutaNavigationBottomSheetScreen.IDLE
        }
    }
}
