package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.button.FashionButton
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonSizes
import com.aiuta.fashionsdk.compose.uikit.button.FashionButtonStyles
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.configuration.features.consent.AiutaConsentStandaloneFeature
import com.aiuta.fashionsdk.internal.navigation.composition.LocalAiutaNavigationController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent.component.common.ConsentAppBar
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent.controller.completeConsentViewing
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent.controller.listenIsAllMandatoryConsentChecked
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent.controller.rememberConsentController
import com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.consent.controller.updateConsentState
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature
import kotlinx.coroutines.launch

@Composable
internal fun ConsentScreen(
    modifier: Modifier = Modifier,
    onObtainedConsents: () -> Unit,
) {
    val theme = LocalTheme.current
    val controller = LocalController.current
    val navigationController = LocalAiutaNavigationController.current
    val scope = rememberCoroutineScope()

    val consentStandaloneFeature = strictProvideFeature<AiutaConsentStandaloneFeature>()
    val consentController = rememberConsentController(consentStandaloneFeature)

    val generalHorizontalPadding = 16.dp

    Column(
        modifier = modifier
            .background(theme.color.background)
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ConsentAppBar(
            modifier = Modifier
                .padding(horizontal = generalHorizontalPadding)
                .fillMaxWidth(),
        )

        Spacer(Modifier.height(32.dp))

        ConsentContent(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            consentsList = consentController.consentsList,
            onUpdateConsentState = consentController::updateConsentState,
        )

        FashionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = generalHorizontalPadding),
            text = consentStandaloneFeature.strings.consentButtonAccept,
            style = FashionButtonStyles.primaryStyle(theme),
            size = FashionButtonSizes.lSize(),
            isEnable = consentController.listenIsAllMandatoryConsentChecked().value,
            onClick = {
                scope.launch {
                    consentController.completeConsentViewing(controller.consentInteractor)
                    navigationController.navigateBack().apply { onObtainedConsents() }
                }
            },
        )

        Spacer(Modifier.height(12.dp))
    }
}
