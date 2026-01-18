package com.aiuta.fashionsdk.internal.navigation.internal.utils

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

/**
 * Duration for navigation transitions in milliseconds.
 */
private const val TRANSITION_DURATION_MS = 400

/**
 * Left-to-right slide transition (forward navigation).
 *
 * New screen slides in from the right, old screen slides out to the left.
 */
internal val rightToLeftTransition: ContentTransform =
    slideInHorizontally(
        animationSpec = tween(durationMillis = TRANSITION_DURATION_MS),
        initialOffsetX = { fullWidth -> fullWidth },
    ) togetherWith
        slideOutHorizontally(
            animationSpec = tween(durationMillis = TRANSITION_DURATION_MS),
            targetOffsetX = { fullWidth -> -fullWidth },
        )

/**
 * Right-to-left slide transition (backward navigation).
 *
 * New screen slides in from the left, old screen slides out to the right.
 */
internal val leftToRightTransition: ContentTransform =
    slideInHorizontally(
        animationSpec = tween(durationMillis = TRANSITION_DURATION_MS),
        initialOffsetX = { fullWidth -> -fullWidth },
    ) togetherWith
        slideOutHorizontally(
            animationSpec = tween(durationMillis = TRANSITION_DURATION_MS),
            targetOffsetX = { fullWidth -> fullWidth },
        )
