package com.aiuta.fashionsdk.sizefit.compose.ui.internal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationInitialisation
import com.aiuta.fashionsdk.internal.navigation.aiutaEntryProvider
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.composition.LocalAiutaSizeFit
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFit

@Composable
internal fun SizeFitNavigationInitialisation(
    aiutaConfiguration: AiutaConfiguration,
    aiutaSizeFit: AiutaSizeFit,
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
        CompositionLocalProvider(
            LocalAiutaSizeFit provides aiutaSizeFit,
        ) {
            content()
        }
    }
}
