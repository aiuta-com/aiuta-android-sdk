package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.recommendation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme

@Composable
internal fun BottomRecommendationBlock(
    sizeName: String,
    percentage: Int,
    progressColor: Brush,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val theme = LocalTheme.current

    val trackHeight = 8.dp
    val trackHeightPx = with(density) { trackHeight.toPx() }

    val markerRadius = trackHeight / 2 + 2.dp
    val markerRadiusPx = with(density) { markerRadius.toPx() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(trackHeight + 12.dp),
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val y = size.height / 2f

            val normalizedPercentage = percentage / 100f
            val widthPx = with(density) { maxWidth.toPx() }
            val markerCenterX = (normalizedPercentage * widthPx).coerceIn(markerRadiusPx, widthPx - markerRadiusPx)

            // Трек (толстая линия с округлыми краями)
            drawLine(
                brush = progressColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = trackHeightPx,
                cap = StrokeCap.Round,
            )

            // Маркер (белый круг с обводкой)
            drawCircle(
                color = theme.color.background,
                radius = markerRadiusPx,
                center = Offset(markerCenterX, y),
            )

            // Внутренний круг (обводка/акцент)
            drawCircle(
                brush = progressColor,
                radius = markerRadiusPx * 0.55f,
                center = Offset(markerCenterX, y),
            )
        }
    }
}
