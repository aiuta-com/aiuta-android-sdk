package com.aiuta.fashionsdk.sizefit.compose.ui.internal.screens.recommendation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.uikit.composition.LocalTheme

internal val BUBBLE_WIDTH = 50.dp

@Composable
internal fun TopRecommendationBlock(
    sizeName: String,
    percentage: Int,
    progressColor: Brush,
    percentageTextColor: Color,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    BoxWithConstraints(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.align(Alignment.TopStart),
            text = sizeName,
            style = theme.label.typography.titleM,
            color = theme.color.primary,
            textAlign = TextAlign.Start,
        )

        BubblePercent(
            progressText = "$percentage%",
            progressColor = progressColor,
            percentageTextColor = percentageTextColor,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset {
                    val normalizedPercentage = percentage / 100f
                    val dotOffset = maxWidth.toPx() * normalizedPercentage
                    val bubbleWidthPx = BUBBLE_WIDTH.toPx()

                    val bubbleOffsetPx = dotOffset - bubbleWidthPx / 2
                    IntOffset(bubbleOffsetPx.toInt(), 0)
                },
        )
    }
}

@Composable
private fun BubblePercent(
    progressText: String,
    progressColor: Brush,
    percentageTextColor: Color,
    modifier: Modifier = Modifier,
) {
    val theme = LocalTheme.current

    Column(
        modifier = modifier.width(BUBBLE_WIDTH),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(progressColor)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = progressText,
                style = theme.button.typography.buttonS,
                color = percentageTextColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
        }

        Canvas(
            modifier = Modifier.size(width = 8.dp, height = 6.dp),
        ) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width / 2, size.height)
                close()
            }
            drawPath(path = path, brush = progressColor)
        }
    }
}
