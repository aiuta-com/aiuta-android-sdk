package com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationScreen

@Immutable
internal abstract class TryOnScreen : AiutaNavigationScreen() {

    object Splash : TryOnScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.WELCOME
        override val shouldSaveInBackStack: Boolean = false
    }

    object Preonboarding : TryOnScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.WELCOME
    }

    object Onboarding : TryOnScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.HOW_IT_WORKS
        override val shouldSaveInBackStack: Boolean = false
    }

    object ImageSelector : TryOnScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.IMAGE_PICKER
    }

    class Consent(
        val onObtainedConsents: () -> Unit,
    ) : TryOnScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.CONSENT
    }

    object ModelSelector : TryOnScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.IMAGE_PICKER
        override val shouldSaveInBackStack: Boolean = false
    }

    object GenerationResult : TryOnScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.RESULTS
    }

    // Utility screens
    object History : TryOnScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.HISTORY
    }

    class ImageListViewer(
        val pickedIndex: Int,
    ) : TryOnScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.HISTORY

        override fun transitionSpec(): ContentTransform? {
            val durationMillis = 500
            return fadeIn(animationSpec = tween(durationMillis = durationMillis)) togetherWith
                fadeOut(animationSpec = tween(durationMillis = durationMillis))
        }
    }
}

internal fun defaultStartScreen(): TryOnScreen = TryOnScreen.Splash
