package com.aiuta.fashionsdk.tryon.core.domain.slice.ping

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.internal.subscription.internalAiutaSubscription
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.productOperationsDataSourceFactory
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal.PingOperationSliceImpl
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal.utils.operationDelaySequence
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal.utils.toPollingDelayStep
import kotlin.time.Duration.Companion.milliseconds

// How long polling may wait for the subscription details when none are cached
// yet (e.g. headless use without a preload), before it starts with the default
// delays. Cached details are used right away.
private val DELAYS_WAIT_TIMEOUT = 500.milliseconds

internal val Aiuta.pingOperationSliceFactory: PingOperationSlice
    get() {
        val subscription = internalAiutaSubscription
        return PingOperationSliceImpl(
            productOperationsDataSource = this.productOperationsDataSourceFactory,
            delaySequenceFactory = {
                val delays = subscription.getDetails(timeout = DELAYS_WAIT_TIMEOUT)?.operationDelaysSequence
                operationDelaySequence(delays?.map { it.toPollingDelayStep() })
            },
        )
    }
