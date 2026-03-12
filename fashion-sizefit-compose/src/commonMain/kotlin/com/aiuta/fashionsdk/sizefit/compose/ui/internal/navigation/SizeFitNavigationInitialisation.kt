package com.aiuta.fashionsdk.sizefit.compose.ui.internal.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.internal.navigation.AiutaNavigationInitialisation
import com.aiuta.fashionsdk.internal.navigation.aiutaEntryProvider
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.composition.LocalAiutaSizeFit
import com.aiuta.fashionsdk.sizefit.core.sizeFit

@Composable
internal fun SizeFitNavigationInitialisation(
    aiutaConfiguration: AiutaConfiguration,
    content: @Composable () -> Unit,
) {
    val aiutaSizeFit = remember {
        aiutaConfiguration.aiuta.sizeFit
    }

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
