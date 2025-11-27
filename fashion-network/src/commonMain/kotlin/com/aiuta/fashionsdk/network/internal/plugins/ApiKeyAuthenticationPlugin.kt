package com.aiuta.fashionsdk.network.internal.plugins

import io.ktor.client.plugins.auth.AuthConfig
import io.ktor.client.plugins.auth.AuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.http.auth.HttpAuthHeader

internal fun AuthConfig.apiKey(block: ApiKeyAuthConfig.() -> Unit) {
    with(ApiKeyAuthConfig().apply(block)) {
        this@apiKey.providers.add(ApiKeyAuthProvider(apiKey, sendWithoutRequest))
    }
}

internal class ApiKeyAuthConfig {
    internal lateinit var apiKey: String

    internal var sendWithoutRequest: (HttpRequestBuilder) -> Boolean = { true }

    fun sendWithoutRequest(block: (HttpRequestBuilder) -> Boolean) {
        this.sendWithoutRequest = block
    }

    fun setApiKey(apiKey: String) {
        this.apiKey = apiKey
    }
}

internal class ApiKeyAuthProvider(
    private val apiKey: String,
    private val sendWithoutRequestCallback: (HttpRequestBuilder) -> Boolean,
) : AuthProvider {
    @Deprecated("Please use sendWithoutRequest function instead", level = DeprecationLevel.ERROR)
    override val sendWithoutRequest: Boolean
        get() = error("Deprecated")

    override fun sendWithoutRequest(request: HttpRequestBuilder): Boolean = sendWithoutRequestCallback(request)

    override fun isApplicable(auth: HttpAuthHeader): Boolean = auth.authScheme == HEADER_API_KEY

    override suspend fun addRequestHeaders(
        request: HttpRequestBuilder,
        authHeader: HttpAuthHeader?,
    ) {
        request.header(HEADER_API_KEY, apiKey)
    }

    private companion object {
        const val HEADER_API_KEY = "x-api-key"
    }
}
