package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.body

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.configuration.features.tryon.loading.AiutaTryOnLoadingPageFeature
import com.aiuta.fashionsdk.tryon.compose.ui.internal.utils.features.strictProvideFeature

@Composable
internal fun ShimmerBlock(
    modifier: Modifier = Modifier,
    durationMillis: Int = 3000,
    lineHeight: Dp = 4.dp,
) {
    val theme = LocalTheme.current
    val density = LocalDensity.current

    val loadingPageFeature = strictProvideFeature<AiutaTryOnLoadingPageFeature>()

    val transition = rememberInfiniteTransition()
    val translateAnimation =
        transition.animateFloat(
            initialValue = 1f,
            targetValue = -1f,
            animationSpec =
            infiniteRepeatable(
                animation =
                tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "Shimmer loading animation",
        )

    val loadingAnimationGradient = remember {
        loadingPageFeature.styles.loadingStatusBackgroundGradient ?: listOf(
            theme.color.brand,
            Color.Transparent,
        )
    }

    Canvas(modifier = modifier.alpha(0.5f)) {
        // Main line

        loadingAnimationGradient.firstOrNull()?.let { color ->
            drawRect(
                color = color,
                topLeft = Offset(0f, size.height * translateAnimation.value),
                size = Size(
                    width = size.width,
                    height = with(density) { lineHeight.toPx() },
                ),
            )
        }

        // Down gradient
        val rectHeight = (size.height * (1 - translateAnimation.value)).coerceAtMost(
            maximumValue = size.height * 0.4f,
        )
        drawRect(
            brush =
            Brush.verticalGradient(
                loadingAnimationGradient,
                startY = size.height * translateAnimation.value,
                endY = size.height * translateAnimation.value + rectHeight,
            ),
            topLeft = Offset(0f, size.height * translateAnimation.value),
            size =
            Size(
                width = size.width,
                height = rectHeight,
            ),
        )
    }
}
