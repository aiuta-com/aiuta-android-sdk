package com.aiuta.fashionsdk.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aiuta.fashionsdk.benchmark.flow.confirmModelSelection
import com.aiuta.fashionsdk.benchmark.flow.enterTryOnFlow
import com.aiuta.fashionsdk.benchmark.flow.openHistoryFromAppBar
import com.aiuta.fashionsdk.benchmark.flow.openModelSelector
import com.aiuta.fashionsdk.benchmark.flow.passOnboarding
import com.aiuta.fashionsdk.benchmark.flow.scrollHistory
import com.aiuta.fashionsdk.benchmark.flow.startGeneration
import com.aiuta.fashionsdk.benchmark.flow.waitForGenerationResult
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Generates the Baseline Profile shipped in the SDK AAR by replaying the main
 * Try On journey end to end:
 *
 * ```
 * launch → enter Try On flow → onboarding → select model → try on →
 *          generate → result → history → scroll
 * ```
 *
 * Each step is a `MacrobenchmarkScope` extension in the [flow] package
 * (mirrors the Now in Android benchmark structure). Run with:
 *
 * ```
 * ./gradlew :fashion-tryon-compose:generateBaselineProfile
 * ```
 *
 * The consumer plugin filters the collected rules to `com.aiuta.fashionsdk.**`
 * before writing them into the library's profile. Generation needs a valid
 * API key + network on the device (the steps after [startGeneration] are
 * null-safe, so a backend hiccup still yields a startup+flow profile).
 */
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() = rule.collect(
        packageName = TARGET_PACKAGE_NAME,
        includeInStartupProfile = true,
    ) {
        pressHome()
        startActivityAndWait()

        enterTryOnFlow()
        passOnboarding()
        openModelSelector()
        confirmModelSelection()
        startGeneration()
        waitForGenerationResult()
        openHistoryFromAppBar()
        scrollHistory()
    }
}
