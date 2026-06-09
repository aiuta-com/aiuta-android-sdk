package com.aiuta.fashionsdk.benchmark.flow

import androidx.benchmark.macro.MacrobenchmarkScope
import com.aiuta.fashionsdk.benchmark.tags.AiutaTestTags
import com.aiuta.fashionsdk.benchmark.tapTag
import com.aiuta.fashionsdk.benchmark.waitForTag

/** Sample flow selector → enter the Try On flow. */
internal fun MacrobenchmarkScope.enterTryOnFlow() {
    device.waitForTag(AiutaTestTags.FLOW_SELECTOR)
    device.tapTag(AiutaTestTags.TRY_ON_FLOW_BUTTON)
}

/** Sample flow selector → open the standalone History flow. */
internal fun MacrobenchmarkScope.openHistoryFlow() {
    device.waitForTag(AiutaTestTags.FLOW_SELECTOR)
    device.tapTag(AiutaTestTags.HISTORY_FLOW_BUTTON)
}
