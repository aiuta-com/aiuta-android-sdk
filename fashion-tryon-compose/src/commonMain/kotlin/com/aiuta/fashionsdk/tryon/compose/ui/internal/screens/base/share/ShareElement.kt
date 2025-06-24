package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share

import androidx.compose.runtime.Composable
import com.aiuta.fashionsdk.configuration.features.share.AiutaShareFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.provideFeature

@Composable
internal inline fun ShareElement(
    content: @Composable ShareElementScope.() -> Unit,
) {
    val shareFeature = provideFeature<AiutaShareFeature>()

    shareFeature?.let {
        val shareScope = rememberShareElementScope(
            shareFeature = shareFeature,
        )

        shareScope.content()
    }
}
