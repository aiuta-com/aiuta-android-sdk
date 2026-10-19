package com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal.utils

import com.aiuta.fashionsdk.internal.subscription.model.AiutaOperationDelay
import kotlin.test.Test
import kotlin.test.assertEquals
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
                PollingDelayStep(mode = "recurring", delay = 1.0, repeat = 1),
                PollingDelayStep(mode = "recurring", delay = 0.3, repeat = 2),
                PollingDelayStep(mode = "infinite", delay = 2.0),
            ),
        )
        assertEquals(listOf(1000L, 300L, 300L, 2000L, 2000L, 2000L), delays.take(6).toList())
    }

    @Test
    fun sequenceWithoutInfiniteStepEndsWhichMeansTimeout() {
        val delays = operationDelaySequence(listOf(PollingDelayStep(mode = "recurring", delay = 1.0, repeat = 2)))
        assertEquals(listOf(1000L, 1000L), delays.take(5).toList())
    }

    @Test
    fun unknownModesAndNonPositiveRepeatsAreSkipped() {
        val delays = operationDelaySequence(
            listOf(
                PollingDelayStep(mode = "exponential", delay = 5.0),
                PollingDelayStep(mode = "recurring", delay = 5.0, repeat = 0),
                PollingDelayStep(mode = "recurring", delay = 5.0, repeat = -3),
                PollingDelayStep(mode = "recurring", delay = 1.0, repeat = 1),
            ),
        )
        assertEquals(listOf(1000L), delays.take(5).toList())
    }

    @Test
    fun sequenceWithoutUsableDelaysFallsBackToTheDefault() {
        // Wrong-case mode, missing repeat and a zero repeat count: nothing usable
        val delays = operationDelaySequence(
            listOf(
                PollingDelayStep(mode = "Recurring", delay = 1.0, repeat = 4),
                PollingDelayStep(mode = "recurring", delay = 1.0),
                PollingDelayStep(mode = "recurring", delay = 1.0, repeat = 0),
            ),
        )
        assertEquals(defaultDelays, delays.take(26).toList())
    }

    @Test
    fun delaysAreClampedBetweenTenthOfSecondAndOneMinute() {
        val delays = operationDelaySequence(
            listOf(
                PollingDelayStep(mode = "recurring", delay = 0.0, repeat = 1),
                PollingDelayStep(mode = "recurring", delay = -1.0, repeat = 1),
                PollingDelayStep(mode = "recurring", repeat = 1),
                // Milliseconds sent as seconds
                PollingDelayStep(mode = "recurring", delay = 3000.0, repeat = 1),
            ),
        )
        assertEquals(listOf(100L, 100L, 100L, 60_000L), delays.toList())
    }

    @Test
    fun backendSequenceMapsToTheSchedule() {
        // AiutaOperationDelay can only come from deserialization, as from the backend
        val backendDelays = Json.decodeFromString(
            ListSerializer(AiutaOperationDelay.serializer()),
            """[{"mode": "recurring", "delay": 0.3, "repeat": 2}, {"mode": "infinite", "delay": 2.0}]""",
        )

        val delays = operationDelaySequence(backendDelays.map { it.toPollingDelayStep() })

        assertEquals(listOf(300L, 300L, 2000L, 2000L), delays.take(4).toList())
    }
}
