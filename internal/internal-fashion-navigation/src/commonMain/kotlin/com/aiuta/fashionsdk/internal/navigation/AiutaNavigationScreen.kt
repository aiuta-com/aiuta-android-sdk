package com.aiuta.fashionsdk.internal.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.runtime.Immutable
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
    @OptIn(ExperimentalUuidApi::class)
    public val id: String = Uuid.random().toString()

    public abstract val exitPageId: AiutaAnalyticsPageId

    /**
     * Defines custom animation transition for this screen.
     *
     * Return null to use default navigation transitions.
     *
     * @return Custom [ContentTransform] or null for default behavior
     */
    public open fun transitionSpec(): ContentTransform? = null

    /**
     * Whether this screen should be saved to backstack when navigating away from it.
     * Override to return false for screens like Splash, Onboarding, etc.
     */
    public open val shouldSaveInBackStack: Boolean = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AiutaNavigationScreen) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

internal fun AnimatedContentTransitionScope<AiutaNavigationScreen>.solveTransitionAnimation(): ContentTransform? {
    val initialTransition = initialState.transitionSpec()
    val targetState = targetState.transitionSpec()

    return initialTransition ?: targetState
}
