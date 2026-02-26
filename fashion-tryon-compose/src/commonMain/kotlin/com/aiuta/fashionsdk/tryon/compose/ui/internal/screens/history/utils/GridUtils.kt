package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.history.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.aiuta.fashionsdk.compose.core.size.rememberScreenSize

@Composable
internal fun calculateGridColumnsCount(
    preferredWidth: Dp,
    minColumnsCount: Int,
    contentPadding: Dp,
    horizontalSpacing: Dp,
): Int {
    val screenSize = rememberScreenSize()
    val availableWidth = screenSize.widthDp - contentPadding * 2
    val adaptiveCount = ((availableWidth + horizontalSpacing) / (preferredWidth + horizontalSpacing)).toInt()
    return maxOf(minColumnsCount, adaptiveCount)
}
