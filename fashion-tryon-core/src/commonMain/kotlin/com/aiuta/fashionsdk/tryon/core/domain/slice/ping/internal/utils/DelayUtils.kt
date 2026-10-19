package com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal.utils

import com.aiuta.fashionsdk.internal.subscription.model.AiutaOperationDelay
import kotlin.math.roundToLong

// Guard against misconfigured delays: zero/negative ones would turn polling
// into a busy loop, huge ones (e.g. milliseconds sent as seconds) would stall it
private const val MIN_DELAY_MS = 100L
private const val MAX_DELAY_MS = 60_000L

/**
 * How a step of the polling schedule repeats its delay
 */
internal enum class PollingMode(private val backendValue: String) {
    /** The delay is waited `repeat` times */
    RECURRING("recurring"),

    /** The delay is waited until the operation ends */
    INFINITE("infinite"),
    ;

    companion object {
        /**
         * The mode for a backend `mode` value, or null for an unknown one.
         * The backend model keeps it as a string, so a new or misspelled
         * value only skips that step instead of failing the whole response.
         * Matching is exact, as on iOS and Web.
         */
        fun fromBackend(value: String?): PollingMode? = entries.firstOrNull { it.backendValue == value }
    }
}

/**
 * One step of the polling schedule. The core keeps its own type instead of
 * creating [AiutaOperationDelay], whose constructor is internal to
 * internal-fashion-subscription.
 *
 * @property mode null for a mode this SDK doesn't know: the step is skipped
 */
internal data class PollingDelayStep(
    val mode: PollingMode?,
    val delay: Double? = null,
    val repeat: Int? = null,
)

internal fun AiutaOperationDelay.toPollingDelayStep(): PollingDelayStep = PollingDelayStep(
    mode = PollingMode.fromBackend(mode),
    delay = delay,
    repeat = repeat,
)

/**
 * Delay before getting operation status in second:
 * 1 (4 times), 0.5 (20 times), 3 (forever)
 */
internal val DEFAULT_OPERATION_DELAYS: List<PollingDelayStep> = listOf(
    PollingDelayStep(mode = PollingMode.RECURRING, delay = 1.0, repeat = 4),
    PollingDelayStep(mode = PollingMode.RECURRING, delay = 0.5, repeat = 20),
    PollingDelayStep(mode = PollingMode.INFINITE, delay = 3.0),
)

internal fun defaultGenerationDelaySequence(): Sequence<Long> = generationDelaySequence(DEFAULT_OPERATION_DELAYS)

/**
 * Delays in milliseconds, each waited before an operation status poll.
 *
 * Steps with an unknown mode or a non-positive repeat count are skipped; a
 * missing sequence, or one without a single usable delay, means the default
 * one. The sequence ends (the operation times out) after the last recurring
 * step unless there is an infinite one.
 */
internal fun operationDelaySequence(delays: List<PollingDelayStep>?): Sequence<Long> = sequence {
    var hasDelays = false
    for (delay in generationDelaySequence(delays.orEmpty())) {
        hasDelays = true
        yield(delay)
    }

    // A misconfigured sequence must not time out every operation before the
    // first poll
    if (!hasDelays) yieldAll(defaultGenerationDelaySequence())
}

private fun generationDelaySequence(steps: List<PollingDelayStep>): Sequence<Long> = sequence {
    for (step in steps) {
        val delaySeconds = step.delay?.takeIf { it.isFinite() } ?: 0.0
        val delayMs = (delaySeconds * 1000).roundToLong().coerceIn(MIN_DELAY_MS, MAX_DELAY_MS)

        when (step.mode) {
            PollingMode.RECURRING -> repeat((step.repeat ?: 0).coerceAtLeast(0)) { yield(delayMs) }
            PollingMode.INFINITE -> while (true) yield(delayMs)
            null -> Unit // Unknown mode: skipped
        }
    }
}
