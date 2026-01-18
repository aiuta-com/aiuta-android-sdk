package com.aiuta.fashionsdk.tryon.compose.ui.internal.components.appbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.aiuta.fashionsdk.configuration.features.tryon.history.AiutaTryOnGenerationsHistoryFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickClose
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.isAppbarHistoryAvailable
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.navigateTo
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components.appbar.AppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components.appbar.AppBarIcon
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme

@Composable
internal fun MainAppBar(
    modifier: Modifier = Modifier,
    title: String,
) {
    val controller = LocalController.current
    val navigationController = LocalAiutaNavigationController.current
    val theme = LocalTheme.current

    val isAppbarHistoryAvailable = controller.isAppbarHistoryAvailable()

    val historyButton: @Composable BoxScope.(modifier: Modifier) -> Unit = { innerModifier ->
        AnimatedVisibility(
            modifier = innerModifier,
            visible = isAppbarHistoryAvailable.value,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            val generationsHistoryFeature = strictProvideFeature<AiutaTryOnGenerationsHistoryFeature>()

            AppBarIcon(
                icon = generationsHistoryFeature.icons.history24,
                color = theme.color.primary,
                onClick = {
                    navigationController.navigateTo(TryOnScreen.History)
                },
            )
        }
    }

    val closeButton: @Composable BoxScope.(modifier: Modifier) -> Unit = { innerModifier ->
        AppBarIcon(
            modifier = innerModifier,
            icon = theme.pageBar.icons.close24,
            color = theme.color.primary,
            onClick = controller::clickClose,
        )
    }

    AppBar(
        modifier = modifier,
        navigationIcon = {
            val navigationIconModifier = Modifier.align(Alignment.CenterStart)

            if (theme.pageBar.toggles.preferCloseButtonOnTheRight) {
                historyButton(navigationIconModifier)
            } else {
                closeButton(navigationIconModifier)
            }
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                text = title,
                style = theme.pageBar.typography.pageTitle,
                color = theme.color.primary,
                textAlign = TextAlign.Center,
            )
        },
        actions = {
            val actionModifier = Modifier.align(Alignment.CenterEnd)

            if (theme.pageBar.toggles.preferCloseButtonOnTheRight) {
                closeButton(actionModifier)
            } else {
                historyButton(actionModifier)
            }
        },
    )
}
