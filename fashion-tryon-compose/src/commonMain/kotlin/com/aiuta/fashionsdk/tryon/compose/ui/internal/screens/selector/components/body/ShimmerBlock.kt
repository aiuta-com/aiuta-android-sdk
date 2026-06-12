package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.selector.components.body

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme
import com.aiuta.fashionsdk.compose.uikit.utils.strictProvideFeature
import com.aiuta.fashionsdk.configuration.features.tryon.loading.AiutaTryOnLoadingPageFeature
import kotlin.math.sqrt

@Composable
internal fun ShimmerBlock(
    modifier: Modifier = Modifier,
    durationMillis: Int = 3000,
    // Width of the moving highlight band, as a fraction of the diagonal length.
    bandWidthFraction: Float = 1.0f,
) {
    val theme = LocalTheme.current

    val loadingPageFeature = strictProvideFeature<AiutaTryOnLoadingPageFeature>()

    // Colors of the highlight, transparent on both outer edges so it reads as a
    // band sweeping across instead of filling the whole surface.
    val shimmerColors = remember {
        loadingPageFeature.styles.loadingStatusBackgroundGradient ?: listOf(
            Color.White.copy(alpha = 0.5f),
            Color.Transparent,
            Color.White.copy(alpha = 0.5f),
        )
    }

    val transition = rememberInfiniteTransition(label = "Shimmer loading transition")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )

    Canvas(modifier = modifier.alpha(0.5f)) {
        val diagonal = sqrt(size.width * size.width + size.height * size.height)
        if (diagonal == 0f) return@Canvas

        // Unit vector pointing from the top-left corner to the bottom-right corner.
        val direction = Offset(size.width / diagonal, size.height / diagonal)

        val bandWidth = diagonal * bandWidthFraction
        // The band center travels from just before the top-left corner to just past
        // the bottom-right corner, so the highlight fully enters and exits the surface.
        val travel = diagonal + bandWidth
        val center = -bandWidth / 2f + travel * progress

        drawRect(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = direction * (center - bandWidth / 2f),
                end = direction * (center + bandWidth / 2f),
            ),
            size = size,
        )
    }
}
