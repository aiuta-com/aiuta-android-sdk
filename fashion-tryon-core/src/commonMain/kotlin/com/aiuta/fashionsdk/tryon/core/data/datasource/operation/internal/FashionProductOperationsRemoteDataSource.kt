package com.aiuta.fashionsdk.tryon.core.data.datasource.operation.internal

import com.aiuta.fashionsdk.network.NetworkClient
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.FashionProductOperationsDataSource
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models.CreateProductOperationRequest
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models.CreateProductOperationResponse
import com.aiuta.fashionsdk.tryon.core.data.datasource.operation.models.ProductOperation
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

internal class FashionProductOperationsRemoteDataSource(
    private val networkClient: NetworkClient,
) : FashionProductOperationsDataSource {
    override suspend fun createProductOperation(
        request: CreateProductOperationRequest,
    ): CreateProductOperationResponse = networkClient.httpClient.value.post(
        urlString = PATH_SKU_OPERATIONS,
    ) {
        setBody(request)
    }.body()

    override suspend fun getProductOperation(operationId: String): ProductOperation = networkClient.httpClient.value.get(
        urlString = "$PATH_SKU_OPERATIONS/$operationId",
    ).body()

    private companion object {
        const val PATH_SKU_OPERATIONS = "sku_try_on_operations"
    }
}
