package com.aiuta.fashionsdk.compose.uikit.indicator.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.aiuta.fashionsdk.compose.uikit.indicator.IndicatorOrientation
import kotlin.math.abs

@Composable
internal fun IndicatorItem(
    index: Int,
    progressState: PagerProgress,
    color: Color,
    inactiveColorAlpha: Float,
    indicatorSmallSize: Dp,
    indicatorBigSize: Dp,
    orientation: IndicatorOrientation,
) {
    val progress: Float
    val size: Dp

    if (index == progressState.position) {
        progress = 1 - abs(progressState.offset)
        size = (indicatorBigSize - indicatorSmallSize) * progress + indicatorSmallSize
    } else {
        val isLeftModifiedItem =
            progressState.offset < 0f && progressState.position - index == 1
        val isRightModifiedItem =
            progressState.offset > 0f && index - progressState.position == 1
        if (isLeftModifiedItem || isRightModifiedItem) {
            progress = abs(progressState.offset)
            size = indicatorSmallSize + (indicatorBigSize - indicatorSmallSize) * progress
        } else {
            progress = 0f
            size = indicatorSmallSize
        }
    }
    val shape = RoundedCornerShape(indicatorSmallSize / 2)
    Box(
        modifier =
        Modifier
            .clip(shape)
            .then(
                if (orientation == IndicatorOrientation.HORIZONTAL) {
                    Modifier.size(size, indicatorSmallSize)
                } else {
                    Modifier.size(indicatorSmallSize, size)
                },
            )
            .background(
                color =
                color.copy(
                    alpha = (inactiveColorAlpha + (1 - inactiveColorAlpha) * abs(progress)),
                ),
                shape = shape,
            ),
    )
}
