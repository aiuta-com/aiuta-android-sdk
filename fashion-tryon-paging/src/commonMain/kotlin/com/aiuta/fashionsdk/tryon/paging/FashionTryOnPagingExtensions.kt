package com.aiuta.fashionsdk.tryon.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.aiuta.fashionsdk.tryon.core.AiutaTryOn
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductCatalog
import com.aiuta.fashionsdk.tryon.core.domain.models.ProductGenerationItem
import com.aiuta.fashionsdk.tryon.paging.ContainerPagingSource
import kotlinx.coroutines.flow.Flow

/**
 * Extension for using [getProductItems] method with androidx.paging library
 */
public fun AiutaTryOn.getProductItems(
    catalogName: String,
    config: PagingConfig = PagingConfig(pageSize = ContainerPagingSource.DEFAULT_PAGE_SIZE),
): Flow<PagingData<ProductGenerationItem>> = Pager(
    config = config,
    pagingSourceFactory = {
        ContainerPagingSource {
            getProductItems(
                catalogName = catalogName,
                paginationOffset = it,
            )
        }
    },
).flow

/**
 * Extension for using [getProductCatalogs] method with androidx.paging library
 */
public fun AiutaTryOn.getProductCatalogs(
    config: PagingConfig = PagingConfig(pageSize = ContainerPagingSource.DEFAULT_PAGE_SIZE),
): Flow<PagingData<ProductCatalog>> = Pager(
    config = config,
    pagingSourceFactory = {
        ContainerPagingSource {
            getProductCatalogs(
                paginationOffset = it,
            )
        }
    },
).flow
