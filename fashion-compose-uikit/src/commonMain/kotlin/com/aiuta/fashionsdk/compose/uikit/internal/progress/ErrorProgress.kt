package com.aiuta.fashionsdk.compose.uikit.internal.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon

@Composable
internal fun ErrorProgress(
    modifier: Modifier = Modifier,
    background: Color = LocalTheme.current.color.neutral,
    iconTint: Color = LocalTheme.current.color.primary,
) {
    val theme = LocalTheme.current

    Box(
        modifier = modifier.clipToBounds().background(background),
        contentAlignment = Alignment.Center,
    ) {
        AiutaIcon(
            modifier = Modifier.size(36.dp),
            icon = theme.image.icons.error36,
            contentDescription = null,
            tint = iconTint,
        )
    }
}
