package com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationScreen

/**
 * Be careful, order is matter for animation transitions,
 */
@Immutable
internal abstract class TryOnScreen : AiutaNavigationScreen() {

    object Splash : TryOnScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.WELCOME
    }

    object Preonboarding : TryOnScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.WELCOME
    }

    object Onboarding : TryOnScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.HOW_IT_WORKS
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

// Stack
@Deprecated("Remove after migration to new nav library")
private val screenStacks =
    listOf(
        TryOnScreen.Splash,
        TryOnScreen.Preonboarding,
        TryOnScreen.Onboarding,
        TryOnScreen.ImageSelector,
        TryOnScreen.ModelSelector,
        TryOnScreen.GenerationResult,
        // Utils
        TryOnScreen.History,
    )

@Deprecated("Remove after migration to new nav library")
internal fun screenPosition(screen: TryOnScreen): Int = screenStacks.indexOf(screen)

@Deprecated("Remove after migration to new nav library")
internal fun AnimatedContentTransitionScope<TryOnScreen>.solveTransitionAnimation(): ContentTransform? {
    val initialTransition = initialState.transitionSpec()
    val targetState = targetState.transitionSpec()

    return initialTransition ?: targetState
}
