package com.aiuta.fashionsdk.network.internal

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.network.NetworkClient
import io.ktor.client.HttpClient
import kotlin.concurrent.Volatile
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

internal class KtorNetworkClient(
    override val httpClient: Lazy<HttpClient>,
) : NetworkClient {
    companion object : NetworkClient.Factory, SynchronizedObject() {
        @Volatile
        private var instance: NetworkClient? = null
        private var cachedSubscriptionId: String? = null

        override fun create(
            aiuta: Aiuta,
            host: String?,
            encodedPath: String?,
        ): NetworkClient {
            validateCacheInstance(newSubscriptionId = aiuta.authenticationStrategy.subscriptionId)

            return instance ?: synchronized(this) {
                instance ?: buildKtorNetworkClient(
                    aiuta = aiuta,
                    host = host,
                    encodedPath = encodedPath,
                ).also {
                    instance = it
                    cachedSubscriptionId = aiuta.authenticationStrategy.subscriptionId
                }
            }
        }

        internal fun buildKtorNetworkClient(
            aiuta: Aiuta,
            host: String? = null,
            encodedPath: String? = null,
        ): NetworkClient = KtorNetworkClient(
            httpClient = KtorHttpClientFactory(
                authenticationStrategy = aiuta.authenticationStrategy,
                aiutaLogger = aiuta.logger,
                host = host,
                encodedPath = encodedPath,
            ).create(),
        )

        private fun validateCacheInstance(newSubscriptionId: String) {
            // We should remove cache, if we have new instance of api key
            if (newSubscriptionId != cachedSubscriptionId) {
                instance = null
                cachedSubscriptionId = newSubscriptionId
            }
        }
    }
}
