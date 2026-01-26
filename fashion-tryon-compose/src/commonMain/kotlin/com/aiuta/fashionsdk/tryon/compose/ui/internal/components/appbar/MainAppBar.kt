package com.aiuta.fashionsdk.tryon.compose.ui.internal.components.appbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBar
import com.aiuta.fashionsdk.compose.uikit.appbar.AiutaAppBarIcon
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.history.AiutaTryOnGenerationsHistoryFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickClose
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.isAppbarHistoryAvailable
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.TryOnScreen

@Composable
internal fun MainAppBar(
    modifier: Modifier = Modifier,
    title: String,
) {
    val controller = LocalController.current
    val navigationController = LocalAiutaNavigationController.current
    val theme = LocalTheme.current

    val isAppbarHistoryAvailable = controller.isAppbarHistoryAvailable()

    AiutaAppBar(
        modifier = modifier,
        navigationIcon = { navigationIconModifier ->
            AnimatedVisibility(
                modifier = navigationIconModifier,
                visible = isAppbarHistoryAvailable.value,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                val generationsHistoryFeature = strictProvideFeature<AiutaTryOnGenerationsHistoryFeature>()

                AiutaAppBarIcon(
                    icon = generationsHistoryFeature.icons.history24,
                    color = theme.color.primary,
                    onClick = {
                        navigationController.navigateTo(TryOnScreen.History)
                    },
                )
            }
        },
        title = { titleModifier ->
            Text(
                modifier = titleModifier.fillMaxWidth(),
                text = title,
                style = theme.pageBar.typography.pageTitle,
                color = theme.color.primary,
                textAlign = TextAlign.Center,
            )
        },
        closeButton = { actionModifier ->
            AiutaAppBarIcon(
                modifier = actionModifier,
                icon = theme.pageBar.icons.close24,
                color = theme.color.primary,
                onClick = {
                    controller.clickClose(
                        navigationController = navigationController,
                    )
                },
            )
        },
    )
}
