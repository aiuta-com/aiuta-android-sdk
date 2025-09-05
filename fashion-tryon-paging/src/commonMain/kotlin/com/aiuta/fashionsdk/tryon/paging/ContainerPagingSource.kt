package com.aiuta.fashionsdk.tryon.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aiuta.fashionsdk.network.paging.models.PageContainer
import com.aiuta.fashionsdk.network.paging.models.PaginationDirection
import com.aiuta.fashionsdk.network.paging.models.PaginationOffset

/**
 * Base paging source container for implementation paging with using [androidx.paging.Pager]
 *
 * @param loadBackend Lambda for getting new page from backend
 */
public class ContainerPagingSource<T : Any>(
    private val loadBackend: suspend (PaginationOffset?) -> PageContainer<T>,
) : PagingSource<PaginationOffset, T>() {
    override fun getRefreshKey(state: PagingState<PaginationOffset, T>): PaginationOffset? = state.anchorPosition?.let { anchorPosition ->
        val anchorPage = state.closestPageToPosition(anchorPosition)
        anchorPage?.nextKey
    }

    override suspend fun load(
        params: LoadParams<PaginationOffset>,
    ): LoadResult<PaginationOffset, T> = try {
        val response = loadBackend(params.key)
        LoadResult.Page(
            data = response.result,
            prevKey = null,
            nextKey =
            response.afterKey
                .ifEmpty { null }
                ?.let { PaginationOffset(PaginationDirection.AFTER, it) },
            itemsBefore = if (params.key == null) 0 else LoadResult.Page.COUNT_UNDEFINED,
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    public companion object {
        public const val DEFAULT_PAGE_SIZE: Int = 10
    }
}
