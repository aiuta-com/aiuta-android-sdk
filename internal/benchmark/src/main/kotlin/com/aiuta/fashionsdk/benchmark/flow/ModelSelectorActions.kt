package com.aiuta.fashionsdk.benchmark.flow

import androidx.benchmark.macro.MacrobenchmarkScope
import com.aiuta.fashionsdk.benchmark.SCREEN_TIMEOUT_MS
import com.aiuta.fashionsdk.benchmark.tags.AiutaTestTags
import com.aiuta.fashionsdk.benchmark.tapTag

/**
 * Model selector → confirm the default-selected predefined model and return to
 * the image selector with auto try-on armed. Predefined models avoid any photo
 * upload, keeping the journey deterministic.
 */
internal fun MacrobenchmarkScope.confirmModelSelection() {
    device.tapTag(AiutaTestTags.MODEL_TRY_ON, SCREEN_TIMEOUT_MS)
}
