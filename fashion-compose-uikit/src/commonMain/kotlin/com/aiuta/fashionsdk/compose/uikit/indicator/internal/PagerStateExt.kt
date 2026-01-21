package com.aiuta.fashionsdk.compose.uikit.indicator.internal

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningReduce

/**
 * Return normalized scroll position, where scroll of every page can be:
 * - [0.0;1.0] left scroll (go forward)
 * - [-1.0;0.0] right scroll (go back)
 *
 * @return Flow<[PagerProgress]>
 **/
@OptIn(ExperimentalCoroutinesApi::class)
internal fun PagerState.getProgressFlow(): Flow<PagerProgress> = snapshotFlow { currentPage }
    .flatMapLatest { currentPage ->
        snapshotFlow { currentPageOffsetFraction }.map { Pair(it, currentPage) }
    }
    .flatMapLatest { pair ->
        snapshotFlow { isScrollInProgress }.map { PagerProgress(pair.first, pair.second, it) }
    }
    .runningReduce { last, new ->
        when {
            // page in idle state
            !new.isScrollInProgress -> new

            // scroll to left (forward) and active page is changed
            last.position < new.position -> {
                val offset = 1 + new.offset
                if (offset > 1) {
                    // scroll is very big. User's just jumped over page.
                    // User's just jumped from n position to n+2.
                    // Return final state of page with index n+1
                    PagerProgress(1f, last.position + 1, false)
                } else {
                    PagerProgress(offset, last.position, true)
                }
            }

            // scroll to right (back) and active page is changed
            last.position > new.position -> {
                val offset = -1 + new.offset
                if (offset < -1) {
                    // scroll is very big. User's just jumped over page.
                    // User's just jumped from n position to n-2.
                    // Return final state of page with index n-1
                    PagerProgress(-1f, last.position - 1, false)
                } else {
                    PagerProgress(offset, last.position, true)
                }
            }

            // page is not changed
            else -> {
                PagerProgress(new.offset, last.position, true)
            }
        }
    }

internal data class PagerProgress(val offset: Float, val position: Int, val isScrollInProgress: Boolean)
