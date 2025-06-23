package com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation

import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import kotlin.random.Random

/**
 * Be careful, order is matter for animation transitions,
 */
internal abstract class NavigationScreen {
    val id: String = Random.nextInt().toString()
    abstract val exitPageId: AiutaAnalyticsPageId

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
        NavigationScreen.History,
    )

internal fun screenPosition(screen: NavigationScreen): Int = screenStacks.indexOf(screen)
