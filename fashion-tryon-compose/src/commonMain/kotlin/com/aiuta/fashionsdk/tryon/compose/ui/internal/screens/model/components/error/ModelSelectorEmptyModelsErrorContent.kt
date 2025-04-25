package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.core.size.rememberScreenSize
import com.aiuta.fashionsdk.configuration.features.features.selector.model.AiutaImageSelectorPredefinedModelFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaIcon

@Composable
internal fun ModelSelectorEmptyModelsErrorContent(modifier: Modifier) {
    val theme = LocalTheme.current

    val predefinedModelFeature = strictProvideFeature<AiutaImageSelectorPredefinedModelFeature>()

    val screenSize = rememberScreenSize()
    val textHorizontalPadding = screenSize.widthDp * 0.26f

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AiutaIcon(
            modifier = Modifier.size(36.dp),
            icon = theme.errorSnackbar.icons.error36,
            tint = theme.color.primary,
            contentDescription = null,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = textHorizontalPadding),
            text = predefinedModelFeature.strings.predefinedModelErrorEmptyModelsList,
            style = theme.label.typography.regular,
            color = theme.color.primary,
            textAlign = TextAlign.Center,
        )
    }
}
