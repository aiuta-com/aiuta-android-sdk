package com.aiuta.fashionsdk.network

import com.aiuta.fashionsdk.Aiuta
import io.ktor.client.HttpClient

/**
 * Public interface of [NetworkClient] for creating network requests.
 *
 * In the current implementation, all interaction will be done via [httpClient]
 * as part of io.ktor
 */
public interface NetworkClient {
    public val httpClient: Lazy<HttpClient>

    public interface Factory {
        /**
         * Factory for [NetworkClient]
         *
         * @param aiuta Instance of Aiuta.
         * @param host Optional backend endpoint. Otherwise, will use default.
         * @param encodedPath Optional encoded path. Otherwise, will use default.
         *
         * @return [NetworkClient] that can fetch data by using [HttpClient]
         */
        public fun create(
            aiuta: Aiuta,
            host: String? = null,
            encodedPath: String? = null,
        ): NetworkClient
    }
}
