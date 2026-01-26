package com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.disclaimer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.disclaimer.AiutaTryOnFitDisclaimerFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaBottomSheetNavigator
import com.aiuta.fashionsdk.tryon.compose.ui.internal.sheets.components.SheetDivider

@Composable
internal fun FitDisclaimerSheet(
    modifier: Modifier = Modifier,
) {
    val bottomSheetNavigator = LocalAiutaBottomSheetNavigator.current
    val theme = LocalTheme.current

    val fitDisclaimerFeature = strictProvideFeature<AiutaTryOnFitDisclaimerFeature>()

    val sharedModifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)

    Column(modifier = modifier) {
        SheetDivider()

        Spacer(Modifier.height(30.dp))

        Text(
            modifier = sharedModifier,
            text = fitDisclaimerFeature.strings.tryOnFitDescription,
            style = theme.pageBar.typography.pageTitle,
            color = theme.color.primary,
            textAlign = TextAlign.Start,
        )

        Spacer(Modifier.height(32.dp))

        FashionButton(
            modifier = sharedModifier,
            text = fitDisclaimerFeature.strings.tryOnFitButtonClose,
            style = FashionButtonStyles.secondaryStyle(theme),
            size = FashionButtonSizes.lSize(),
            onClick = {
                bottomSheetNavigator.hide()
            },
        )
    }
}
