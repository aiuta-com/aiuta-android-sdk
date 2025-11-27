package com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.generated.images

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.generated.images.GeneratedImageDatasource
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.toEntity
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.toUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DatabaseGeneratedImageInteractor(
    private val generatedImageDatasource: GeneratedImageDatasource,
) : LocalGeneratedImageInteractor {
    override suspend fun insertAll(
        generatedProductIds: List<String>,
        images: List<GeneratedImageUIModel>,
    ): Result<Unit> = runCatching {
        generatedImageDatasource.insertAll(
            generatedImages = images.map { it.toEntity() },
        )
    }

    override fun generatedImagesFlow(): Flow<PagingData<GeneratedImageUIModel>> = Pager(
        config = PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
        ),
        pagingSourceFactory = {
            generatedImageDatasource.pagingSource()
        },
    )
        .flow
        .map { pagingData ->
            pagingData.map { it.toUiModel() }
        }

    override suspend fun remove(generatedImages: List<GeneratedImageUIModel>): Result<Unit> = runCatching {
        generatedImageDatasource.remove(
            generatedImageIds = generatedImages.map { it.id },
        )
    }

    override suspend fun removeAll(): Result<Unit> = runCatching {
        generatedImageDatasource.removeAll()
    }

    override fun countFlow(): Flow<Int> = generatedImageDatasource.countFlow()

    companion object {
        private const val DEFAULT_PAGE_SIZE = 10

        fun getInstance(platformContext: AiutaPlatformContext): DatabaseGeneratedImageInteractor = DatabaseGeneratedImageInteractor(
            generatedImageDatasource = GeneratedImageDatasource.getInstance(
                platformContext = platformContext,
            ),
        )
    }
}
