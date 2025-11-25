package com.aiuta.fashionsdk.tryon.core

import com.aiuta.fashionsdk.Aiuta
import com.aiuta.fashionsdk.network.exceptions.FashionIOException
import com.aiuta.fashionsdk.network.exceptions.FashionNetworkDisconnectedException
import com.aiuta.fashionsdk.network.paging.models.PageContainer
import com.aiuta.fashionsdk.network.paging.models.PaginationOffset
import com.aiuta.fashionsdk.tryon.core.domain.AiutaTryOnImpl
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductCatalog
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationContainer
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationItem
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationStatus
import kotlinx.coroutines.flow.Flow

/**
 * Default Kotlin extension for creating [AiutaTryOn]
 *
 * @return new instance of [AiutaTryOn]
 */
public val Aiuta.tryon: AiutaTryOn
    get() = AiutaTryOnImpl.create(aiuta = this)

/**
 * Entry point for all functionality of Digital try on
 */
public interface AiutaTryOn {
    /**
     * Get new page of [ProductCatalog]
     *
     * @param paginationOffset Offset for new page request
     * @param paginationLimit Limit for items in page
     * @throws FashionIOException If the backend returns an error response
     * @throws FashionNetworkDisconnectedException If the network is unavailable
     */
    public suspend fun getProductCatalogs(
        paginationOffset: PaginationOffset? = null,
        paginationLimit: Int? = null,
    ): PageContainer<ProductCatalog>

    /**
     * Get new page of [ProductGenerationItem]
     *
     * @param catalogName Name of catalog for concrete product
     * @param paginationOffset Offset for new page request
     * @param paginationLimit Limit for items in page
     * @throws FashionIOException If the backend returns an error response
     * @throws FashionNetworkDisconnectedException If the network is unavailable
     */
    public suspend fun getProductItems(
        catalogName: String,
        paginationOffset: PaginationOffset? = null,
        paginationLimit: Int? = null,
    ): PageContainer<ProductGenerationItem>

    /**
     * Start product generation by user image
     *
     * Pipeline is next:
     * - Try to compress image or use origin
     * - Upload image on backend for processing
     * - Create generation operation and wait for result
     *
     * @return flow with statuses of started generation
     */
    public fun startProductGeneration(container: ProductGenerationContainer): Flow<ProductGenerationStatus>
}
