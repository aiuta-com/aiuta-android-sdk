package com.aiuta.fashionsdk.tryon.compose.uikit.indicator

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.tryon.compose.uikit.indicator.internal.IndicatorItem
import com.aiuta.fashionsdk.tryon.compose.uikit.indicator.internal.PagerProgress
import com.aiuta.fashionsdk.tryon.compose.uikit.indicator.internal.getProgressFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

/**
 * VERTICAL or HORIZONTAL indicator for Pager
 *
 * @param pagerState state of listened Pager
 * @param color color of current selected page
 * @param indicatorCount count of visible indicators
 * @param indicatorSmallSize size of inactive page
 * @param indicatorBigSize size of active page
 * @param space distance between indicators
 * @param inactiveColorAlpha alpha of inactive indicator. Color of inactive indicator is [color]
 * @param orientation orientation of this view
 **/
@Composable
public fun PagerIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    color: Color,
    initialPage: Int = 0,
    indicatorCount: Int = 5,
    indicatorSmallSize: Dp = 4.dp,
    indicatorBigSize: Dp = 20.dp,
    space: Dp = 6.dp,
    inactiveColorAlpha: Float = 0.3f,
    orientation: IndicatorOrientation = IndicatorOrientation.HORIZONTAL,
) {
    val updatablePageCount by rememberUpdatedState(newValue = pagerState.pageCount)

    val listState = rememberLazyListState()
    val realIndicatorCount = minOf(indicatorCount, updatablePageCount)
    val totalWidth: Dp =
        indicatorSmallSize * (realIndicatorCount - 1) +
            indicatorBigSize + space * (realIndicatorCount - 1)
    val totalWidthPx = LocalDensity.current.run { totalWidth.toPx() }
    val indicatorBigSizePx = LocalDensity.current.run { indicatorBigSize.toPx() }
    val distancePx = LocalDensity.current.run { space.toPx() + indicatorSmallSize.toPx() }

    val progressState =
        remember {
            mutableStateOf(
                with(pagerState) {
                    PagerProgress(currentPageOffsetFraction, currentPage, isScrollInProgress)
                },
            )
        }

    // Auto scroll to center while initialization
    LaunchedEffect(Unit) {
        val initScroll =
            if (needToAutoscroll(progressState.value, realIndicatorCount, updatablePageCount)) {
                -(totalWidthPx - indicatorBigSizePx) / 2
            } else {
                0f
            }
        listState.scrollToItem(initialPage, initScroll.toInt())
    }

    // Auto scroll pager list and modify progressState
    LaunchedEffect(pagerState) {
        var consumed = 0f
        pagerState.getProgressFlow()
            .shareIn(this, SharingStarted.Eagerly, 10)
            .collect { progress ->
                progressState.value = progress
                if (needToAutoscroll(progressState.value, realIndicatorCount, updatablePageCount)) {
                    // move only if we are in a middle of preview
                    if (!progress.isScrollInProgress) {
                        consumed = 0f
                    }
                    consumed += listState.scrollBy(distancePx * progress.offset - consumed)
                } else {
                    consumed = 0f
                }
            }
    }

    PagerList(
        modifier = modifier,
        orientation = orientation,
        listState = listState,
        pageCount = updatablePageCount,
        progressState = progressState.value,
        totalWidth = totalWidth,
        space = space,
        indicatorSmallSize = indicatorSmallSize,
        indicatorBigSize = indicatorBigSize,
        color = color,
        inactiveColorAlpha = inactiveColorAlpha,
    )
}

@Composable
private fun PagerList(
    modifier: Modifier,
    orientation: IndicatorOrientation,
    listState: LazyListState,
    pageCount: Int,
    progressState: PagerProgress,
    totalWidth: Dp,
    space: Dp,
    indicatorSmallSize: Dp,
    indicatorBigSize: Dp,
    color: Color,
    inactiveColorAlpha: Float,
) {
    if (orientation == IndicatorOrientation.HORIZONTAL) {
        LazyRow(
            modifier = modifier.width(totalWidth),
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(space),
            userScrollEnabled = false,
        ) {
            items(pageCount) { index ->
                IndicatorItem(
                    index = index,
                    progressState = progressState,
                    color = color,
                    inactiveColorAlpha = inactiveColorAlpha,
                    indicatorSmallSize = indicatorSmallSize,
                    indicatorBigSize = indicatorBigSize,
                    orientation = orientation,
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.height(totalWidth),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(space),
            userScrollEnabled = false,
        ) {
            items(pageCount) { index ->
                IndicatorItem(
                    index = index,
                    progressState = progressState,
                    color = color,
                    inactiveColorAlpha = inactiveColorAlpha,
                    indicatorSmallSize = indicatorSmallSize,
                    indicatorBigSize = indicatorBigSize,
                    orientation = orientation,
                )
            }
        }
    }
}

private fun needToAutoscroll(
    progress: PagerProgress,
    indicatorCount: Int,
    pageCount: Int,
): Boolean = progress.position in (indicatorCount / 2) until pageCount - (indicatorCount / 2)
