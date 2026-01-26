package com.aiuta.fashionsdk.compose.uikit.internal.utils

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.fade
import com.eygraber.compose.placeholder.placeholder

@Composable
internal fun Modifier.placeholderFadeConnecting(
    shape: CornerBasedShape = RoundedCornerShape(4.dp),
    visible: Boolean = true,
): Modifier {
    val theme = LocalTheme.current
    val placeholderModifier = Modifier.placeholder(
        visible = visible,
        shape = shape,
        color = theme.color.neutral.copy(alpha = 0.2f),
        highlight = PlaceholderHighlight.fade(
            highlightColor = theme.color.neutral,
        ),
    )

    return this then placeholderModifier
}
