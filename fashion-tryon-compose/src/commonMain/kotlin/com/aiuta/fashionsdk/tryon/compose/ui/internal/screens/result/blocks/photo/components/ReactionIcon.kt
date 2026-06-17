package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.blocks.photo.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.tryon.compose.ui.internal.components.icons.AiutaLoadingComponent
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

/**
 * The shared circular, blurred icon button used by every overlay control on the result photo card
 * (share, wishlist, change photo, like, dislike). Renders an [AiutaLoadingComponent] so callers that
 * need a busy state (sharing) reuse the same look.
 */
@Composable
internal fun ReactionIcon(
    icon: AiutaIcon,
    hazeState: HazeState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    val haptic = LocalHapticFeedback.current
    val theme = LocalTheme.current

    Box(
        modifier = modifier
            .size(38.dp)
            .clip(CircleShape)
            .hazeEffect(hazeState) {
                val sharedColor = theme.color.onLight.copy(alpha = 0.4f)

                blurRadius = 12.dp
                backgroundColor = sharedColor
                tints = listOf(HazeTint(sharedColor))
                fallbackTint = HazeTint(sharedColor)
            },
        contentAlignment = Alignment.Center,
    ) {
        AiutaLoadingComponent(
            isLoading = isLoading,
            circleColor = theme.color.onDark,
            circleSize = 20.dp,
        ) {
            AiutaIcon(
                modifier = Modifier
                    .size(20.dp)
                    .clickableUnindicated(enabled = !isLoading) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onClick()
                    },
                icon = icon,
                contentDescription = null,
                tint = theme.color.onDark,
            )
        }
    }
}
