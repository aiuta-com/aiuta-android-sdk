package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.best

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.resources.drawable.AiutaDrawableResource
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaIcon
import com.aiuta.fashionsdk.compose.uikit.resources.AiutaImage
import com.aiuta.fashionsdk.configuration.features.onboarding.AiutaOnboardingFeature
import com.aiuta.fashionsdk.configuration.features.onboarding.bestresult.AiutaOnboardingBestResultsPageFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature

@Composable
internal fun BestResultPageItem(
    modifier: Modifier = Modifier,
    image: AiutaDrawableResource,
    isGoodImage: Boolean,
) {
    val onboardingFeature = strictProvideFeature<AiutaOnboardingFeature>()
    val bestResultFeature = strictProvideFeature<AiutaOnboardingBestResultsPageFeature>()

    Box(
        modifier = modifier.clip(onboardingFeature.shapes.onboardingImageSShape),
    ) {
        AiutaImage(
            modifier = modifier.fillMaxSize(),
            image = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        AiutaIcon(
            modifier =
            Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
                .size(24.dp),
            icon =
            if (isGoodImage) {
                bestResultFeature.icons.onboardingBestResultsGood24
            } else {
                bestResultFeature.icons.onboardingBestResultsBad24
            },
            contentDescription = null,
            tint = Color.Unspecified,
        )
    }
}
