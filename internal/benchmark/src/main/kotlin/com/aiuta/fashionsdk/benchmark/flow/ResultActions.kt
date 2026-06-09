package com.aiuta.fashionsdk.benchmark.flow

import androidx.benchmark.macro.MacrobenchmarkScope
import com.aiuta.fashionsdk.benchmark.GENERATION_TIMEOUT_MS
import com.aiuta.fashionsdk.benchmark.SCREEN_TIMEOUT_MS
import com.aiuta.fashionsdk.benchmark.tags.AiutaTestTags
import com.aiuta.fashionsdk.benchmark.tapTag
import com.aiuta.fashionsdk.benchmark.waitForTag

/** Wait for the generation result screen — a backend round-trip. */
internal fun MacrobenchmarkScope.waitForGenerationResult(): Boolean = device.waitForTag(AiutaTestTags.RESULT_SCREEN, GENERATION_TIMEOUT_MS)

/** Open History via the main app-bar icon (shown once generations exist). */
internal fun MacrobenchmarkScope.openHistoryFromAppBar() {
    device.tapTag(AiutaTestTags.HISTORY_APP_BAR, SCREEN_TIMEOUT_MS)
}
