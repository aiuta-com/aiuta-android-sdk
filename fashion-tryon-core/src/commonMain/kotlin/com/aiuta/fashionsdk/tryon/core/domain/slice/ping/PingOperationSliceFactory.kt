package com.aiuta.fashionsdk.tryon.core.domain.slice.ping

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.productOperationsDataSourceFactory
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal.PingOperationSliceImpl
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal.delays.OperationDelaysProvider
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal.utils.operationDelaySequence
import kotlin.time.Duration.Companion.milliseconds

// How long polling may wait for the backend delays before it starts with the
// default ones; they are normally loaded during the photo upload already
private val DELAYS_WAIT_TIMEOUT = 500.milliseconds

internal val Aiuta.pingOperationSliceFactory: PingOperationSlice
    get() {
        val delaysProvider = OperationDelaysProvider.getInstance(aiuta = this)
        return PingOperationSliceImpl(
            productOperationsDataSource = this.productOperationsDataSourceFactory,
            delaySequenceFactory = {
                operationDelaySequence(delaysProvider.getDelays(timeout = DELAYS_WAIT_TIMEOUT))
            },
        )
    }
