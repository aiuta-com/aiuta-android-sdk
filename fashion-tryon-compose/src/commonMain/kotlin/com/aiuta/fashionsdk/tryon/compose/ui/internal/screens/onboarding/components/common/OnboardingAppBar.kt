package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.common

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.style.TextAlign
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.clickClose
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components.appbar.AppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.navigation.components.appbar.AppBarIcon
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.OnboardingController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.previousPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.BestResultPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.ConsentPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.TryOnPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.buildAnnotatedStringFromHtml
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.transitionAnimation
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme

@Composable
internal fun OnboardingAppBar(
    modifier: Modifier = Modifier,
    onboardingController: OnboardingController,
) {
    val controller = LocalController.current
    val theme = LocalTheme.current

    val titleTransition = updateTransition(onboardingController.state.value)

    AppBar(
        modifier = modifier,
        navigationIcon = {
            AppBarIcon(
                modifier = Modifier.align(Alignment.CenterStart),
                icon = theme.pageBar.icons.back24,
                color = theme.color.primary,
                onClick = {
                    onboardingController.previousPage(controller)
                },
            )
        },
        title = {
            titleTransition.AnimatedContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                transitionSpec = { transitionAnimation },
            ) { state ->
                state.pageTitle?.let { pageTitle ->
                    Text(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        text =
                        buildAnnotatedStringFromHtml(
                            input = pageTitle,
                            isClickable = false,
                        ),
                        style =
                        theme.pageBar.typography.pageTitle.copy(
                            fontSynthesis = FontSynthesis.All,
                        ),
                        color = theme.color.primary,
                        textAlign = TextAlign.Center,
                    )
                }
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
                            pageId =
                            when (onboardingController.state.value) {
                                is TryOnPage -> AiutaAnalyticsPageId.HOW_IT_WORKS
                                is BestResultPage -> AiutaAnalyticsPageId.BEST_RESULTS
                                is ConsentPage -> AiutaAnalyticsPageId.CONSENT
                            },
                        )
                    },
                )
            }
        },
    )
}
