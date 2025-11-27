package com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.generated.images

import androidx.paging.PagingData
import com.aiuta.fashionsdk.context.AiutaPlatformContext
import com.aiuta.fashionsdk.tryon.compose.data.internal.datasource.generated.images.GeneratedImageDatasource
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class EmptyGeneratedImageInteractor(
    private val generatedImageDatasource: GeneratedImageDatasource,
) : LocalGeneratedImageInteractor {
    override suspend fun insertAll(
        generatedProductIds: List<String>,
        images: List<GeneratedImageUIModel>,
    ): Result<Unit> = Result.success(Unit)

    override fun generatedImagesFlow(): Flow<PagingData<GeneratedImageUIModel>> = flowOf(PagingData.empty())

    override suspend fun remove(generatedImages: List<GeneratedImageUIModel>): Result<Unit> = Result.success(Unit)

    override suspend fun removeAll(): Result<Unit> {
        // Only support remove local, because if it's empty - we should remove all local images
        return runCatching { generatedImageDatasource.removeAll() }
    }

    override fun countFlow(): Flow<Int> = flowOf(0)

    companion object {
        fun getInstance(platformContext: AiutaPlatformContext): EmptyGeneratedImageInteractor = EmptyGeneratedImageInteractor(
            generatedImageDatasource = GeneratedImageDatasource.getInstance(
                platformContext = platformContext,
            ),
        )
    }
}
