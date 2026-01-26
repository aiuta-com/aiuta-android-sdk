package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.result.components.footer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.disclaimer.AiutaTryOnFitDisclaimerFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnBottomSheetScreen

@Composable
internal fun DisclaimerBlock(modifier: Modifier = Modifier) {
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
    val fitDisclaimerFeature = provideFeature<AiutaTryOnFitDisclaimerFeature>()

    fitDisclaimerFeature?.let {
        DisclaimerBlockContent(
            modifier = modifier.clickableUnindicated {
                bottomSheetNavigator.show(
                    newSheetScreen = TryOnBottomSheetScreen.FitDisclaimer,
                )
            },
            fitDisclaimerFeature = fitDisclaimerFeature,
        )
    }
}

@Composable
private fun DisclaimerBlockContent(
    modifier: Modifier = Modifier,
    fitDisclaimerFeature: AiutaTryOnFitDisclaimerFeature,
) {
    val theme = LocalTheme.current

    Row(
        modifier = modifier.background(
            color = theme.color.neutral,
            shape = theme.bottomSheet.shapes.bottomSheetShape,
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = fitDisclaimerFeature.strings.tryOnFitTitle,
            style = fitDisclaimerFeature.typography.disclaimer,
            color = theme.color.primary,
            textAlign = TextAlign.Center,
        )

        fitDisclaimerFeature.icons.info20?.let { info20 ->
            Spacer(Modifier.width(4.dp))

            AiutaIcon(
                modifier = Modifier.size(20.dp),
                icon = info20,
                contentDescription = null,
                tint = theme.color.primary,
            )
        }
    }
}
