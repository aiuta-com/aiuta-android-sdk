package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.consent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.buildAnnotatedStringFromHtml
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentEmbeddedIntoOnboardingFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaLogger
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.OnboardingScreenViewState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.TryOnPage

@Composable
internal fun SmallConsentContent(
    viewState: State<OnboardingScreenViewState>,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current
    val logger = LocalAiutaLogger.current

    val consentBuiltInFeature = provideFeature<AiutaConsentEmbeddedIntoOnboardingFeature>()

    val isVisible =
        remember(
            viewState.value.currentStep,
        ) {
            derivedStateOf {
                viewState.value.currentStep is TryOnPage && consentBuiltInFeature != null
            }
        }

    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible.value,
    ) {
        consentBuiltInFeature?.let {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
            ) {
                Spacer(Modifier.height(24.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = buildAnnotatedStringFromHtml(
                        input = consentBuiltInFeature.strings.consentHtml,
                        logger = logger,
                    ),
                    style = theme.productBar.typography.product,
                    color = theme.color.secondary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
