package com.aiuta.fashionsdk.sizefit.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.navigation.SizeFitNavigationFlow
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFit

@Composable
public fun AiutaSizeFitFlow(
    aiutaConfiguration: AiutaConfiguration,
    aiutaSizeFit: AiutaSizeFit,
    modifier: Modifier = Modifier,
) {
    SizeFitNavigationFlow(
        aiutaConfiguration = aiutaConfiguration,
        aiutaSizeFit = aiutaSizeFit,
        modifier = modifier,
    )
}
