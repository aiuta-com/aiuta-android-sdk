package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.consent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent.ConsentContent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.OnboardingScreenEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.models.OnboardingScreenViewState

@Composable
internal fun ConsentPageContent(
    state: OnboardingScreenViewState,
    eventHandler: (OnboardingScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Spacer(Modifier.height(32.dp))

        ConsentContent(
            modifier = Modifier.fillMaxSize(),
            consentsList = state.consents,
            onUpdateConsentState = { consent, isObtained ->
                eventHandler(
                    OnboardingScreenEvent.ConsentToggled(
                        consent = consent,
                        isObtained = isObtained,
                    ),
                )
            },
        )
    }
}
