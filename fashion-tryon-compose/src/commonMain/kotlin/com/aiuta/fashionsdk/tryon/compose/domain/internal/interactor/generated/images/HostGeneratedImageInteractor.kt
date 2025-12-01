package com.aiuta.fashionsdk.tryon.compose.domain.internal.interactor.generated.images

import androidx.paging.PagingData
import com.aiuta.fashionsdk.configuration.features.tryon.history.dataprovider.AiutaTryOnGenerationsHistoryFeatureDataProviderCustom
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.GeneratedImageUIModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.toImageUiModel
import com.aiuta.fashionsdk.tryon.compose.domain.models.internal.generated.images.toPublic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class HostGeneratedImageInteractor(
    private val dataProvider: AiutaTryOnGenerationsHistoryFeatureDataProviderCustom,
) : GeneratedImageInteractor {
    override suspend fun insertAll(
        generatedProductIds: List<String>,
        images: List<GeneratedImageUIModel>,
    ): Result<Unit> = runCatching {
        dataProvider.addGeneratedImages(
            productIds = generatedProductIds,
            images = images.map { image -> image.toPublic() },
        )
    }

    override fun generatedImagesFlow(): Flow<PagingData<GeneratedImageUIModel>> = dataProvider.generatedImages
        .map { images ->
            images.map { image -> image.toImageUiModel() }
        }
        .map { images -> PagingData.from(images) }

    override suspend fun remove(generatedImages: List<GeneratedImageUIModel>): Result<Unit> = runCatching {
        dataProvider.deleteGeneratedImages(
            images = generatedImages.map { image -> image.toPublic() },
        )
    }

    override suspend fun removeAll(): Result<Unit> {
        // Ignore this, because host should solve current list of generations
        return Result.success(Unit)
    }

    override fun countFlow(): Flow<Int> = dataProvider.generatedImages.map { images -> images.size }

    companion object {
        fun getInstance(
            dataProvider: AiutaTryOnGenerationsHistoryFeatureDataProviderCustom,
        ): HostGeneratedImageInteractor = HostGeneratedImageInteractor(dataProvider = dataProvider)
    }
}
