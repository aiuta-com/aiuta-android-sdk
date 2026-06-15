package com.aiuta.fashionsdk.tryon.core.data.datasource.sku

import com.aiuta.fashionsdk.network.paging.models.PageContainer
import com.aiuta.fashionsdk.tryon.core.data.datasource.sku.models.ProductItemDTO

internal interface FashionProductDataSource {

    suspend fun getProductItems(
        paginationOffset: Int? = null,
        paginationLimit: Int? = null,
    ): PageContainer<ProductItemDTO>

    suspend fun getProductItem(
        productCatalogName: String,
        productId: String,
    ): ProductItemDTO
}
