package com.aiuta.fashionsdk.internal.subscription.internal.remote

import com.aiuta.fashionsdk.internal.subscription.model.AiutaSubscriptionDetails
import com.aiuta.fashionsdk.network.NetworkClient
import com.aiuta.fashionsdk.network.exceptions.FashionIOException
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

internal sealed interface SubscriptionDetailsResponse {
    data class Modified(
        val details: AiutaSubscriptionDetails,
        val etag: String?,
    ) : SubscriptionDetailsResponse

    data object NotModified : SubscriptionDetailsResponse
}

internal interface SubscriptionDetailsRemoteDataSource {
    /**
     * Loads the subscription details, revalidating the cached ones by [etag]
     *
     * @throws Exception if the request fails
     */
    suspend fun getSubscriptionDetails(etag: String?): SubscriptionDetailsResponse
}

internal class KtorSubscriptionDetailsRemoteDataSource(
    private val networkClient: NetworkClient,
) : SubscriptionDetailsRemoteDataSource {
    override suspend fun getSubscriptionDetails(etag: String?): SubscriptionDetailsResponse {
        val response = try {
            networkClient.httpClient.value.get(urlString = PATH_SUBSCRIPTION_DETAILS) {
                etag?.let { header(HttpHeaders.IfNoneMatch, it) }
            }
        } catch (e: FashionIOException) {
            // The SDK client throws for every status but 200 and 401
            if (e.errorCode == HttpStatusCode.NotModified.value) return SubscriptionDetailsResponse.NotModified
            throw e
        }

        if (response.status == HttpStatusCode.NotModified) return SubscriptionDetailsResponse.NotModified

        // The client lets 401 through for the JWT refresh; its JSON error body
        // would otherwise parse as empty details and get cached as a success
        check(response.status.isSuccess()) { "Failed to load subscription details: ${response.status}" }

        return SubscriptionDetailsResponse.Modified(
            details = response.body(),
            etag = response.headers[HttpHeaders.ETag],
        )
    }

    private companion object {
        const val PATH_SUBSCRIPTION_DETAILS = "subscription_details"
    }
}
