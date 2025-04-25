package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.appbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.aiuta.fashionsdk.configuration.features.features.selector.model.AiutaImageSelectorPredefinedModelFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateBack
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components.appbar.AppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components.appbar.AppBarIcon
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme

@Composable
internal fun ModelSelectorAppBar(modifier: Modifier = Modifier) {
    val controller = LocalController.current
    val theme = LocalTheme.current

    val predefinedModelFeature = strictProvideFeature<AiutaImageSelectorPredefinedModelFeature>()

    AppBar(
        modifier = modifier,
        navigationIcon = {
            AppBarIcon(
                modifier = Modifier.align(Alignment.CenterStart),
                icon = theme.pageBar.icons.back24,
                color = theme.color.primary,
                onClick = controller::navigateBack,
            )
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                text = predefinedModelFeature.strings.predefinedModelPageTitle,
                style = theme.pageBar.typography.pageTitle,
                color = theme.color.primary,
                textAlign = TextAlign.Center,
            )
        },
    )
}
