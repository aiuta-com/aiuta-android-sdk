package com.aiuta.fashionsdk.compose.uikit.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier

private val sharedInteractionSource = MutableInteractionSource()

/**
 * Applies a modifier conditionally based on a boolean value.
 *
 * @param condition The condition that determines whether to apply the modifier
 * @param modifier The modifier to apply if the condition is true
 * @return The modified Modifier
 */
public fun Modifier.conditional(
    condition: Boolean,
    modifier: Modifier.() -> Modifier,
): Modifier = if (condition) {
    then(modifier(Modifier))
} else {
    this
}

/**
 * Creates a clickable modifier without a visual indication (ripple effect).
 *
 * @param enabled Whether the clickable area is enabled
 * @param onClick The callback to be invoked when the area is clicked
 * @return The modified Modifier
 */
public fun Modifier.clickableUnindicated(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = this then
    Modifier.clickable(
        interactionSource = sharedInteractionSource,
        indication = null,
        enabled = enabled,
        onClickLabel = null,
        role = null,
        onClick,
    )
