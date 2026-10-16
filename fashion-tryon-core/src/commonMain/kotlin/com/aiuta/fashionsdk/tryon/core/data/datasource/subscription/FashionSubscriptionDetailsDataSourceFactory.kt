package com.aiuta.fashionsdk.tryon.core.data.datasource.subscription

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.network.defaultNetworkClient
import com.aiuta.fashionsdk.tryon.core.data.datasource.subscription.internal.FashionSubscriptionDetailsRemoteDataSource

internal val Aiuta.subscriptionDetailsDataSourceFactory: FashionSubscriptionDetailsDataSource
    get() =
        FashionSubscriptionDetailsRemoteDataSource(
            networkClient =
            defaultNetworkClient(
                aiuta = this,
            ),
        )
