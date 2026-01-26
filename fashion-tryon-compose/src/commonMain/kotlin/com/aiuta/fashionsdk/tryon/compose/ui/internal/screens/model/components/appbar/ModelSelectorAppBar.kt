package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.components.appbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBar
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBarIcon
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.picker.model.AiutaImagePickerPredefinedModelFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController

@Composable
internal fun ModelSelectorAppBar(modifier: Modifier = Modifier) {
    val navigationController = LocalAiutaNavigationController.current
    val theme = LocalTheme.current

    val predefinedModelFeature = strictProvideFeature<AiutaImagePickerPredefinedModelFeature>()

    AiutaAppBar(
        modifier = modifier,
        navigationIcon = {
            AiutaAppBarIcon(
                modifier = Modifier.align(Alignment.CenterStart),
                icon = theme.pageBar.icons.back24,
                color = theme.color.primary,
                onClick = navigationController::navigateBack,
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
