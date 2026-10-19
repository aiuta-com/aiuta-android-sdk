package com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal.utils

import com.aiuta.fashionsdk.internal.subscription.model.AiutaOperationDelay
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

internal class DelayUtilsTest {
    private val defaultDelays = List(4) { 1000L } + List(20) { 500L } + listOf(3000L, 3000L)

    @Test
    fun defaultSequenceIsOneSecondFourTimesThenHalfSecondThenThreeSeconds() {
        assertEquals(defaultDelays, defaultGenerationDelaySequence().take(26).toList())
    }

    @Test
    fun missingOrEmptySequenceUsesTheDefault() {
        assertEquals(defaultDelays, operationDelaySequence(null).take(26).toList())
        assertEquals(defaultDelays, operationDelaySequence(emptyList()).take(26).toList())
    }

    @Test
    fun backendSequenceIsFollowed() {
        val delays = operationDelaySequence(
            listOf(
                PollingDelayStep(mode = PollingMode.RECURRING, delay = 1.0, repeat = 1),
                PollingDelayStep(mode = PollingMode.RECURRING, delay = 0.3, repeat = 2),
                PollingDelayStep(mode = PollingMode.INFINITE, delay = 2.0),
            ),
        )
        assertEquals(listOf(1000L, 300L, 300L, 2000L, 2000L, 2000L), delays.take(6).toList())
    }

    @Test
    fun sequenceWithoutInfiniteStepEndsWhichMeansTimeout() {
        val delays = operationDelaySequence(listOf(PollingDelayStep(mode = PollingMode.RECURRING, delay = 1.0, repeat = 2)))
        assertEquals(listOf(1000L, 1000L), delays.take(5).toList())
    }

    @Test
    fun unknownModesAndNonPositiveRepeatsAreSkipped() {
        val delays = operationDelaySequence(
            backendSteps(
                """
                [
                  {"mode": "exponential", "delay": 5.0},
                  {"mode": "recurring", "delay": 5.0, "repeat": 0},
                  {"mode": "recurring", "delay": 5.0, "repeat": -3},
                  {"mode": "recurring", "delay": 1.0, "repeat": 1}
                ]
                """,
            ),
        )
        assertEquals(listOf(1000L), delays.take(5).toList())
    }

    @Test
    fun sequenceWithoutUsableDelaysFallsBackToTheDefault() {
        // Wrong-case mode, missing repeat and a zero repeat count: nothing usable
        val delays = operationDelaySequence(
            backendSteps(
                """
                [
                  {"mode": "Recurring", "delay": 1.0, "repeat": 4},
                  {"mode": "recurring", "delay": 1.0},
                  {"mode": "recurring", "delay": 1.0, "repeat": 0}
                ]
                """,
            ),
        )
        assertEquals(defaultDelays, delays.take(26).toList())
    }

    @Test
    fun delaysAreClampedBetweenTenthOfSecondAndOneMinute() {
        val delays = operationDelaySequence(
            listOf(
                PollingDelayStep(mode = PollingMode.RECURRING, delay = 0.0, repeat = 1),
                PollingDelayStep(mode = PollingMode.RECURRING, delay = -1.0, repeat = 1),
                PollingDelayStep(mode = PollingMode.RECURRING, repeat = 1),
                // Milliseconds sent as seconds
                PollingDelayStep(mode = PollingMode.RECURRING, delay = 3000.0, repeat = 1),
            ),
        )
        assertEquals(listOf(100L, 100L, 100L, 60_000L), delays.toList())
    }

    @Test
    fun backendSequenceMapsToTheSchedule() {
        val delays = operationDelaySequence(
            backendSteps("""[{"mode": "recurring", "delay": 0.3, "repeat": 2}, {"mode": "infinite", "delay": 2.0}]"""),
        )

        assertEquals(listOf(300L, 300L, 2000L, 2000L), delays.take(4).toList())
    }

    @Test
    fun backendModesMatchExactlyLikeOnIosAndWeb() {
        assertEquals(PollingMode.RECURRING, PollingMode.fromBackend("recurring"))
        assertEquals(PollingMode.INFINITE, PollingMode.fromBackend("infinite"))

        for (unknown in listOf("Recurring", "INFINITE", "exponential", "", null)) {
            assertNull(PollingMode.fromBackend(unknown), "mode $unknown")
        }
    }

    /**
     * Steps as they come from the backend: [AiutaOperationDelay] can only be
     * created by deserialization
     */
    private fun backendSteps(json: String): List<PollingDelayStep> = Json
        .decodeFromString(ListSerializer(AiutaOperationDelay.serializer()), json)
        .map { it.toPollingDelayStep() }
}
