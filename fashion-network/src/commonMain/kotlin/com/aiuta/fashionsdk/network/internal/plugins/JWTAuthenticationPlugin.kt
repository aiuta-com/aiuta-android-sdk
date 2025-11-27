package com.aiuta.fashionsdk.network.internal.plugins

import com.aiuta.fashionsdk.network.utils.authorizedPaths
import com.aiuta.fashionsdk.network.utils.jsonSerializer
import io.ktor.client.plugins.auth.AuthConfig
import io.ktor.client.plugins.auth.AuthProvider
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.content.TextContent
import io.ktor.http.HttpHeaders
import io.ktor.http.auth.HttpAuthHeader
import io.ktor.http.encodedPath
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

internal fun AuthConfig.jwt(block: JWTConfig.() -> Unit) {
    with(JWTConfig().apply(block)) {
        this@jwt.providers.add(JWTProvider(loadToken, sendWithoutRequest))
    }
}

internal class JWTConfig {
    internal var loadToken: suspend (params: String?) -> String? = { null }
    internal var sendWithoutRequest: (HttpRequestBuilder) -> Boolean = { true }

    fun sendWithoutRequest(block: (HttpRequestBuilder) -> Boolean) {
        this.sendWithoutRequest = block
    }

    fun loadTokens(block: suspend (params: String?) -> String) {
        this.loadToken = block
    }
}

internal class JWTProvider(
    private val loadToken: suspend (params: String?) -> String?,
    private val sendWithoutRequestCallback: (HttpRequestBuilder) -> Boolean,
) : AuthProvider {
    private var cachedToken: String? = null
    private val tokensHolder = AuthTokenHolder(loadToken)

    override val sendWithoutRequest: Boolean
        get() = error("Deprecated")

    override fun sendWithoutRequest(request: HttpRequestBuilder): Boolean = sendWithoutRequestCallback(request)

    override fun isApplicable(auth: HttpAuthHeader): Boolean = true

    override suspend fun addRequestHeaders(
        request: HttpRequestBuilder,
        authHeader: HttpAuthHeader?,
    ) {
        if (request.url.encodedPath !in authorizedPaths) return

        val token = cachedToken
            ?: tokensHolder.loadToken(request.solveJsonBody()).also { cachedToken = it }
            ?: return

        request.headers {
            val tokenValue = "Bearer $token"
            if (contains(HttpHeaders.Authorization)) {
                remove(HttpHeaders.Authorization)
            }
            append(HttpHeaders.Authorization, tokenValue)
        }
    }

    override suspend fun refreshToken(response: HttpResponse): Boolean {
        // Receive new token
        val newToken = tokensHolder.setToken { loadToken(response.solveJsonBody()) }

        // Update cached token
        cachedToken = newToken

        return newToken != null
    }

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    private fun HttpRequestBuilder?.solveJsonBody(): String? = try {
        this?.bodyType?.type?.let { type ->
            val actualSerializer =
                jsonSerializer.serializersModule.getContextual(type) ?: type.serializer()
            jsonSerializer.encodeToString(actualSerializer as KSerializer<Any>, body)
        }
    } catch (e: Exception) {
        null
    }

    private fun HttpResponse.solveJsonBody(): String? = (request.content as? TextContent)?.text
}
