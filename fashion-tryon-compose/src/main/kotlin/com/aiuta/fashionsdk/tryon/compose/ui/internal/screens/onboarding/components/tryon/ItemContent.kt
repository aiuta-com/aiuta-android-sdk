package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.onboarding.components.tryon

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.tokens.utils.clickableUnindicated
import com.aiuta.fashionsdk.tryon.compose.ui.internal.controller.composition.LocalTheme

@Composable
internal fun ItemContent(
    modifier: Modifier = Modifier,
    itemImage: Int,
    isActive: Boolean,
    onClick: () -> Unit,
) {
    val theme = LocalTheme.current

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

    val cornerRadiusTransition =
        animateDpAsState(
            targetValue = if (isActive) 12.dp else 10.dp,
            label = "cornerRadiusTransition",
        )

    Image(
        modifier =
            modifier
                .width(widthTransition.value)
                .height(heightTransition.value)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(cornerRadiusTransition.value),
                    ambientColor = Color.Black,
                    spotColor = Color.Black,
                )
                .background(
                    color = theme.colors.background,
                    shape = RoundedCornerShape(cornerRadiusTransition.value),
                )
                .clickableUnindicated {
                    onClick()
                },
        painter = painterResource(itemImage),
        contentDescription = null,
    )
}
