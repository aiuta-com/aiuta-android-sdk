package com.aiuta.fashionsdk.benchmark.flow

import androidx.benchmark.macro.MacrobenchmarkScope
import com.aiuta.fashionsdk.benchmark.SCREEN_TIMEOUT_MS
import com.aiuta.fashionsdk.benchmark.findByTag
import com.aiuta.fashionsdk.benchmark.flingDownUp
import com.aiuta.fashionsdk.benchmark.tags.AiutaTestTags
import com.aiuta.fashionsdk.benchmark.waitForTag

/** History grid → fling down and back up to capture first-scroll composition. */
internal fun MacrobenchmarkScope.scrollHistory() {
    if (!device.waitForTag(AiutaTestTags.HISTORY_GRID, SCREEN_TIMEOUT_MS)) return
    device.findByTag(AiutaTestTags.HISTORY_GRID)?.let { device.flingDownUp(it) }
}
