package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent.component.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.style.TextAlign
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickClose
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components.appbar.AppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components.appbar.AppBarIcon
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.buildAnnotatedStringFromHtml
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme

@Composable
internal fun ConsentAppBar(
    modifier: Modifier = Modifier,
) {
    val controller = LocalController.current
    val navigationController = LocalAiutaNavigationController.current
    val theme = LocalTheme.current

    val consentStandaloneFeature = strictProvideFeature<AiutaConsentStandaloneFeature>()

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
            consentStandaloneFeature.strings.consentPageTitle?.let { title ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    text = buildAnnotatedStringFromHtml(
                        input = title,
                        isClickable = false,
                    ),
                    style = theme.pageBar.typography.pageTitle.copy(
                        fontSynthesis = FontSynthesis.All,
                    ),
                    color = theme.color.primary,
                    textAlign = TextAlign.Center,
                )
            }
        },
        actions = {
            if (theme.pageBar.toggles.preferCloseButtonOnTheRight) {
                AppBarIcon(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    icon = theme.pageBar.icons.close24,
                    color = theme.color.primary,
                    onClick = {
                        controller.clickClose(
                            navigationController = navigationController,
                            pageId = AiutaAnalyticsPageId.CONSENT,
                        )
                    },
                )
            }
        },
    )
}
