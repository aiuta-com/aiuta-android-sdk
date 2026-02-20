package com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.config

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.network.NetworkClient
import com.aiuta.fashionsdk.network.defaultNetworkClient
import com.aiuta.fashionsdk.tryon.compose.data.internal.entity.remote.config.ClientConfig
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class ConfigRemoteDataSource(
    private val networkClient: NetworkClient,
) {
    suspend fun getBackendConfig(etag: String?): ClientConfig = withContext(Dispatchers.IO) {
        val request = networkClient.httpClient.value.get(BACKEND_CONFIG_PATH) {
            header(IF_NOT_MATCH_HEADER_PARAM, etag)
        }

        ClientConfig(
            etag = request.headers[ETAG_HEADER_PARAM],
            predefinedTryOnModels = request.body(),
        )
    }

    companion object {
        private const val BACKEND_CONFIG_PATH = "predefined_try_on_models"

        private const val IF_NOT_MATCH_HEADER_PARAM = "if-none-match"
        private const val ETAG_HEADER_PARAM = "etag"

        fun getInstance(aiuta: Aiuta): ConfigRemoteDataSource = ConfigRemoteDataSource(
            networkClient = defaultNetworkClient(
                aiuta = aiuta,
            ),
        )
    }
}
