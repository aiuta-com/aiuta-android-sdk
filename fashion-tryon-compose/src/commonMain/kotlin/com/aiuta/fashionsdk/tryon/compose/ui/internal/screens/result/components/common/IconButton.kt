package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated

@Composable
internal fun IconButton(
    modifier: Modifier = Modifier,
    icon: AiutaIcon,
    onClick: () -> Unit,
) {
    val theme = LocalTheme.current

    Box(
        modifier =
        modifier
            .size(38.dp)
            .background(
                color = theme.color.background,
                shape = CircleShape,
            )
            .clickableUnindicated { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        AiutaIcon(
            modifier = Modifier.size(24.dp),
            icon = icon,
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}
