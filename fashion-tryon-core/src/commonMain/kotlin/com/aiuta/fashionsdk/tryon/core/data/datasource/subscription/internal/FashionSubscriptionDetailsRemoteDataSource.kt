package com.aiuta.fashionsdk.tryon.core.data.datasource.subscription.internal

import com.aiuta.fashionsdk.network.NetworkClient
import com.aiuta.fashionsdk.tryon.core.data.datasource.subscription.FashionSubscriptionDetailsDataSource
import com.aiuta.fashionsdk.tryon.core.data.datasource.subscription.models.OperationDelaysDetails
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class FashionSubscriptionDetailsRemoteDataSource(
    private val networkClient: NetworkClient,
) : FashionSubscriptionDetailsDataSource {
    // No ETag: the try-on core keeps the delays only in memory, so a 304
    // would leave it without data
    override suspend fun getOperationDelaysDetails(): OperationDelaysDetails = networkClient.httpClient.value.get(
        urlString = PATH_SUBSCRIPTION_DETAILS,
    ).body()

    private companion object {
        const val PATH_SUBSCRIPTION_DETAILS = "subscription_details"
    }
}
