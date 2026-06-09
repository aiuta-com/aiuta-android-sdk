package com.aiuta.fashionsdk.benchmark.flow

import androidx.benchmark.macro.MacrobenchmarkScope
import com.aiuta.fashionsdk.benchmark.FLOW_ENTER_TIMEOUT_MS
import com.aiuta.fashionsdk.benchmark.MAX_ONBOARDING_PAGES
import com.aiuta.fashionsdk.benchmark.tags.AiutaTestTags
import com.aiuta.fashionsdk.benchmark.tapTag
import com.aiuta.fashionsdk.benchmark.waitForTag

/**
 * Walks pre-onboarding (welcome) + the multi-page onboarding until the image
 * selector ("select model") is reached. Every step is optional, so the journey
 * still works if onboarding was already completed on a warm device.
 */
internal fun MacrobenchmarkScope.passOnboarding() {
    // Welcome / pre-onboarding — the first screen appears only after the sample
    // finishes loading a product over the network, hence the long timeout.
    if (device.waitForTag(AiutaTestTags.WELCOME_START, FLOW_ENTER_TIMEOUT_MS)) {
        device.tapTag(AiutaTestTags.WELCOME_START)
    }
    // Advance onboarding pages until the image selector is shown.
    repeat(MAX_ONBOARDING_PAGES) {
        if (device.waitForTag(AiutaTestTags.SELECT_MODEL, timeoutMs = 1_500)) return
        if (!device.tapTag(AiutaTestTags.ONBOARDING_NEXT, timeoutMs = 2_000)) return
    }
}
