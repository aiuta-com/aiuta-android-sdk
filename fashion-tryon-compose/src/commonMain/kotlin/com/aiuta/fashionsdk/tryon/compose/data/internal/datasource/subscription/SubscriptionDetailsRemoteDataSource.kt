package com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.subscription

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.network.NetworkClient
import com.aiuta.fashionsdk.network.defaultNetworkClient
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.subscription.SubscriptionDetailsConfig
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class SubscriptionDetailsRemoteDataSource(
    private val networkClient: NetworkClient,
) {
    suspend fun getSubscriptionDetails(etag: String?): SubscriptionDetailsConfig = withContext(Dispatchers.IO) {
        val request = networkClient.httpClient.value.get(BACKEND_CONFIG_PATH) {
            header(IF_NOT_MATCH_HEADER_PARAM, etag)
        }

        SubscriptionDetailsConfig(
            etag = request.headers[ETAG_HEADER_PARAM],
            details = request.body(),
        )
    }

    companion object Companion {
        private const val BACKEND_CONFIG_PATH = "subscription_details"

        private const val IF_NOT_MATCH_HEADER_PARAM = "if-none-match"
        private const val ETAG_HEADER_PARAM = "etag"

        fun getInstance(aiuta: Aiuta): SubscriptionDetailsRemoteDataSource = SubscriptionDetailsRemoteDataSource(
            networkClient = defaultNetworkClient(
                aiuta = aiuta,
            ),
        )
    }
}
