package com.aiuta.fashionsdk.sizefit.core.internal.utils

import com.aiuta.fashionsdk.logger.AiutaLogger
import com.aiuta.fashionsdk.logger.d
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private object SizeFitKtorConfig {
    const val REQUEST_TIMEOUT_MILLS = 30_000L
    const val CONNECT_TIMEOUT_MILLS = 10_000L

    const val MAX_RETRIES = 3
}

internal fun createHttpClient(
    aiutaLogger: AiutaLogger?,
    apiKey: String,
    partitionHeader: String,
    sizeFitHost: String,
): HttpClient = HttpClient {
    // Timeout configuration
    install(HttpTimeout) {
        requestTimeoutMillis = SizeFitKtorConfig.REQUEST_TIMEOUT_MILLS
        connectTimeoutMillis = SizeFitKtorConfig.CONNECT_TIMEOUT_MILLS
        socketTimeoutMillis = SizeFitKtorConfig.REQUEST_TIMEOUT_MILLS
    }

    // Retry mechanism (Problem #6)
    install(HttpRequestRetry) {
        retryOnServerErrors(maxRetries = SizeFitKtorConfig.MAX_RETRIES)
        exponentialDelay()
    }

    install(ContentNegotiation) {
        json(
            json = Json {
                ignoreUnknownKeys = true
            },
        )
    }

    aiutaLogger?.let {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    aiutaLogger.d(message)
                }
            }
            level = LogLevel.ALL
        }
    }

    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
        header("X-API-Key", apiKey)
        header("X-Partition", partitionHeader)
        url {
            protocol = URLProtocol.HTTPS
            host = sizeFitHost
        }
    }
}
