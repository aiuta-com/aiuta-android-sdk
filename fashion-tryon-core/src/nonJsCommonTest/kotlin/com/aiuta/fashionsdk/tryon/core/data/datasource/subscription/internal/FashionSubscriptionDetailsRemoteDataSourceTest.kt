package com.aiuta.fashionsdk.tryon.core.data.datasource.subscription.internal

import com.aiuta.fashionsdk.network.NetworkClient
import com.aiuta.fashionsdk.tryon.core.data.datasource.subscription.models.OperationDelay
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
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

internal class FashionSubscriptionDetailsRemoteDataSourceTest {
    private val requestedPaths = mutableListOf<String>()

    /** Data source over a client configured like the SDK one (ignoreUnknownKeys) */
    private fun dataSource(status: HttpStatusCode, body: String): FashionSubscriptionDetailsRemoteDataSource {
        val engine = MockEngine { request ->
            requestedPaths += request.url.encodedPath
            respond(
                content = body,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        return FashionSubscriptionDetailsRemoteDataSource(
            networkClient = object : NetworkClient {
                override val httpClient: Lazy<HttpClient> = lazyOf(client)
            },
        )
    }

    @Test
    fun parsesOperationDelaysSequenceAndIgnoresOtherFields() = runTest {
        val details = dataSource(
            status = HttpStatusCode.OK,
            body = """
                {
                  "subscription_id": "subscription",
                  "powered_by_sticker": {"is_visible": true},
                  "operation_delays_sequence": [
                    {"mode": "recurring", "delay": 1.0, "repeat": 4},
                    {"mode": "infinite", "delay": 3.0}
                  ]
                }
            """.trimIndent(),
        ).getOperationDelaysDetails()

        assertEquals(
            listOf(
                OperationDelay(mode = "recurring", delay = 1.0, repeat = 4),
                OperationDelay(mode = "infinite", delay = 3.0),
            ),
            details.operationDelaysSequence,
        )
        assertEquals(listOf("/subscription_details"), requestedPaths)
    }

    @Test
    fun responseWithoutDelaysHasNoSequence() = runTest {
        val details = dataSource(
            status = HttpStatusCode.OK,
            body = """{"subscription_id": "subscription"}""",
        ).getOperationDelaysDetails()

        assertNull(details.operationDelaysSequence)
    }

    @Test
    fun unauthorizedResponseFailsInsteadOfParsingAsNoDelays() = runTest {
        // The SDK client lets 401 through for the JWT refresh
        val dataSource = dataSource(
            status = HttpStatusCode.Unauthorized,
            body = """{"message": "Invalid authentication credentials"}""",
        )

        assertFailsWith<IllegalStateException> { dataSource.getOperationDelaysDetails() }
    }
}
