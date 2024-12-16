package com.aiuta.fashionsdk.tryon.core.domain.slice.ping

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.skuOperationsDataSourceFactory
import com.aiuta.fashionsdk.tryon.core.domain.slice.ping.internal.PingOperationSliceImpl

internal val Aiuta.pingOperationSliceFactory: PingOperationSlice
    get() =
        PingOperationSliceImpl(
            skuOperationsDataSource = this.skuOperationsDataSourceFactory,
        )
