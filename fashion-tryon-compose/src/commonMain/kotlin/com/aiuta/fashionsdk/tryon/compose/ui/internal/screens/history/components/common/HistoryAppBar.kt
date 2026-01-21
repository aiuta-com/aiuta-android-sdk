package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.components.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.clickableUnindicated
import com.aiuta.fashionsdk.configuration.features.tryon.history.AiutaTryOnGenerationsHistoryFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.activateSelectMode
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.isAppbarSelectAvailable
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components.appbar.AppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components.appbar.AppBarIcon
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature

@Composable
internal fun HistoryAppBar(modifier: Modifier = Modifier) {
    val controller = LocalController.current
    val navigationController = LocalAiutaNavigationController.current
    val theme = LocalTheme.current

    val generationsHistoryFeature = strictProvideFeature<AiutaTryOnGenerationsHistoryFeature>()

    val isAppbarSelectAvailable = controller.isAppbarSelectAvailable()

    val actionColorCalculation: (Boolean) -> Color = { active ->
        if (active) {
            theme.color.primary
        } else {
            theme.color.secondary
        }
    }

    val selectColorTransition =
        animateColorAsState(
            targetValue = actionColorCalculation(isAppbarSelectAvailable.value),
            label = "selectColorTransition",
        )

    AppBar(
        modifier = modifier,
        navigationIcon = {
            AppBarIcon(
                modifier = Modifier.align(Alignment.CenterStart),
                icon = theme.pageBar.icons.back24,
                color = theme.color.primary,
                onClick = navigationController::navigateBack,
            )
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                text = generationsHistoryFeature.strings.generationsHistoryPageTitle,
                style = theme.pageBar.typography.pageTitle,
                color = theme.color.primary,
                textAlign = TextAlign.Center,
            )
        },
        actions = {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickableUnindicated { if (isAppbarSelectAvailable.value) controller.activateSelectMode() },
                text = theme.selectionSnackbar.strings.select,
                style = theme.pageBar.typography.pageTitle,
                color = selectColorTransition.value,
            )
        },
    )
}
