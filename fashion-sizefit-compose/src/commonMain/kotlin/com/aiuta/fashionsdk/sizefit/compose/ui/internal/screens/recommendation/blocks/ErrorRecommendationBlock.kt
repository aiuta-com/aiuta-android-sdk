package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.recommendation.blocks

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.sizefit.AiutaSizeFitFeature

@Composable
internal fun ColumnScope.ErrorRecommendationBlock(
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    val sizeFitFeature = strictProvideFeature<AiutaSizeFitFeature>()

    Spacer(Modifier.weight(0.3f))

    AiutaIcon(
        modifier = Modifier.size(60.dp).align(Alignment.CenterHorizontally),
        icon = theme.errorSnackbar.icons.error36,
        contentDescription = null,
        tint = theme.color.primary,
    )

    Spacer(Modifier.height(24.dp))

    Text(
        text = sizeFitFeature.strings.errorMessage,
        style = theme.label.typography.regular,
        color = theme.color.primary,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 40.dp),
    )

    Spacer(Modifier.weight(0.7f))
}
