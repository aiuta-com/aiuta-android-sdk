package com.aiuta.fashionsdk.sizefit.compose.ui.internal.navigation

import androidx.compose.runtime.Composable
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationInitialisation
import com.aiuta.fashionsdk.internal.navigation.aiutaEntryProvider

@Composable
internal fun SizeFitNavigationInitialisation(
    aiutaConfiguration: AiutaConfiguration,
    content: @Composable () -> Unit,
) {
    // Base init
    AiutaNavigationInitialisation(
        startScreen = SizeFitScreen.Questionary,
        aiutaConfiguration = aiutaConfiguration,
        bottomSheetEntryProvider = aiutaEntryProvider {
            // No bottom sheet yet
        },
    ) {
        content()
    }
}
