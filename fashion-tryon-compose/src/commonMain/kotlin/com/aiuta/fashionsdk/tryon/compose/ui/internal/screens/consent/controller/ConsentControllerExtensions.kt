package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent.controller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticOnboardingEventType
import com.aiuta.fashionsdk.analytics.events.AiutaAnalyticsPageId
import com.aiuta.fashionsdk.tryon.compose.domain.internal.utils.isRequired
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.screen.onboarding.AiutaConsentUiModel
import com.aiuta.fashionsdk.tryon.compose.ui.internal.analytic.sendOnboardingEvent
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.FashionTryOnController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.dataprovider.safeInvoke

internal suspend fun FashionTryOnController.completeConsentViewing(
    consentController: ConsentController,
) {
    val obtainedConsentId = consentController.consentsList.mapNotNull { consentModel ->
        consentModel.consent.id.takeIf { consentModel.isObtained }
    }
    sendOnboardingEvent(
        eventType = AiutaAnalyticOnboardingEventType.CONSENT_GIVEN,
        pageId = AiutaAnalyticsPageId.CONSENT,
        consentsIds = obtainedConsentId,
    )

    consentInteractor::obtainConsent.safeInvoke(obtainedConsentId)
}

internal fun ConsentController.updateConsentState(
    consent: AiutaConsentUiModel,
    newState: Boolean,
) {
    val consentIndex = consentsList.indexOfLast { it.consent.id == consent.consent.id }
    if (consentIndex >= 0) {
        consentsList[consentIndex] = consent.copy(
            isObtained = newState,
        )
    }
}

@Composable
internal fun ConsentController.listenIsAllMandatoryConsentChecked(): State<Boolean> = remember(
    consentsList,
) {
    derivedStateOf { isAllMandatoryConsentChecked() }
}

internal fun ConsentController.isAllMandatoryConsentChecked(): Boolean = consentsList.all { consentModel ->
    if (consentModel.consent.isRequired()) {
        consentModel.isObtained
    } else {
        true
    }
}
