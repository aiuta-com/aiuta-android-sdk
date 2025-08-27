package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme

@Composable
internal fun AiutaLabel(modifier: Modifier = Modifier) {
    val theme = LocalTheme.current

    Box(
        modifier = modifier
            .background(
                shape = RoundedCornerShape(100.dp),
                color = theme.color.neutral,
            )
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = theme.powerBar.strings.poweredByAiuta,
            style = theme.button.typography.buttonS,
            color = theme.color.primary,
        )
    }
}
