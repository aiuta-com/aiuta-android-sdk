package com.aiuta.fashionsdk.internal.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import kotlin.random.Random

/**
 * Base navigation screen for Fashion SDK flows.
 *
 * Each screen has a unique ID for navigation tracking and can define custom transitions.
 * Implementers should define their specific screens as sealed class hierarchies.
 *
 * @property id Unique identifier for this screen instance (auto-generated)
 * @property exitPageId Analytics page ID emitted when exiting this screen
 */
@Immutable
public abstract class AiutaNavigationScreen : AiutaNavKey {
    public val id: String = Random.nextInt().toString()

    public abstract val exitPageId: AiutaAnalyticsPageId

    /**
     * Defines custom animation transition for this screen.
     *
     * Return null to use default navigation transitions.
     *
     * @return Custom [ContentTransform] or null for default behavior
     */
    public open fun transitionSpec(): ContentTransform? = null
}

internal fun AnimatedContentTransitionScope<AiutaNavigationScreen>.solveTransitionAnimation(): ContentTransform? {
    val initialTransition = initialState.transitionSpec()
    val targetState = targetState.transitionSpec()

    return initialTransition ?: targetState
}
