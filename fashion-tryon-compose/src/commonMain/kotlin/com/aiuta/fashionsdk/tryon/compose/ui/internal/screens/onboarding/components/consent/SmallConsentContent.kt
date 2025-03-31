package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.consent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.tokens.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.domain.models.configuration.features.consent.consentBuiltInFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.OnboardingController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.controller.state.TryOnPage
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.buildAnnotatedStringFromHtml

@Composable
internal fun SmallConsentContent(
    modifier: Modifier = Modifier,
    onboardingController: OnboardingController,
) {
    val theme = LocalTheme.current

    val consentBuiltInFeature = consentBuiltInFeature()

    val isVisible =
        remember(
            onboardingController.state.value,
        ) {
            derivedStateOf {
                onboardingController.state.value is TryOnPage && consentBuiltInFeature != null
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
                    text = buildAnnotatedStringFromHtml(consentBuiltInFeature.strings.consentHtml),
                    style = theme.typography.productName,
                    color = theme.colors.secondary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
