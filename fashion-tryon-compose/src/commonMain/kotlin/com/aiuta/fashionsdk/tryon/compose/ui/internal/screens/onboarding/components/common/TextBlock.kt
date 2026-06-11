package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme

@Composable
internal fun ColumnScope.CentredTextBlock(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.weight(1f))

        Text(
            text = title,
            style = theme.label.typography.titleL,
            color = theme.color.primary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = subtitle,
            style = theme.label.typography.regular,
            color = theme.color.primary,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.weight(1f))
    }
}
