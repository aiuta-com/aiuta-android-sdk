package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.base.share

import androidx.compose.runtime.Composable
import com.aiuta.fashionsdk.compose.uikit.utils.provideFeature
import com.aiuta.fashionsdk.configuration.features.share.AiutaShareFeature

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
