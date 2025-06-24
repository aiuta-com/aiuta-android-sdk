package com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import kotlin.random.Random

/**
 * Be careful, order is matter for animation transitions,
 */
internal abstract class NavigationScreen {
    val id: String = Random.nextInt().toString()
    abstract val exitPageId: AiutaAnalyticsPageId

    open fun transitionSpec(): ContentTransform? = null

    object Splash : NavigationScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.WELCOME
    }

    object Preonboarding : NavigationScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.WELCOME
    }

    object Onboarding : NavigationScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.HOW_IT_WORKS
    }

    object ImageSelector : NavigationScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.IMAGE_PICKER
    }

    class Consent(
        val onObtainedConsents: () -> Unit,
    ) : NavigationScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.CONSENT
    }

    object ModelSelector : NavigationScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.IMAGE_PICKER
    }

    object GenerationResult : NavigationScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.RESULTS
    }

    // Utility screens
    object History : NavigationScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.HISTORY
    }

    class ImageListViewer(
        val pickedIndex: Int,
    ) : NavigationScreen() {
        override val exitPageId: AiutaAnalyticsPageId = AiutaAnalyticsPageId.HISTORY

        override fun transitionSpec(): ContentTransform? {
            val durationMillis = 500
            return fadeIn(animationSpec = tween(durationMillis = durationMillis)) togetherWith
                fadeOut(animationSpec = tween(durationMillis = durationMillis))
        }
    }
}

internal fun defaultStartScreen(): NavigationScreen = NavigationScreen.Splash

// Stack
private val screenStacks =
    listOf(
        NavigationScreen.Splash,
        NavigationScreen.Preonboarding,
        NavigationScreen.Onboarding,
        NavigationScreen.ImageSelector,
        NavigationScreen.ModelSelector,
        NavigationScreen.GenerationResult,
        // Utils
        NavigationScreen.History,
    )

internal fun screenPosition(screen: NavigationScreen): Int = screenStacks.indexOf(screen)

internal fun AnimatedContentTransitionScope<NavigationScreen>.solveTransitionAnimation(): ContentTransform? {
    val initialTransition = initialState.transitionSpec()
    val targetState = targetState.transitionSpec()

    return initialTransition ?: targetState
}
