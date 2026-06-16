package com.aiuta.fashionsdk.tryon.compose.ui.internal.screens.model.general.components.list

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aiuta.fashionsdk.compose.core.size.rememberScreenSize

@Composable
internal fun CentredModelsHorizontalPager(
    state: PagerState,
    modifier: Modifier = Modifier,
    beyondViewportPageCount: Int = PagerDefaults.BeyondViewportPageCount,
    pageSpacing: Dp = 6.dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    flingBehavior: TargetedFlingBehavior = PagerDefaults.flingBehavior(state = state),
    userScrollEnabled: Boolean = true,
    reverseLayout: Boolean = false,
    key: ((index: Int) -> Any)? = null,
    pageNestedScrollConnection: NestedScrollConnection =
        PagerDefaults.pageNestedScrollConnection(
            state,
            Orientation.Horizontal,
        ),
    snapPosition: SnapPosition = SnapPosition.Start,
    pageContent: @Composable PagerScope.(page: Int) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    val activeItemWidth = 76.dp

    val screenSize = rememberScreenSize()
    val startPadding = (screenSize.widthDp - activeItemWidth) / 2

    LaunchedEffect(state.currentPage) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    HorizontalPager(
        modifier = modifier,
        state = state,
        pageSize = PageSize.Fixed(activeItemWidth),
        pageSpacing = pageSpacing,
        beyondViewportPageCount = beyondViewportPageCount,
        verticalAlignment = verticalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        contentPadding = PaddingValues(horizontal = startPadding),
        reverseLayout = reverseLayout,
        key = key,
        pageNestedScrollConnection = pageNestedScrollConnection,
        snapPosition = snapPosition,
        pageContent = pageContent,
    )
}
