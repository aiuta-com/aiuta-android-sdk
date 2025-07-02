package com.aiuta.fashionsdk.tryon.compose.ui.internal.components.icons

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.tryon.compose.uikit.resources.AiutaIcon

@Composable
internal fun AiutaBoxedLoadingIcon(
    modifier: Modifier = Modifier,
    circleColor: Color = LocalTheme.current.color.brand,
) {
    val theme = LocalTheme.current

    Box(
        modifier = modifier.background(theme.activityIndicator.colors.overlay),
        contentAlignment = Alignment.Center,
    ) {
        AiutaLoadingIcon(circleColor = circleColor)
    }
}

@Composable
internal fun AiutaLoadingIcon(
    modifier: Modifier = Modifier,
    circleColor: Color = LocalTheme.current.color.brand,
) {
    val theme = LocalTheme.current

    theme.activityIndicator.icons.loading14?.let { loading14 ->
        val infiniteTransition = rememberInfiniteTransition()
        val angle = infiniteTransition.animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec =
            infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing),
            ),
        )

        AiutaIcon(
            modifier = modifier.rotate(angle.value),
            icon = loading14,
            tint = circleColor,
            contentDescription = null,
        )
    } ?: CircularProgressIndicator(
        modifier = modifier,
        color = circleColor,
        strokeWidth = 1.dp,
    )
}
