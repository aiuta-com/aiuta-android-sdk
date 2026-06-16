package com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.models

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.network.NetworkClient
import com.aiuta.fashionsdk.network.defaultNetworkClient
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.models.CachedTryOnModels
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class TryOnModelsRemoteDataSource(
    private val networkClient: NetworkClient,
) {
    suspend fun getTryOnModels(etag: String?): CachedTryOnModels? = withContext(Dispatchers.IO) {
        val response = networkClient.httpClient.value.get(TRY_ON_MODELS_PATH) {
            etag?.let { header(HttpHeaders.IfNoneMatch, it) }
        }

        if (response.status == HttpStatusCode.NotModified) return@withContext null

        CachedTryOnModels(
            etag = response.headers[HttpHeaders.ETag],
            models = response.body(),
        )
    }

    companion object {
        private const val TRY_ON_MODELS_PATH = "try_on_models"

        fun getInstance(aiuta: Aiuta): TryOnModelsRemoteDataSource = TryOnModelsRemoteDataSource(
            networkClient = defaultNetworkClient(
                aiuta = aiuta,
            ),
        )
    }
}
