package com.aiuta.fashionsdk.benchmark

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Cold-startup A/B benchmark proving the Baseline Profile moves the number.
 *
 * Run with:
 * `./gradlew :internal:benchmark:pixel6Api34BenchmarkAndroidTest`
 *
 * Compare the medians of [startupBaselineProfile] (profile applied) against
 * [startupNoCompilation] (interpreted) — report medians, not means.
 */
@RunWith(AndroidJUnit4::class)
class StartupBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    /** Baseline: no AOT compilation, the slow path. */
    @Test
    fun startupNoCompilation() = startup(CompilationMode.None())

    /**
     * Profile applied. [BaselineProfileMode.Require] fails loudly if the
     * profile is missing instead of silently measuring an unprofiled build.
     */
    @Test
    fun startupBaselineProfile() = startup(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun startup(compilationMode: CompilationMode) = rule.measureRepeated(
        packageName = TARGET_PACKAGE_NAME,
        metrics = listOf(StartupTimingMetric()),
        iterations = 10,
        startupMode = StartupMode.COLD,
        compilationMode = compilationMode,
    ) {
        pressHome()
        startActivityAndWait()
    }
}
