package com.aiuta.fashionsdk.tryon.core.data.datasource.sku.internal

import com.aiuta.fashionsdk.network.NetworkClient
import com.aiuta.fashionsdk.network.paging.models.PageContainer
import com.aiuta.fashionsdk.network.paging.models.PaginationOffset
import com.aiuta.fashionsdk.network.paging.utils.saveAppend
import com.aiuta.fashionsdk.network.utils.saveAppendLimit
import com.aiuta.fashionsdk.tryon.core.data.datasource.sku.FashionProductDataSource
import com.aiuta.fashionsdk.tryon.core.data.datasource.sku.models.ProductCatalogDTO
import com.aiuta.fashionsdk.tryon.core.data.datasource.sku.models.ProductItemDTO
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class FashionProductRemoteDataSource(
    private val networkClient: NetworkClient,
) : FashionProductDataSource {
    // SKU catalogs
    override suspend fun getProductCatalogs(
        paginationOffset: PaginationOffset?,
        paginationLimit: Int?,
    ): PageContainer<ProductCatalogDTO> = networkClient.httpClient.value.get(
        urlString = PATH_SKU_CATALOGS,
    ) {
        url {
            saveAppend(paginationOffset)
            saveAppendLimit(paginationLimit)
        }
    }.body()

    override suspend fun getProductItems(
        productCatalogName: String,
        paginationOffset: PaginationOffset?,
        paginationLimit: Int?,
    ): PageContainer<ProductItemDTO> = networkClient.httpClient.value.get(
        urlString = "$PATH_SKU_ITEMS/$productCatalogName",
    ) {
        url {
            saveAppend(paginationOffset)
            saveAppendLimit(paginationLimit)
        }
    }.body()

    override suspend fun getProductItem(
        productCatalogName: String,
        productId: String,
    ): ProductItemDTO = networkClient.httpClient.value.get(
        urlString = "$PATH_SKU_ITEMS/$productCatalogName/$productId",
    ).body()

    private companion object {
        const val PATH_SKU_ITEMS = "sku_items"
        const val PATH_SKU_CATALOGS = "sku_catalogs"
    }
}
