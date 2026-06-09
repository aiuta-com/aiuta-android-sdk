package com.aiuta.fashionsdk.benchmark.flow

import androidx.benchmark.macro.MacrobenchmarkScope
import com.aiuta.fashionsdk.benchmark.SCREEN_TIMEOUT_MS
import com.aiuta.fashionsdk.benchmark.tags.AiutaTestTags
import com.aiuta.fashionsdk.benchmark.tapTag

/** Image selector (empty state) → open the predefined-model selector. */
internal fun MacrobenchmarkScope.openModelSelector() {
    device.tapTag(AiutaTestTags.SELECT_MODEL, SCREEN_TIMEOUT_MS)
}

/** Image selector (model chosen) → start try-on generation. */
internal fun MacrobenchmarkScope.startGeneration() {
    device.tapTag(AiutaTestTags.GENERATE, SCREEN_TIMEOUT_MS)
}
