package com.aiuta.fashionsdk.sizefit.core.internal

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import com.aiuta.fashionsdk.logger.e
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFit
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitConfig
import com.aiuta.fashionsdk.sizefit.core.AiutaSizeFitRecommendation
import com.aiuta.fashionsdk.sizefit.core.exceptions.SizeFitApiException
import com.aiuta.fashionsdk.sizefit.core.exceptions.SizeFitNetworkException
import com.aiuta.fashionsdk.sizefit.core.internal.models.SizeFitResponseDTO
import com.aiuta.fashionsdk.sizefit.core.internal.models.fromDTO
import com.aiuta.fashionsdk.sizefit.core.internal.models.toDTO
import com.aiuta.fashionsdk.sizefit.core.internal.utils.createHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

internal class AiutaSizeFitImpl(
    private val apiKey: String,
    private val partitionHeader: String,
    private val aiutaLogger: AiutaLogger?,
) : AiutaSizeFit {

    private val httpClient: HttpClient = createHttpClient(
        aiutaLogger = aiutaLogger,
        apiKey = apiKey,
        partitionHeader = partitionHeader,
        sizeFitHost = DEFAULT_API_HOST,
    )

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

            val response: HttpResponse = httpClient.post(
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
            aiutaLogger?.d("recommendSize(): success - recommended size: ${result.recommendedSizeName}")

            return result.fromDTO()
        } catch (e: Exception) {
            // Error logging
            aiutaLogger?.e("recommendSize(): network error", e)
            throw SizeFitNetworkException(e)
        }
    }

    override fun close() {
        httpClient.close()
    }

    companion object {
        private const val DEFAULT_API_HOST = "api.naiz.fit"
        private const val PATH_RECOMMENDATION = "/recommendation"

        fun create(
            aiuta: Aiuta,
            apiKey: String,
            partitionHeader: String,
        ): AiutaSizeFit = AiutaSizeFitImpl(
            apiKey = apiKey,
            partitionHeader = partitionHeader,
            aiutaLogger = aiuta.logger,
        )
    }
}
