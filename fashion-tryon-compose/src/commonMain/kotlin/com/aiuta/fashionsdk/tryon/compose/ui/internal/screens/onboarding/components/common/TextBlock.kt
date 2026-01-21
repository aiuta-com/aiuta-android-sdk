package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme

@Composable
internal fun CentredTextBlock(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(Modifier.weight(1f))

        TextBlock(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            title = title,
            subtitle = subtitle,
        )

        Spacer(Modifier.weight(1f))
    }
}

@Composable
internal fun ColumnScope.TextBlock(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
) {
    val theme = LocalTheme.current

    Text(
        modifier = modifier,
        text = title,
        style = theme.label.typography.titleL,
        color = theme.color.primary,
        textAlign = TextAlign.Start,
    )

    Spacer(Modifier.height(18.dp))

    Text(
        modifier = modifier,
        text = subtitle,
        style = theme.label.typography.regular,
        color = theme.color.primary,
        textAlign = TextAlign.Start,
    )
}
