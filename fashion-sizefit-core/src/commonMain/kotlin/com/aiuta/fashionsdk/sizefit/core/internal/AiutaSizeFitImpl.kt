package com.aiuta.fashionsdk.sizefit.core.internal

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.logger.e
import com.aiuta.fashionsdk.network.NetworkClient
import com.aiuta.fashionsdk.network.defaultNetworkClient
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFit
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitConfig
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitRecommendation
import com.aiuta.fashionsdk.sizefit.core.exceptions.SizeFitApiException
import com.aiuta.fashionsdk.sizefit.core.exceptions.SizeFitNetworkException
import com.aiuta.fashionsdk.sizefit.core.internal.models.SizeFitResponseDTO
import com.aiuta.fashionsdk.sizefit.core.internal.models.fromDTO
import com.aiuta.fashionsdk.sizefit.core.internal.models.toDTO
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

internal class AiutaSizeFitImpl(
    private val aiutaLogger: AiutaLogger?,
    private val networkClient: NetworkClient,
) : AiutaSizeFit {

    override suspend fun recommendSize(
        code: String,
        config: AiutaSizeFitConfig,
    ): AiutaSizeFitRecommendation {
        // Validation (Problem #4)
        require(code.isNotBlank()) {
            "Size chart code cannot be blank"
        }

        aiutaLogger?.d("recommendSize()")

        try {
            currentCoroutineContext().ensureActive()

            val response: HttpResponse = networkClient.httpClient.value.post(
                urlString = PATH_RECOMMENDATION,
            ) {
                setBody(config.toDTO(code))
            }

            // HTTP error handling
            if (!response.status.isSuccess()) {
                aiutaLogger?.e("API request failed with status ${response.status.value}")

                throw SizeFitApiException(response.status.value)
            }

            val result = response.body<SizeFitResponseDTO>()
            aiutaLogger?.d("recommendSize(): success - recommended size - ${result.recommendedSizeName}")

            return result.fromDTO()
        } catch (e: Exception) {
            // Error logging
            aiutaLogger?.e("recommendSize(): network error", e)
            throw SizeFitNetworkException(e)
        }
    }

    override fun close() {
        networkClient.httpClient.value.close()
    }

    companion object {
        private const val PATH_RECOMMENDATION = "size_and_fit/recommendation"

        fun create(
            aiuta: Aiuta,
        ): AiutaSizeFit = AiutaSizeFitImpl(
            aiutaLogger = aiuta.logger,
            networkClient = defaultNetworkClient(aiuta),
        )
    }
}
