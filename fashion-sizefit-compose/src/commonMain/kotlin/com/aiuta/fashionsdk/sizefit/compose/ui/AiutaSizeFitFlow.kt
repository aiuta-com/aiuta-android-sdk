package com.aiuta.fashionsdk.sizefit.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aiuta.fashionsdk.configuration.AiutaConfiguration
import com.aiuta.fashionsdk.sizefit.compose.ui.internal.navigation.SizeFitNavigationFlow

@Composable
public fun AiutaSizeFitFlow(
    aiutaConfiguration: AiutaConfiguration,
    productCode: String,
    modifier: Modifier = Modifier,
) {
    SizeFitNavigationFlow(
        aiutaConfiguration = aiutaConfiguration,
        productCode = productCode,
        modifier = modifier,
    )
}
