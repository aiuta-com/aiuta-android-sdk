package com.aiuta.fashionsdk.benchmark

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aiuta.fashionsdk.benchmark.flow.openHistoryFlow
import com.aiuta.fashionsdk.benchmark.flow.scrollHistory
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * First-scroll jank A/B benchmark for the SDK History grid (reached directly via
 * the sample's History flow, so it doesn't depend on a live generation).
 *
 * [FrameTimingMetric] reports frameDurationCpuMs / frameOverrunMs percentiles;
 * the headline number is P95 frameOverrunMs. Compare medians of
 * [scrollBaselineProfile] vs [scrollNoCompilation]. Requires generated history
 * on the device for the fling to do real work.
 */
@RunWith(AndroidJUnit4::class)
class ScrollBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun scrollNoCompilation() = scroll(CompilationMode.None())

    @Test
    fun scrollBaselineProfile() = scroll(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun scroll(compilationMode: CompilationMode) = rule.measureRepeated(
        packageName = TARGET_PACKAGE_NAME,
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        compilationMode = compilationMode,
    ) {
        startActivityAndWait()
        openHistoryFlow()
        scrollHistory()
    }
}
