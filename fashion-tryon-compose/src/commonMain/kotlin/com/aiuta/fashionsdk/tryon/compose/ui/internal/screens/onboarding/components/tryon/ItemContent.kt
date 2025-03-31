package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.tryon

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.molecules.images.AiutaImage
import com.aiuta.fashionsdk.compose.tokens.composition.LocalTheme
import com.aiuta.fashionsdk.compose.tokens.images.AiutaImage
import com.aiuta.fashionsdk.compose.tokens.utils.clickableUnindicated
import com.aiuta.fashionsdk.tryon.compose.domain.models.configuration.features.onboarding.shapes.onboardingImageSShape
import com.aiuta.fashionsdk.tryon.compose.domain.models.configuration.features.onboarding.strictOnboardingFeature

@Composable
internal fun ItemContent(
    modifier: Modifier = Modifier,
    itemImage: AiutaImage,
    isActive: Boolean,
    onClick: () -> Unit,
) {
    val theme = LocalTheme.current
    val onboardingFeature = strictOnboardingFeature()

    val widthTransition =
        animateDpAsState(
            targetValue = if (isActive) 88.dp else 64.dp,
            label = "widthTransition",
        )

    val heightTransition =
        animateDpAsState(
            targetValue = if (isActive) 120.dp else 88.dp,
            label = "heightTransition",
        )

    val cornerRadius = onboardingFeature.shapes.onboardingImageSShape

    AiutaImage(
        modifier =
            modifier
                .width(widthTransition.value)
                .height(heightTransition.value)
                .shadow(
                    elevation = 10.dp,
                    shape = cornerRadius,
                    ambientColor = Color.Black,
                    spotColor = Color.Black,
                )
                .background(
                    color = theme.colors.background,
                    shape = cornerRadius,
                )
                .clickableUnindicated {
                    onClick()
                },
        image = itemImage,
        contentDescription = null,
    )
}
