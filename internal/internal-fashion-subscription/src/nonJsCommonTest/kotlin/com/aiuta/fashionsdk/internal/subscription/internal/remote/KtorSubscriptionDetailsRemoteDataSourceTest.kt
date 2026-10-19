package com.aiuta.fashionsdk.internal.subscription.internal.remote

import com.aiuta.fashionsdk.internal.subscription.model.AiutaOperationDelay
import com.aiuta.fashionsdk.internal.subscription.model.AiutaPoweredBySticker
import com.aiuta.fashionsdk.internal.subscription.model.AiutaSubscriptionDetails
import com.aiuta.fashionsdk.network.NetworkClient
import com.aiuta.fashionsdk.network.exceptions.FashionIOException
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json

internal class KtorSubscriptionDetailsRemoteDataSourceTest {
    private val requests = mutableListOf<HttpRequestData>()

    /** Data source over a client configured like the SDK one (ignoreUnknownKeys) */
    private fun dataSource(
        handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData,
    ): KtorSubscriptionDetailsRemoteDataSource {
        val engine = MockEngine { request ->
            requests += request
            handler(request)
        }
        val client = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        return KtorSubscriptionDetailsRemoteDataSource(
            networkClient = object : NetworkClient {
                override val httpClient: Lazy<HttpClient> = lazyOf(client)
            },
        )
    }

    private fun MockRequestHandleScope.json(
        body: String,
        status: HttpStatusCode = HttpStatusCode.OK,
        etag: String? = null,
    ) = respond(
        content = body,
        status = status,
        headers = headersOf(
            *listOfNotNull(
                HttpHeaders.ContentType to listOf("application/json"),
                etag?.let { HttpHeaders.ETag to listOf(it) },
            ).toTypedArray(),
        ),
    )

    @Test
    fun parsesTheConfigTheSdkUsesAndIgnoresOtherFields() = runTest {
        val response = dataSource {
            json(
                etag = "\"v1\"",
                body = """
                    {
                      "subscription_id": "subscription",
                      "powered_by_sticker": {"url_android": "https://aiuta.com/android", "is_visible": true},
                      "retry_counts": {"photo_upload": 2},
                      "operation_delays_sequence": [
                        {"mode": "recurring", "delay": 1.0, "repeat": 4},
                        {"mode": "infinite", "delay": 3.0}
                      ],
                      "predefined_try_on_models": [{"category": "man", "models": []}],
                      "default_output_images_format": "jpeg"
                    }
                """.trimIndent(),
            )
        }.getSubscriptionDetails(etag = null)

        assertEquals(
            SubscriptionDetailsResponse.Modified(
                details = AiutaSubscriptionDetails(
                    poweredBySticker = AiutaPoweredBySticker(urlAndroid = "https://aiuta.com/android", isVisible = true),
                    operationDelaysSequence = listOf(
                        AiutaOperationDelay(mode = "recurring", delay = 1.0, repeat = 4),
                        AiutaOperationDelay(mode = "infinite", delay = 3.0),
                    ),
                ),
                etag = "\"v1\"",
            ),
            response,
        )
        assertEquals("/subscription_details", requests.single().url.encodedPath)
        assertNull(requests.single().headers[HttpHeaders.IfNoneMatch])
    }

    @Test
    fun revalidatesByEtag() = runTest {
        val response = dataSource { respond(content = "", status = HttpStatusCode.NotModified) }
            .getSubscriptionDetails(etag = "\"v1\"")

        assertEquals(SubscriptionDetailsResponse.NotModified, response)
        assertEquals("\"v1\"", requests.single().headers[HttpHeaders.IfNoneMatch])
    }

    @Test
    fun notModifiedThrownByTheSdkClientIsNotAnError() = runTest {
        // The SDK client throws FashionIOException for every status but 200 and 401
        val response = dataSource {
            throw FashionIOException(errorCode = 304, errorMessages = emptyList(), requestId = "", date = "")
        }.getSubscriptionDetails(etag = "\"v1\"")

        assertEquals(SubscriptionDetailsResponse.NotModified, response)
    }

    @Test
    fun otherSdkClientErrorsFail() = runTest {
        val dataSource = dataSource {
            throw FashionIOException(errorCode = 500, errorMessages = emptyList(), requestId = "", date = "")
        }

        assertFailsWith<FashionIOException> { dataSource.getSubscriptionDetails(etag = null) }
    }

    @Test
    fun unauthorizedResponseFailsInsteadOfParsingAsEmptyDetails() = runTest {
        // The SDK client lets 401 through for the JWT refresh
        val dataSource = dataSource {
            json(status = HttpStatusCode.Unauthorized, body = """{"message": "Invalid authentication credentials"}""")
        }

        assertFailsWith<IllegalStateException> { dataSource.getSubscriptionDetails(etag = null) }
    }
}
